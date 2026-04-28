package com.example.Monies;

public class JPYMoney extends BaseMoney {
    public JPYMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "JPY";
    }
}
