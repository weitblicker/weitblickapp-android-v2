package com.example.weitblickapp_android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private String email;
    private String key;
    private String first_name;
    private String last_name;

    public LoggedInUser(String username, String email, String key) {
        this.username = username;
        this.email = email;
        this.key = key;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LoggedInUser(){

    }



    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {return key;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
