package com.example.weitblickapp_android.data.model;

public class User {
    private String email;
    private String username;
    private int userId;
    private String password;

    public User(String email, String username, int userId, String password) {

        this.email = email;
        this.username = username;
        this.userId = userId;
        this.password = password;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
