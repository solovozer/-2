package com.example;

import io.javalin.Javalin;

import com.example.Repository.AccountRepository;
import com.example.Repository.UserRepository;
import com.example.Service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;


public class App 
{
    public static void main( String[] args )
    {
        DatabaseConfig.initialize();
        
        AccountRepository repo = new AccountRepository();
        TransferService transferService = new TransferService(repo);
        
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/static";
                staticFiles.location = io.javalin.http.staticfiles.Location.CLASSPATH;
            });
        }).start(7000);
        
        ObjectMapper objectMapper = new ObjectMapper();

        app.get("/", ctx -> ctx.html(serveFile("src/main/resources/static/templates/index.html")));
        app.get("/create-user", ctx -> ctx.html(serveFile("src/main/resources/static/templates/create-user.html")));
        app.get("/create-account", ctx -> ctx.html(serveFile("src/main/resources/static/templates/create-account.html")));
        app.get("/user-info", ctx -> ctx.html(serveFile("src/main/resources/static/templates/user-info.html")));
        app.get("/account-info", ctx -> ctx.html(serveFile("src/main/resources/static/templates/account-info.html")));
        app.get("/transfer-money", ctx -> ctx.html(serveFile("src/main/resources/static/templates/transfer-money.html")));
        app.get("/convert-money", ctx -> ctx.html(serveFile("src/main/resources/static/templates/convert-money.html")));
        app.get("/transaction-history", ctx -> ctx.html(serveFile("src/main/resources/static/templates/transaction-history.html")));


        app.get("/currencies", ctx -> ctx.json(CurrencyConstants.SUPPORTED_CURRENCIES));
        
        app.post("/users", ctx -> {
            try {
                CreateUserRequest request = objectMapper.readValue(ctx.body(), CreateUserRequest.class);
                
                User existingUser = new UserRepository().findByUsername(request.username);
                if (existingUser != null) {
                    ctx.status(409).result("Username '" + request.username + "' is already taken");
                    return;
                }
                
                User user = new User(request.name, request.email, request.username, request.password);
                saveUser(user);
                ctx.result("User created");
            } catch (Exception e) {
                ctx.status(400).result("Failed to create user: " + e.getMessage());
            }
        });


        app.post("/accounts", ctx -> {
            try {
                CreateAccountRequest request = objectMapper.readValue(ctx.body(), CreateAccountRequest.class);
                
                Account existingAccount = repo.findById(request.id);
                if (existingAccount != null) {
                    ctx.status(409).result("Account with Name '" + request.id + "' already exists");
                    return;
                }
                
                Money initialBalance = new Money(request.initialBalance, request.currency);
                Account account = new Account(request.id, request.name, initialBalance);
                saveAccount(account);
                ctx.result("Account created");
            } catch (Exception e) {
                ctx.status(400).result("Failed to create account: " + e.getMessage());
            }
        });
        
        //Account balance
        app.get("/accounts/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Account account = repo.findById(id);
            if (account != null) {
                ctx.json(new AccountResponse(account.getId(), account.getOwner(), account.getBalance().getFormattedAmount()));
            } else {
                ctx.status(404).result("Account not found");
            }
        });
        
        //Account transactions
        app.get("/accounts/{id}/transactions", ctx -> {
            String id = ctx.pathParam("id");
            DatabaseConfig db = new DatabaseConfig();
            java.util.List<DatabaseConfig.TransactionRecord> transactions = db.getTransactionsForAccount(id);
            ctx.json(transactions);
        });

        app.get("/accounts/transactions/ALL", ctx -> {
            DatabaseConfig db = new DatabaseConfig();
            java.util.List<DatabaseConfig.TransactionRecord> transactions = db.getAllTransactionHistory();
            ctx.json(transactions);
        });
        
        //Transfer money
        app.post("/transfer", ctx -> {
            try {
                TransferRequest request = objectMapper.readValue(ctx.body(), TransferRequest.class);
                Money amount = new Money(request.amount, request.currency);
                transferService.transfer(request.fromId, request.toId, amount);
                ctx.result("Transfer successful");
            } catch (Exception e) {
                ctx.status(400).result("Transfer failed: " + e.getMessage());
            }
        });
        
        //Wipe database
        app.post("/api/admin/487135rrdbfe854y/wipe", ctx -> {
            try {
                wipeDatabase();
                ctx.result("Database wiped successfully");
            } catch (Exception e) {
                ctx.status(500).result("Failed to wipe database: " + e.getMessage());
            }
        });
        
        System.out.println("Server started on port 7000");
    }

    private static void saveUser(User user) {
        String sql = "INSERT INTO users(id, name, email, username, password) VALUES(?,?,?,?,?)";
        
        try (java.sql.Connection conn = DatabaseConfig.connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUsername());
            pstmt.setString(5, user.getPassword());
            
            pstmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void saveAccount(Account account) {
        String sql = "INSERT INTO accounts(id, user_id, balance_amount, balance_currency) VALUES(?,?,?,?)";
        
        try (java.sql.Connection conn = DatabaseConfig.connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getId());
            pstmt.setString(2, account.getOwner());
            pstmt.setBigDecimal(3, account.getBalance().getAmount());
            pstmt.setString(4, account.getBalance().getCurrency());
            
            pstmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void wipeDatabase() {
        // 1. Connection logic
        try (java.sql.Connection conn = DatabaseConfig.connect();
            java.sql.Statement stmt = conn.createStatement()) {
            
            conn.setAutoCommit(false);

            try {
                stmt.executeUpdate("DELETE FROM transactions");
                stmt.executeUpdate("DELETE FROM users");
                conn.commit();
            } catch (java.sql.SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (java.sql.SQLException e) {
            System.err.println("Database Wipe Failed: " + e.getMessage());
            throw new RuntimeException("Wipe failed", e);
        }
    }
    
    private static String serveFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            return "<h1>Error loading page</h1><p>" + e.getMessage() + "</p>";
        }
    }
    
    static class TransferRequest {
        public String fromId;
        public String toId;
        public BigDecimal amount;
        public String currency;
    }
    
    static class CreateUserRequest {
        public String name;
        public String email;
        public String username;
        public String password;
    }

    static class CreateAccountRequest {
        public String name;
        public BigDecimal initialBalance;
        public String currency;
    }
    
    static class AccountResponse {
        public User owner;
        public String balance;
        
        public AccountResponse(User owner, String balance) {
            this.owner = owner;
            this.balance = balance;
        }
    }
}
