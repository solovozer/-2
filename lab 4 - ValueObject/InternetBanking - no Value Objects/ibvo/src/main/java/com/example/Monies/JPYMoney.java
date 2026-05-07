package com.example.Monies;

import java.math.BigDecimal;

public class JPYMoney extends BaseMoney {
    public JPYMoney(BigDecimal amount) {
        super(amount);
    }

    @Override
    public String getCurrency() {
        return "JPY";
    }
}
