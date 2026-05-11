package com.example;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || !CurrencyConstants.SUPPORTED_CURRENCIES.contains(currency)) {
            throw new IllegalArgumentException("Unsupported or null currency");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public String getFormattedAmount() {
        return String.format("%.2f %s", amount, currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() { return currency; }
    
    public boolean isSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }

    public Money add(Money amount) {
        if (!this.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.getAmount().add(amount.getAmount()), this.getCurrency());
    }

    public Money subtract(Money amount) {
        if (!this.isSameCurrency(amount)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.getAmount().subtract(amount.getAmount()), this.getCurrency());
    }
    
    public boolean isLessThan(Money other) {
        if (!this.isSameCurrency(other)) {
            throw new IllegalArgumentException("Cannot compare different currencies");
        }
        return this.getAmount().compareTo(other.getAmount()) < 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
    }
    

    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, currency);
    }
}