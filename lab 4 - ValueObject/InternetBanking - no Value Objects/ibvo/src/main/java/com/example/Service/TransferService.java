package com.example.Service;

import com.example.Account;
import com.example.Money;
import com.example.Repository.AccountRepository;

public class TransferService {
    private final AccountRepository AR;

    public TransferService(AccountRepository repo) {
        AR = repo;
    } 
    
    public void transfer(String fromId, String toId, Money amount) {
        Account from = AR.findById(fromId);
        Account to = AR.findById(toId);

        if (from == null) {
            throw new RuntimeException("Sender's account not found");
        }
        
        if (to == null) {
            throw new RuntimeException("Recipient's account not found");
        }

        if (!from.getCurrency().equals(amount.getCurrency()) || 
            !to.getCurrency().equals(amount.getCurrency())) {
            throw new RuntimeException("Currency mismatch across accounts");
        }

        try {
            from.withdraw(amount);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        to.deposit(amount);

        AR.updateBalance(from);
        AR.updateBalance(to);
        AR.logTransaction(fromId, toId, amount);
    }
}