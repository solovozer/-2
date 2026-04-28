package com.example.Monies;


public class VNDMoney extends BaseMoney {
    public VNDMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "VND";
    }
}