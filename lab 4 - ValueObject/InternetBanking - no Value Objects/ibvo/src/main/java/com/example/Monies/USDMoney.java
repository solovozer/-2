package com.example.Monies;

import java.math.BigDecimal;

public class USDMoney extends BaseMoney {
    public USDMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "USD";
    }
}