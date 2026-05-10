package com.example;

import io.javalin.Javalin;

import com.example.Repository.AccountRepository;
import com.example.Repository.UserRepository;
import com.example.Service.TransferService;
import com.example.Service.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class App 
{
    public static void main( String[] args )
    {
        DatabaseConfig.initialize();
        
        AccountRepository accountRepo = new AccountRepository();
        UserRepository userRepo = new UserRepository();
        TransferService transferService = new TransferService(accountRepo);
        ExchangeService exchangeService = new ExchangeService();
        
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
                
                // Check if the user exists by username (we'll need to pass username somehow)
                // Since we don't have the username in the request, we need to modify our approach
                // For now, let's assume we're getting userId instead of username
                User user = userRepo.findById(request.userId); // assuming we add userId to request
                
                if (user == null) {
                    ctx.status(404).result("User not found. Please create a user first.");
                    return;
                }
                
                // Check if account with given ID already exists using repository
                Account existingAccount = accountRepo.findById(request.id);
                if (existingAccount != null) {
                    ctx.status(409).result("Account with ID '" + request.id + "' already exists");
                    return;
                }
                
                Money initialBalance = new Money(request.initialBalance, request.currency);
                // Create a new Account with the provided ID and initial balance linked to the user
                Account account = new Account(request.id, user.getId(), initialBalance);
                saveAccount(account);
                ctx.result("Account created");
            } catch (Exception e) {
                ctx.status(400).result("Failed to create account: " + e.getMessage());
            }
        });
        
        //Account balance
        app.get("/users/{id}", ctx -> {
            String id = ctx.pathParam("id");
            User user = userRepo.findById(id);
            if (user != null) {
                // Load accounts for this user
                List<Account> accounts = accountRepo.getAllAccountsFromUser(id);
                user.setAccounts(accounts);
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        });
        
        //Account transactions
        app.get("/accounts/{id}/transactions", ctx -> {
            String id = ctx.pathParam("id");
            DatabaseConfig db = new DatabaseConfig();
            Account account = accountRepo.findById(id);
            java.util.List<DatabaseConfig.TransactionRecord> transactions = db.getTransactionsForAccount(account);
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
        
        // Currency conversion endpoint
        app.post("/convert", ctx -> {
            try {
                ConvertRequest request = objectMapper.readValue(ctx.body(), ConvertRequest.class);
                Money originalAmount = new Money(request.amount, request.sourceCurrency);
                Money convertedAmount = exchangeService.convert(originalAmount, request.targetCurrency);
                ctx.json(new ConvertResponse(
                    originalAmount.getFormattedAmount(),
                    convertedAmount.getFormattedAmount(),
                    request.targetCurrency
                ));
            } catch (Exception e) {
                ctx.status(400).result("Conversion failed: " + e.getMessage());
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
            pstmt.setString(2, account.getUserId());
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
        public String id;
        public String userId; // Added userId field
        public BigDecimal initialBalance;
        public String currency;
    }
    
    static class AccountResponse {
        public String owner;
        public String balance;
        
        public AccountResponse(String owner, String balance) {
            this.owner = owner;
            this.balance = balance;
        }
    }

    static class ConvertRequest {
        public BigDecimal amount;
        public String sourceCurrency;
        public String targetCurrency;
    }

    static class ConvertResponse {
        public String originalAmount;
        public String convertedAmount;
        public String targetCurrency;

        public ConvertResponse(String originalAmount, String convertedAmount, String targetCurrency) {
            this.originalAmount = originalAmount;
            this.convertedAmount = convertedAmount;
            this.targetCurrency = targetCurrency;
        }
    }
}
