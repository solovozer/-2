package com.example;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private String username;
    private String name;
    private String email;
    private String password;
    private List<Account> accounts;

    public User(String name, String email, String username, String password) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.email = email; 
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public User(String id, String name, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
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
    
    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void createAccount(Money initialBalance) {
        Account account = new Account(java.util.UUID.randomUUID().toString(), this.id, initialBalance);
        this.accounts.add(account);
    }
}