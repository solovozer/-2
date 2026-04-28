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
        if (!balance.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        this.balance = new Money(this.balance.getAmount() + amount.getAmount(), this.getCurrency());
    }

    public void withdraw(Money amount) {
        if (!balance.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        if (this.balance.getAmount() < amount.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        this.balance = new Money(this.balance.getAmount() - amount.getAmount(), this.getCurrency());
    }

    public Money getBalance() { return balance; }
    public String getId() { return id; }
    public String getOwner() { return owner; }
}
