package com.example.Monies;

public abstract class BaseMoney {
    private long amount; // Value in minor units (e.g., cents)

    public BaseMoney(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public abstract String getCurrency();

    public String getFormattedAmount() {
        return String.format("%.2f %s", amount / 100.0, getCurrency());
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void add(BaseMoney other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount += other.getAmount();
    }

    public void subtract(BaseMoney other) {
        if (!this.getCurrency().equals(other.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        this.amount -= other.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMoney baseMoney = (BaseMoney) o;
        return amount == baseMoney.amount && getCurrency().equals(baseMoney.getCurrency());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, getCurrency());
    }

}