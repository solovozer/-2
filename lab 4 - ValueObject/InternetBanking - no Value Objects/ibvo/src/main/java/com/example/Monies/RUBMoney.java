package com.example.Monies;

public class RUBMoney extends BaseMoney {
    public RUBMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "RUB";
    }
}
