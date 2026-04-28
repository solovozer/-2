package com.example;

import java.util.Objects;

public final class Money {
    private final long amount;
    private final String currency;

    public Money(long amount, String currency) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || (!currency.equals("USD") && !currency.equals("VND"))) {
            throw new IllegalArgumentException("Unsupported or null currency");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public String getFormattedAmount() {
        return String.format("%.2f %s", amount / 100.0, currency);
    }

    public long getRawAmount() {
        return amount;
    }

    public long getAmount() {
        return amount;
    }

    public String getCurrency() { return currency; }
    
    public boolean isSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount && Objects.equals(currency, money.currency);
    }
}