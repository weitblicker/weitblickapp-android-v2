package com.example.weitblickapp_android.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<JSONObject> users;
    public LiveData<JSONObject> getUsers() {
        if (users == null) {
            users = new MutableLiveData<JSONObject>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}