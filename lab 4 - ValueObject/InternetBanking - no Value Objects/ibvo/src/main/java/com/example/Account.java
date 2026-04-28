package com.example;

import com.example.Monies.BaseMoney;

public class Account {
    private final String ID;
    private final String OWNER;
    private BaseMoney balance;

    public Account(String id, String owner, BaseMoney initialBalance) {
        this.ID = id;
        this.OWNER = owner;
        this.balance = initialBalance;
    }

    public String getCurrency() {
        return balance.getCurrency();
    }

    public void deposit(BaseMoney amount) {
        balance.add(amount);
    }

    public void withdraw(BaseMoney amount) {
        if (balance.getAmount() < amount.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        balance.subtract(amount);
    }

    public BaseMoney getBalance() { return balance; }
    public String getId() { return ID; }
    public String getOwner() { return OWNER; }
}
