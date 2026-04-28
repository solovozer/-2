package com.example;

public class TransferService {
    private final AccountRepository ar;

    public TransferService(AccountRepository repo) {
        ar = repo;
    } 
    
    public void transfer(String fromId, String toId, Money amount) {
        Account from = ar.findById(fromId);
        Account to = ar.findById(toId);

        if (!from.getCurrency().equals(amount.getCurrency()) || 
            !to.getCurrency().equals(amount.getCurrency())) {
            throw new RuntimeException("Currency mismatch across accounts");
        }

        from.withdraw(amount);
        to.deposit(amount);

        ar.updateBalance(from);
        ar.updateBalance(to);
        ar.logTransaction(fromId, toId, amount);
    }
}