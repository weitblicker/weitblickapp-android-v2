package com.example.weitblickapp_android.data;

import com.example.weitblickapp_android.data.model.User;

import java.util.Vector;

public class RegistrationDataSource {

    private Vector<User> users;

    RegistrationDataSource(){

        this.users = new Vector<User>();
        users.add(new User("admin@admin.com" , "admin", 1, "123456"));

    }
    private boolean doesUserExist(User u){
        if(users.contains(u)){
            return true;
        }
        return false;
    }

    public User getUserById(int id){
        return new User("admin@admin.com" , "admin", 1, "123456");
    }
}
