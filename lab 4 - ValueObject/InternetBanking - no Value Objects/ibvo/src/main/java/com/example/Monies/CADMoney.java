package com.example.Monies;

import java.math.BigDecimal;

public class CADMoney extends BaseMoney{
    public CADMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "CAD";
    } 
}
