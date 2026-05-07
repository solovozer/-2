package com.example.Monies;

import java.math.BigDecimal;

public class VNDMoney extends BaseMoney {
    public VNDMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "VND";
    }
}