package com.example;

public class Account {
    private final String id;
    private final String owner;
    private Money balance;

    public Account(String id, String owner, Money initialBalance) {
        this.id = id;
        this.owner = owner;
        this.balance = initialBalance;
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
    public String getOwner() { return owner; }
}
