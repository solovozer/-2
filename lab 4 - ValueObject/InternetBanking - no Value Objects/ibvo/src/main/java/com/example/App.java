package com.example;

import io.javalin.Javalin;

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
        TransferService transferService = new TransferService(accountRepo);
        ExchangeService exchangeService = new ExchangeService(accountRepo);

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
        app.get("/convert-currency", ctx -> ctx.html(serveFile("src/main/resources/static/templates/convert-currency.html")));
        app.get("/transaction-history", ctx -> ctx.html(serveFile("src/main/resources/static/templates/transaction-history.html")));
        app.get("/conversion-history", ctx -> ctx.html(serveFile("src/main/resources/static/templates/conversion-history.html")));


        app.get("/currencies", ctx -> ctx.json(CurrencyConstants.SUPPORTED_CURRENCIES));
        
        app.post("/users", ctx -> {
            try {
                CreateUserRequest request = objectMapper.readValue(ctx.body(), CreateUserRequest.class);
                
                User existingUser = accountRepo.findUserByUsername(request.username);
                if (existingUser != null) {
                    ctx.status(409).result("Username '" + request.username + "' is taken");
                    return;
                }
                
                User user = new User(request.name, request.email, request.username, request.password);
                saveUser(user);
                ctx.result("User " + user.getUsername() + " created");
            } catch (Exception e) {
                ctx.status(400).result("Failed to create user: " + e.getMessage());
            }
        });


        app.post("/accounts", ctx -> {
            try {
                CreateAccountRequest request = objectMapper.readValue(ctx.body(), CreateAccountRequest.class);
                User user = accountRepo.findUserByUsername(request.username);
                
                if (user == null) {
                    ctx.status(404).result("User with username '" + request.username + "' not found");
                    return;
                } else {
                    try {
                        Account currencyAccount = accountRepo.GetCurrencyAccount(user.getId(), request.currency);
                        if (currencyAccount != null) {
                            ctx.status(400).result("User already has an account with currency '" + request.currency + "'. Please choose a different currency.");
                            return;
                        }
                    } catch (Exception e) {
                        ctx.status(400).result("Invalid currency '" + request.currency + "'");
                        return;
                    }
                }
                String accountId = java.util.UUID.randomUUID().toString();
                Money initialBalance = new Money(request.initialBalance, request.currency);
                Account account = new Account(accountId, user.getId(), initialBalance);
                saveAccount(account);
                ctx.result(request.currency + " account created successfully");
            } catch (Exception e) {
                ctx.status(400).result("Failed to create account: " + e.getMessage());
            }
        });
        
        app.get("/accounts/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Account account = accountRepo.findById(id);
            if (account != null) {
                // Get recent transactions for this account
                DatabaseConfig db = new DatabaseConfig();
                List<DatabaseConfig.TransactionRecord> transactions = db.getRecentTransactionsForAccount(id, 10);
                ctx.json(new AccountWithTransactionsResponse(account, transactions));
            } else {
                ctx.status(404).result("Account not found");
            }
        });

        app.get("/users-by-username/{username}", ctx -> {
            String username = ctx.pathParam("username");
            User user = accountRepo.findUserByUsername(username);
            if (user != null) {
                // Load accounts for this user
                List<Account> accounts = accountRepo.getAllAccountsFromUser(user.getId()); // Changed method name
                user.setAccounts(accounts);
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        });
        
        app.get("/accounts/{id}/transactions", ctx -> {
            String id = ctx.pathParam("id");
            DatabaseConfig db = new DatabaseConfig();
            Account account = accountRepo.findById(id);
            List<DatabaseConfig.TransactionRecord> transactions = db.getTransactionsForAccount(account);
            ctx.json(transactions);
        });


        app.get("/accounts/transactions/ALL", ctx -> {
            DatabaseConfig db = new DatabaseConfig();
            List<DatabaseConfig.TransactionRecord> transactions = db.getAllTransactionHistory();
            ctx.json(transactions);
        });
        
        //Transfer money - simplified to work with TransferService
        app.post("/transfer", ctx -> {
            try {
                TransferRequest request = objectMapper.readValue(ctx.body(), TransferRequest.class);
                
                User fromUser = accountRepo.findUserByUsername(request.fromUser);
                User toUser = accountRepo.findUserByUsername(request.toUser);
                
                if (fromUser == null) {
                    ctx.status(404).result("Sender not found");
                    return;
                }
                
                if (toUser == null) {
                    ctx.status(404).result("Recipient not found");
                    return;
                }
                
                Account fromAccount = accountRepo.GetCurrencyAccount(fromUser.getId(), request.currency);
                Account toAccount = accountRepo.GetCurrencyAccount(toUser.getId(), request.currency);

                if (fromAccount == null) { 
                    ctx.status(404).result("Sender's " + request.currency + " account not found");
                    return;
                }

                if (toAccount == null) {
                    ctx.status(404).result("Recipient's " + request.currency + " account not found");
                    return;
                }

                Money amount = new Money(request.amount, request.currency);
                try {
                    transferService.transfer(fromAccount.getId(), toAccount.getId(), amount);
                } catch (IllegalArgumentException e) {
                    ctx.status(400).result("Not enough money in the sender's account. Please convert currency or reduce the transfer amount.");
                }
                ctx.result("Transfer successful");
            } catch (Exception e) {
                ctx.status(400).result("Transfer failed: " + e.getMessage());
            }
        });
        

        app.post("/convert", ctx -> {
            try {
                ConvertRequest request = objectMapper.readValue(ctx.body(), ConvertRequest.class);
                User user = accountRepo.findUserByUsername(request.username);
                
                if (user == null) {
                    ctx.status(404).result("User not found");
                    return;
                }
                
                Money originalAmount = new Money(request.amount, request.sourceCurrency);
                exchangeService.exchange(user.getId(), originalAmount, request.targetCurrency);
                ctx.result("Successfully converted " + request.sourceCurrency + " to " + request.targetCurrency);
            } catch (Exception e) {
                ctx.status(400).result("Conversion failed: " + e.getMessage());
            }
        }); 

        app.get("/conversion-history/{username}", ctx -> {
            String username = ctx.pathParam("username");
            User user = accountRepo.findUserByUsername(username);
            if (user == null) {
                ctx.status(404).result("User not found");
                return;
            }
            DatabaseConfig db = new DatabaseConfig();
            ctx.json(db.getConversionHistory(user.getId()));
        });
        
        // Endpoint to get exchange rate
        app.get("/exchange-rate", ctx -> {
            try {
                String sourceCurrency = ctx.queryParam("from");
                String targetCurrency = ctx.queryParam("to");
                BigDecimal amount = new BigDecimal(ctx.queryParam("amount"));
                
                if (sourceCurrency == null || targetCurrency == null) {
                    ctx.status(400).result("Both 'from' and 'to' query parameters are required");
                    return;
                }
                
                Money sourceMoney = new Money(amount, sourceCurrency);
                Money convertedMoney = exchangeService.convert(sourceMoney, targetCurrency);
                
                ctx.result(amount.floatValue() + " " + sourceCurrency + " = " + convertedMoney.getAmount().floatValue() + " " + targetCurrency);
            } catch (Exception e) {
                ctx.status(400).result("Failed to get exchange rate: " + e.getMessage());
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
            pstmt.setString(5, user.getPassword()); // Added password
            
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
                stmt.executeUpdate("DELETE FROM accounts");
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
        public String fromUser;
        public String toUser;
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
        public String username;
        public BigDecimal initialBalance;
        public String currency;
    }
    
   
    static class ConvertRequest {
        public String username;
        public BigDecimal amount;
        public String sourceCurrency;
        public String targetCurrency;
    }

    static class ExchangeRequest {
        public BigDecimal amount;
        public String sourceCurrency;
        public String targetCurrency;
    }
    
    static class AccountWithTransactionsResponse {
        public String id;
        public String userId;
        public String balance;
        public List<DatabaseConfig.TransactionRecord> transactions;
        
        public AccountWithTransactionsResponse(Account account, List<DatabaseConfig.TransactionRecord> transactions) {
            this.id = account.getId();
            this.userId = account.getUserId();
            this.balance = account.getBalance().getFormattedAmount();
            this.transactions = transactions;
        }
    }
}