package com.example.Monies;

import java.util.Objects;
import java.math.BigDecimal;

public abstract class BaseMoney {
    private BigDecimal amount; // Value in minor units (e.g., cents)

    public BaseMoney(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public abstract String getCurrency();

    public String getFormattedAmount() {
        return String.format("%.2f %s", amount.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP), getCurrency());
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void add(BaseMoney other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount = this.amount.add(other.getAmount());
    }

    public void subtract(BaseMoney other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount = this.amount.subtract(other.getAmount());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseMoney money = (BaseMoney) obj;
        return amount.compareTo(money.amount) == 0 && Objects.equals(getCurrency(), money.getCurrency());
    }


    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, getCurrency());
    }

}