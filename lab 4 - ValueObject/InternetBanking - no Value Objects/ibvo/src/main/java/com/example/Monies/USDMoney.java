package com.example.Monies;


public class USDMoney extends BaseMoney {
    public USDMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "USD";
    }
}