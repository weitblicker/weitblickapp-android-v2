package com.example.weitblickapp_android.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginPreferences;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.LoggedInUser;
import com.example.weitblickapp_android.ui.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {


    private LoggedInUser user;

    private SessionManager session;
    private LoginPreferences loginPref;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private boolean saveLogin = false;
    private boolean debug = true;
    private String key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        loginPref = new LoginPreferences(getApplicationContext());

        user = null;

        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.sign_up);

        final ImageView loginImage = findViewById(R.id.loginPicture);
        final CheckBox checkBox = findViewById(R.id.save_login);

        loginImage.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);


        //lade Login Preferences
        if (loginPref.isLoginSaved()) {
            if (debug)
                Toast.makeText(getApplicationContext(), "Login is Saved! (:", Toast.LENGTH_SHORT).show();
            checkBox.setChecked(true);
            usernameEditText.setText(loginPref.getUserName());
            passwordEditText.setText(loginPref.getPassword());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegister();
            }
        });


        //handle save-login checkbox
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    saveLogin = true;
                } else {
                    saveLogin = false;
                    loginPref.clearLoginPreferences();
                    loginPref.setTo_NOT_Save();
                }
            }
        });


    }

    private void updateUiWithUser() {

        if (user != null) {
            if (debug)
                Toast.makeText(getApplicationContext(), "Willkommen " + user.getUsername(), Toast.LENGTH_SHORT).show();

            // Intent intent  =new Intent( this, MapsActivity.class);

            session.createLoginSession(user.getUsername(), user.getEmail(), user.getKey());

            if (saveLogin) {
                loginPref.saveLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }

            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            }
            finish();
            // startActivity(intent);
        } else {
            Log.e("INIT_ERROR: ", "user nich initalisiert!");
        }
    }

    public boolean login(String email, String password) {

        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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

                                if (usernameEditText.getText().toString().equals("admin") && passwordEditText.getText().toString().equals("123456")) {
                                    user = new LoggedInUser(java.util.UUID.randomUUID().toString(), usernameEditText.getText().toString(), "123456789");

                                } else if (key != null) {
                                    user = new LoggedInUser("username_placeholder", usernameEditText.getText().toString(), key);
                                }
                                updateUiWithUser();
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
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;


        } catch (Exception e) {
            Log.e("LOGIN_EXCEPTION: ", e.toString());
            return false;
        }
    }

    private void switchToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    void logout(){

    }
}

