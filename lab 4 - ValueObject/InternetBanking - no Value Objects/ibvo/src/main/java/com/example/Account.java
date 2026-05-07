package com.example;

import com.example.Monies.BaseMoney;

public class Account {
    private final String id;
    private final String owner;
    private BaseMoney balance;

    public Account(String id, String owner, BaseMoney initialBalance) {
        this.id = id;
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getCurrency() {
        return balance.getCurrency();
    }

    public void deposit(BaseMoney amount) {
        balance.add(amount);
    }

    public void withdraw(BaseMoney amount) {
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        balance.subtract(amount);
    }

    public BaseMoney getBalance() { return balance; }
    public String getId() { return id; }
    public String getOwner() { return owner; }
}
