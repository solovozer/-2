package com.example;

import java.math.BigDecimal;

public class Money {
    private BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public String getFormattedAmount() {
        return String.format("%.2f %s", amount.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP), getCurrency());
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void add(Money other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount = this.amount.add(other.getAmount());
    }

    public void subtract(Money other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount = this.amount.subtract(other.getAmount());
    }

    public boolean equals(Money other) {
        if (other == null) return false;
        return this.amount.compareTo(other.amount) == 0 && this.currency.equals(other.currency);
    }


    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, getCurrency());
    }

}