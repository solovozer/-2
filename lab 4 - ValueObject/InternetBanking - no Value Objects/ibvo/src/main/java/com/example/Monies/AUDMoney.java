package com.example.Monies;

public class AUDMoney extends BaseMoney{
    public AUDMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "AUD";
    }   
}
