package com.example.weitblickapp_android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private String email;
    private String key;

    public LoggedInUser(String username, String email, String key) {
        this.username = username;
        this.email = email;
        this.key = key;
    }



    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {return key;}
}
