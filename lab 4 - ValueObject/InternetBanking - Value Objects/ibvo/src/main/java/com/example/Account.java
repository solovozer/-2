package com.example;

public class Account {
    private final String ID;
    private final String OWNER;
    private Money balance;

    public Account(String id, String owner, Money initialBalance) {
        this.ID = id;
        this.OWNER = owner;
        this.balance = initialBalance;
    }

    public String getCurrency() {
        return balance.getCurrency();
    }

    public void deposit(Money amount) {
        balance = balance.add(amount);
    }

    public void withdraw(Money amount) {
        if (balance.getAmount() < amount.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        balance = balance.subtract(amount);
    }

    public Money getBalance() { return balance; }
    public String getId() { return ID; }
    public String getOwner() { return OWNER; }
}
