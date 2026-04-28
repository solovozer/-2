package com.example.Monies;

public class CADMoney extends BaseMoney{
    public CADMoney(long amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "CAD";
    } 
}
