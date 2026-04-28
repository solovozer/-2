package com.example.Monies;

public class EURMoney extends BaseMoney {
    public EURMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "EUR";
    } 
}
