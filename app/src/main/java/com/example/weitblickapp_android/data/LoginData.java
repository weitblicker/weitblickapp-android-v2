package com.example.weitblickapp_android.data;

import android.app.Activity;
import android.content.Context;
import android.se.omapi.Session;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.LoggedInUser;
import com.example.weitblickapp_android.ui.login.Login_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginData{

    private Context app_context;
    private String key;
    private LoggedInUser user;
    private SessionManager sessionManager;
    private LoginPreferences loginPref;

    public LoginData (Context context){
        app_context = context;
        this.sessionManager = new SessionManager( context);
        loginPref = new LoginPreferences(context);

    }



    public void login(final String email, final String password, final boolean isLoginSaved) {

        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://new.weitblicker.org/rest/auth/login/";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", "");
                jsonBody.put("email", email);
                jsonBody.put("password", password);

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(getApplicationContext(),"Anmeldung erfolgreich!" , Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN ONRESPONSE", "VERY sucessful ---------------------------------------------------------------------------------");

                        try {
                            if (response.has("key")) {
                                key = response.getString("key");
                                Log.e("LOGIN", "login in Data-Source aufgerufen");


                                if (key != null) {
                                    user = new LoggedInUser("username_placeholder", email, key);
                                    updateUiWithUser(email, password, isLoginSaved);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY ERROR", error.toString());

                        String body;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                                Log.e("Statuscode:", body);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = "surfer:hangloose";
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

                requestQueue.add(objectRequest);


            } catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (Exception e) {
            Log.e("LOGIN_EXCEPTION: ", e.toString());

        }
    }


    public void logout(){

        Log.e("LOGOUT", "aufgerufen");

        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://new.weitblicker.org/rest/auth/logout/";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("key", sessionManager.getKey());

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(getApplicationContext(),"Anmeldung erfolgreich!" , Toast.LENGTH_SHORT).show();
                        Log.e("LOGOUT ONRESPONSE", "VERY sucessful ---------------------------------------------------------------------------------");


                            if (response.has("detail")) {

                                Toast.makeText(app_context, "Erfolgreich ausgeloggt.", Toast.LENGTH_SHORT).show();
                                Log.e("LOGOUT", "onResponse()in logout in Login Data aufgerufen");

                                sessionManager.logoutUser();


                            }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY ERROR", error.toString());

                        String body;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                                Log.e("Statuscode:", body);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = "surfer:hangloose";
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

                requestQueue.add(objectRequest);


            } catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (Exception e) {
            Log.e("LOGIN_EXCEPTION: ", e.toString());

        }
    }

    private void updateUiWithUser(final String email, final String password, final boolean isLoginSaved) {

        if (user != null) {

            Toast.makeText(app_context, "Willkommen " + user.getUsername(), Toast.LENGTH_SHORT).show();

            sessionManager.createLoginSession(user.getUsername(), user.getEmail(), user.getKey());

            if (isLoginSaved) {
                loginPref.saveLogin(email, password);
            }

            ((Activity)app_context).finish();

        } else {
            Log.e("INIT_ERROR: ", "user nicht initalisiert!");
        }
    }
}
