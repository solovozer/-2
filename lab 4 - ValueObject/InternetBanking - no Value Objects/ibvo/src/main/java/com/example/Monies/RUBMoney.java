package com.example.Monies;

import java.math.BigDecimal;

public class RUBMoney extends BaseMoney {
    public RUBMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "RUB";
    }
}
