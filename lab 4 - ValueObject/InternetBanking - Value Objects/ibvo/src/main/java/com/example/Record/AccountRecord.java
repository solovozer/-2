package com.example.Record;

import java.math.BigDecimal;

public class AccountRecord {
    public final String id;
    public final String userId;
    public final BigDecimal balanceAmount;
    public final String balanceCurrency;
    
    public AccountRecord(String id, String userId, BigDecimal balanceAmount, String balanceCurrency) {
        this.id = id;
        this.userId = userId;
        this.balanceAmount = balanceAmount;
        this.balanceCurrency = balanceCurrency;
    }
}