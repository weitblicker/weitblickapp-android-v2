package com.example.weitblickapp_android.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

//interface to handle asynchronous VolleyRequests

public interface VolleyCallback {
    void onSuccess(String result);
    void onError(String result);
}
