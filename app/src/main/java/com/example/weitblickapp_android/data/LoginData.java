package com.example.weitblickapp_android.data;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.se.omapi.Session;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.LoggedInUser;
import com.example.weitblickapp_android.data.model.MultipartRequest;
import com.example.weitblickapp_android.data.model.MultipartUtility;
import com.example.weitblickapp_android.data.model.VolleyCallback;
import com.example.weitblickapp_android.ui.login.Login_Activity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class LoginData{

    private Context app_context;
    private String key;
    private LoggedInUser user;
    private SessionManager sessionManager;
    private LoginPreferences loginPref;
    private RequestQueue requestQueue;

    public LoginData (Context context){
        app_context = context;
        this.sessionManager = new SessionManager( context);
        loginPref = new LoginPreferences(context);
        requestQueue = Volley.newRequestQueue(context);
        user = new LoggedInUser();
    }

    public JSONObject login(final String email, final String password, final boolean isLoginSaved, final VolleyCallback callback) {

        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://weitblicker.org/rest/auth/login/";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", "");
                jsonBody.put("email", email);
                jsonBody.put("password", password);

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.has("key")) {
                                key = response.getString("key");
                                Log.e("LOGIN", "login in Data-Source aufgerufen");


                                if (key != null) {

                                    if(user == null) user = new LoggedInUser();

                                    user.setEmail(email);
                                    user.setKey(key);

                                    updateUiWithUser(email, password, isLoginSaved);
                                    callback.onSuccess("Anmeldung erfolgreich.");
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMessage = "";

                        if (error instanceof NetworkError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            errorMessage = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Connection TimeOut! Please check your internet connection.";
                        }
                        else{

                            String body;
                            //get status code here
                            if(error != null){
                                String statusCode = String.valueOf(error.networkResponse.statusCode);
                            }

                            //get response body and parse with appropriate encoding
                            if (error.networkResponse.data != null) {
                                try {
                                    //create errorJSON from VolleyError
                                    body = new String(error.networkResponse.data, "UTF-8");

                                    String result = "";

                                    JsonParser parser = new JsonParser();
                                    JsonObject jObject = parser.parse(body).getAsJsonObject();

                                    Map<String, Object> attributes = new HashMap<String, Object>();

                                    Set<Map.Entry<String, JsonElement>> entrySet = jObject.entrySet();

                                    for(Map.Entry<String,JsonElement> entry : entrySet){
                                        result += jObject.get(entry.getKey()).getAsString();
                                    }

                                    Log.e("Statuscode:", body);
                                    errorMessage = result;


                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        callback.onError(errorMessage);

                        Log.e("VOLLEY ERROR LOGIN", error.toString() + errorMessage);
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
        return new JSONObject();
    }

    public void logout(VolleyCallback callback){

        Log.e("LOGOUT", "aufgerufen");

        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://weitblicker.org/rest/auth/logout/";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("key", sessionManager.getKey());

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                            if (response.has("detail")) {
                                sessionManager.logoutUser();
                                callback.onSuccess("Erfolgreich ausgeloggt.");
                            }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMessage = "";

                        if (error instanceof NetworkError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            errorMessage = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Connection TimeOut! Please check your internet connection.";
                        }
                        else {

                            String body = "";
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
                            errorMessage = body;
                        }
                        callback.onError(errorMessage);
                        Log.e("VOLLEY ERROR LOGOUT", error.toString() + errorMessage);
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", sessionManager.getKey());
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

    public void changePassword(final String newPassword,final String newPasswordConfirm, final VolleyCallback callback){

        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://weitblicker.org/rest/auth/password/change/";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("new_password1",newPassword);
                jsonBody.put("new_password2", newPasswordConfirm);

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Change Password Success", response.toString());
                        callback.onSuccess("Passwort erfolgreich geändert.");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "";

                        if (error instanceof NetworkError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            errorMessage = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Connection TimeOut! Please check your internet connection.";
                        }
                        else {
                            Log.e("VOLLEY ERROR LOGOUT", error.toString());
                            String body;
                            //get status code here
                            String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            if (error.networkResponse.data != null) {
                                try {
                                    //create errorJSON from VolleyError
                                    body = new String(error.networkResponse.data, "UTF-8");

                                    String result = "";

                                    JsonParser parser = new JsonParser();
                                    JsonObject jObject = parser.parse(body).getAsJsonObject();

                                    Map<String, Object> attributes = new HashMap<String, Object>();

                                    Set<Map.Entry<String, JsonElement>> entrySet = jObject.entrySet();

                                    for(Map.Entry<String,JsonElement> entry : entrySet){

                                        JsonArray j = jObject.get(entry.getKey()).getAsJsonArray();
                                        for ( int i = 0; i<j.size(); i++){
                                            result += j.get(i).getAsString() + '\n';
                                        }
                                    }

                                    Log.e("Statuscode:", body);
                                    errorMessage = result;


                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        callback.onError(errorMessage);
                        Log.e("VOLLEY ERROR CHANGE_P", error.toString() + errorMessage);
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
                        headers.put("Authorization","Token " + sessionManager.getKey());
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

        sessionManager.createLoginSession(user.getUsername(), user.getEmail(), user.getKey());

        getUserDetails(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {


                    if (isLoginSaved) {
                        loginPref.saveLogin(email, password);
                    }

                    Toast.makeText(app_context, result, Toast.LENGTH_SHORT).show();
                    Log.e("INIT_SUCCESS: ", "getUserDetail erfolgreich.");
                    ((Activity)app_context).finish();
                }

                @Override
                public void onError(String result) {
                    Toast.makeText(app_context, result, Toast.LENGTH_SHORT).show();
                    Log.e("INIT_ERROR: ", "getUserDetail fehlgeschlagen.");
                }
            });




    }

    public void resetPassword(final String email, VolleyCallback callback){
        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://weitblicker.org/rest/auth/password/reset";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email);

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        callback.onSuccess("Passwort erfolgreich geändert.");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY ERROR RESET", error.toString());
                        String body;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                //create errorJSON from VolleyError
                                body = new String(error.networkResponse.data, "UTF-8");

                                String result = "";

                                JsonParser parser = new JsonParser();
                                JsonObject jObject = parser.parse(body).getAsJsonObject();

                                Map<String, Object> attributes = new HashMap<String, Object>();

                                Set<Map.Entry<String, JsonElement>> entrySet = jObject.entrySet();

                                for(Map.Entry<String,JsonElement> entry : entrySet){
                                    result += jObject.get(entry.getKey()).getAsString();
                                }

                                Log.e("Statuscode:", body);
                                callback.onError(result);

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

    public void getUserDetails(VolleyCallback callback){
        try {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(app_context);

                String URL = "https://weitblicker.org/rest/auth/user";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", sessionManager.getUserName());
                jsonBody.put("first_name", "");
                jsonBody.put("last_name", "");

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("LOGIN", response.toString());

                        String isSessionCompleteDebug = "";
                        try {
                            if(response.has("username")){
                                String resp_username = response.get("username").toString();
                                isSessionCompleteDebug += resp_username;
                                if(resp_username != null){
                                    sessionManager.setUserName(resp_username);
                                }
                            }
                            if(response.has("firstname")){
                                String resp_firstname = response.get("firstname").toString();
                                isSessionCompleteDebug += resp_firstname;
                                if(resp_firstname != null){
                                    sessionManager.setFirstName(resp_firstname);
                                }
                            }
                            if(response.has("lastname")){
                                String resp_lastname = response.get("lastname").toString();
                                isSessionCompleteDebug += resp_lastname;
                                if(resp_lastname != null){
                                    sessionManager.setLastName(resp_lastname);
                                }
                            }
                            if(response.has("image")){
                                String resp_image = response.get("image").toString();
                                isSessionCompleteDebug += resp_image;
                                if(resp_image != null){
                                    sessionManager.setImageURL(resp_image);
                                }
                            }

                            //Toast.makeText(app_context, isSessionCompleteDebug , Toast.LENGTH_SHORT).show();
                            Log.e("sessionManagerDebug", isSessionCompleteDebug);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callback.onSuccess("User Details erfoglreich geladen!");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "";

                        /*
                        if (error instanceof NetworkError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            errorMessage = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            errorMessage = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            errorMessage = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            errorMessage = "Connection TimeOut! Please check your internet connection.";
                        }
                        */

                        if(true) {

                        Log.e("VOLLEY ERROR GET_UD", error.toString());
                        String body;
                        //get status code here
                        if(error != null){
                            String statusCode = String.valueOf(error.networkResponse.statusCode);
                        }
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                //create errorJSON from VolleyError
                                body = new String(error.networkResponse.data, "UTF-8");

                                String result = "";

                                JsonParser parser = new JsonParser();
                                JsonObject jObject = parser.parse(body).getAsJsonObject();

                                Map<String, Object> attributes = new HashMap<String, Object>();

                                Set<Map.Entry<String, JsonElement>> entrySet = jObject.entrySet();

                                for(Map.Entry<String,JsonElement> entry : entrySet){
                                    result += jObject.get(entry.getKey()).getAsString();
                                }

                                Log.e("Statuscode:", body);
                                callback.onError(result);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                        if(errorMessage != null){
                            Toast.makeText(app_context, errorMessage , Toast.LENGTH_SHORT).show();
                        }
                        Log.e("VOLLEY ERROR GET_UD", error.toString() + errorMessage);
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Token " + sessionManager.getKey());
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

    public void setProfileImage(Uri imageURI, VolleyCallback callback){

        if(sessionManager.getUserName() == null){
            getUserDetails(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onError(String result) {
                    Toast.makeText(app_context, result , Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            final String url = "https://weitblicker.org/rest/auth/user/";

            String charset = "UTF-8";
            String requestURL = "YOUR_URL";

            String path  = getRealPathFromURI(app_context, imageURI);

            MultipartUtility multipart = null;
            try {
                multipart = new MultipartUtility(url,sessionManager.getKey(), charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO: add Support for last/firstname when they are actually used
            //TODO: save new ImageURL in SessionManager
            //String username = sessionManager.getUserName();
            multipart.addFormField("username", sessionManager.getUserName());
            multipart.addFormField("first_name", sessionManager.getFirstName());
            multipart.addFormField("last_name", sessionManager.getLastName() );


            //possible alternative for checking if first/lastname exist
            /*
            if( (firstname = sessionManager.getFirstName())!= null){
                multipart.addFormField("first_name", firstname);
            }
            else{
                multipart.addFormField("first_name", "firstname");
            }
            */


            try {
                multipart.addFilePart("image", new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = null;
            try {
                response = multipart.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if  (response != null){
                Log.e("Change Image Response", response);
            }

        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}

