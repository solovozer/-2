package com.example.Record;

public class UserRecord {
    public final String id;
    public final String name;
    public final String email;
    public final String username;
    public final String password;

    public UserRecord(String id, String name, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
