package com.example;

import java.util.List;
import com.example.Record.UserRecord;

public class User {
    private final String id;
    private String username;
    private String name;
    private String email;
    private List<Account> accounts;

    public User(String name, String email, String username) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public User(UserRecord ur) {
        this.id = ur.id;
        this.name = ur.name;
        this.email = ur.email;
        this.username = ur.username;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void createAccount(Money initialBalance) {
        Account account = new Account(this, initialBalance);
        this.accounts.add(account);
    }

}