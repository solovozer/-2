package com.example.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.example.Money;

//SUPPORTED_CURRENCIES = Arrays.asList("AUD", "CAD", "CNY", "EUR", "JPY", "RUB", "USD", "VND")
public class ExchangeService {
    private static final Money[] EXCHANGE_RATES = new Money[] {
        new Money(new BigDecimal("1.30"), "AUD"),
        new Money(new BigDecimal("1.25"), "CAD"),
        new Money(new BigDecimal("6.50"), "CNY"),
        new Money(new BigDecimal("0.85"), "EUR"),
        new Money(new BigDecimal("162.1"), "JPY"),
        new Money(new BigDecimal("75.00"), "RUB"),
        new Money(new BigDecimal("1.00"), "USD"),
        new Money(new BigDecimal("26025.00"), "VND")
    };

    public BigDecimal getRate(String currency) {
        for (Money rate : EXCHANGE_RATES) {
            if (rate.getCurrency().equalsIgnoreCase(currency)) {
                return rate.getAmount();
            }
        }
        throw new IllegalArgumentException("Unsupported currency: " + currency);
    }

    public Money convert(Money amount, String targetCurrency) {
        BigDecimal rate = getRate(targetCurrency);
        BigDecimal convertedAmount = amount.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_DOWN);
        return new Money(convertedAmount, targetCurrency);
    }
}
