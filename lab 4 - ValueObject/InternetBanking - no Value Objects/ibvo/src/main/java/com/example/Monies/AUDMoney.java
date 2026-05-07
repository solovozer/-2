package com.example.Monies;

import java.math.BigDecimal;

public class AUDMoney extends BaseMoney{
    public AUDMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "AUD";
    }   
}
