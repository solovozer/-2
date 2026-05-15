package com.example;

import java.math.BigDecimal;
public class Account {
    private final String id;
    private final String userId;
    private Money balance;

    public Account(String id, String userId, BigDecimal balance, String currency) {
        this.id = id;
        this.userId = userId;
        this.balance = new Money(balance, currency);
    }

    public String getCurrency() {
        return balance.getCurrency();
    }

    public void deposit(Money amount) {
        balance.add(amount);
    }

    public void withdraw(Money amount) {
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds!");
        }
        balance.subtract(amount);
    }

    public Money getBalance() { return balance; }
    public String getId() { return id; }
    public String getUserId() { return userId; }
}
