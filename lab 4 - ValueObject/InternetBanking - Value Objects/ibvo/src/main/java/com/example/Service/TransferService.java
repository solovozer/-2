package com.example.Service;

import javax.jws.soap.SOAPBinding.Use;

import com.example.Account;
import com.example.Money;
import com.example.Repository.AccountRepository;
import com.example.Repository.UserRepository;

public class TransferService {
    private final AccountRepository AR;
    private final UserRepository UR;

    public TransferService(AccountRepository repo, UserRepository userRepo) {
        AR = repo;
        UR = userRepo;
    } 
    
    public void transfer(String fromId, String toId, Money amount) {
        Account from = Account(AR.findById(fromId));
        Account to = Account(AR.findById(toId));

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


        from.withdraw(amount);
        to.deposit(amount);

        AR.updateBalance(from);
        AR.updateBalance(to);
        AR.logTransaction(fromId, toId, amount);
    }
}