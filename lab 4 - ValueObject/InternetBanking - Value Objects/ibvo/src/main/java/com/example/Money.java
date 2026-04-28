package com.example;

import java.util.Objects;

public final class Money {
    private final long AMOUNT;
    private final String CURRENCY;

    public Money(long amount, String currency) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || (!currency.equals("USD") && !currency.equals("VND"))) {
            throw new IllegalArgumentException("Unsupported or null currency");
        }
        this.AMOUNT = amount;
        this.CURRENCY = currency;
    }

    public String getFormattedAmount() {
        return String.format("%.2f %s", AMOUNT / 100.0, CURRENCY);
    }

    public long getAmount() {
        return AMOUNT;
    }

    public String getCurrency() { return CURRENCY; }
    
    public boolean isSameCurrency(Money other) {
        return this.CURRENCY.equals(other.CURRENCY);
    }

    public Money add(Money amount) {
        if (!this.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.getAmount() + amount.getAmount(), this.getCurrency());
    }

    public Money subtract(Money amount) {
        if (!this.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.getAmount() - amount.getAmount(), this.getCurrency());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return AMOUNT == money.AMOUNT && Objects.equals(CURRENCY, money.CURRENCY);
    }
}