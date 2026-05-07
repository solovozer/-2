package com.example.Monies;

import java.math.BigDecimal;

public class CNYMoney extends BaseMoney{
    public CNYMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "CNY";
    }
}
