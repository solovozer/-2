package com.example.Monies;

public class CNYMoney extends BaseMoney{
    public CNYMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "CNY";
    }
}
