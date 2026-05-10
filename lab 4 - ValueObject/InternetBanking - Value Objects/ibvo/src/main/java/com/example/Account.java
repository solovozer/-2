package com.example;

import com.example.Record.AccountRecord;

public class Account {
    private final String id;
    private final User owner;
    private Money balance;

    public Account(User owner, Money initialBalance) {
        this.id = java.util.UUID.randomUUID().toString();
        this.owner = owner;
        this.balance = initialBalance;
    }

    public Account(AccountRecord ar) {
        this.id = ar.id;
        this.owner = new User(ar.ownerName, ar.ownerEmail, ar.ownerUsername, ar.ownerPassword);
        this.balance = new Money(ar.balanceAmount, ar.balanceCurrency);
    }

    public String getCurrency() {
        return balance.getCurrency();
    }

    public void deposit(Money amount) {
        balance = balance.add(amount);
    }

    public void withdraw(Money amount) {
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        balance = balance.subtract(amount);
    }

    public Money getBalance() { return balance; }
    public String getId() { return id; }
    public User getOwner() { return owner; }
}
