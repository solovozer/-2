app.get("/users-by-username/{username}", ctx -> {
            String username = ctx.pathParam("username");
            User user = userRepo.findByUsername(username);
            if (user != null) {
                // Load accounts for this user
                List<Account> accounts = accountRepo.getAllAccountsFromUser(user.getId()); // Changed method name
                user.setAccounts(accounts);
                
                // Also get the list of supported currencies to help frontend identify account currencies
                List<String> supportedCurrencies = CurrencyConstants.SUPPORTED_CURRENCIES;
                
                // Return user with accounts and supported currencies
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        });