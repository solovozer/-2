package com.example.Monies;

import java.math.BigDecimal;

public class EURMoney extends BaseMoney {
    public EURMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "EUR";
    } 
}
