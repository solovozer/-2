package com.example.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.example.Money;
import com.example.Account;
import com.example.Repository;
import com.example.DatabaseConfig;

//SUPPORTED_CURRENCIES = Arrays.asList("AUD", "CAD", "CNY", "EUR", "JPY", "RUB", "USD", "VND")
public class ExchangeService {
    private final Repository AR;

    public ExchangeService(Repository repo) {
        this.AR = repo;
    }

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
        BigDecimal rate = getRate(targetCurrency).divide(getRate(amount.getCurrency()), 10, RoundingMode.HALF_DOWN);
        BigDecimal convertedAmount = amount.getAmount().multiply(rate).setScale(5, RoundingMode.HALF_DOWN);
        return new Money(convertedAmount, targetCurrency);
    }

    public void exchange(String userId, Money originalAmount, String targetCurrency) {
        Account from = AR.GetCurrencyAccount(userId, originalAmount.getCurrency());
        Account to = AR.GetCurrencyAccount(userId, targetCurrency);

        Money converedAmount = convert(originalAmount, targetCurrency);

        if (from == null) {
            throw new RuntimeException(originalAmount.getCurrency() + " account not found");
        }

        if (to == null) {
            throw new RuntimeException(targetCurrency + " account not found");
        }

         try {
            from.withdraw(originalAmount);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        to.deposit(converedAmount);

        AR.updateBalance(from);
        AR.updateBalance(to);
        
        // log the currency conversion for this user
        DatabaseConfig db = new DatabaseConfig();
        db.saveConversion(userId, originalAmount, originalAmount.getCurrency(), targetCurrency);
    }
}
