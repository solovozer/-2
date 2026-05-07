package com.example;

import java.math.BigDecimal;

import com.example.Monies.*;

public final class MoneyFactory {
    private MoneyFactory() {
    }

    public static BaseMoney create(BigDecimal amount, String currency) {
        if (currency == null || !CurrencyConstants.SUPPORTED_CURRENCIES.contains(currency)) {
            throw new IllegalArgumentException("Unsupported or null currency");
        }

        switch (currency) {
            case "USD": return new USDMoney(amount);
            case "EUR": return new EURMoney(amount);
            case "AUD": return new AUDMoney(amount);
            case "CAD": return new CADMoney(amount);
            case "CNY": return new CNYMoney(amount);
            case "JPY": return new JPYMoney(amount);
            case "RUB": return new RUBMoney(amount);
            case "VND": return new VNDMoney(amount);
            default: throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }
}
