package com.example.weitblickapp_android.data;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.LoggedInUser;
import com.example.weitblickapp_android.data.model.VolleyCallback;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Class that handles all Registration network-funcitonality*/

public class RegistrationData {

    private Context app_context;
    private LoggedInUser user;

    public RegistrationData (Context context){
        app_context = context;
    }

    /** method to send VolleyRequest to request the registration of the user on the server*/
    public void register(final String username, final String email, final String password, final String password_confirm, final VolleyCallback callback) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(app_context);

            String URL = "https://weitblicker.org/rest/auth/registration/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password1", password);
            jsonBody.put("password2", password_confirm);


            final String requestBody = jsonBody.toString();


            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody , new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                   callback.onSuccess("Registrierung erfolgreich.");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY ERROR REGISTER", error.toString());

                    String body;
                    //get status code here
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    if(error.networkResponse.data!=null) {
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                            Log.e("Statuscode:", body);

                            String result = "";

                            JsonParser parser = new JsonParser();
                            JsonObject jObject = parser.parse(body).getAsJsonObject();

                            Map<String, Object> attributes = new HashMap<String, Object>();

                            Set<Map.Entry<String, JsonElement>> entrySet = jObject.entrySet();

                            for(Map.Entry<String,JsonElement> entry : entrySet){
                                result += "\n" +jObject.get(entry.getKey()).getAsString();
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
                    Log.e("REGISTRATION-HEADER = ", headers.toString());
                    return headers;
                }
            };

            requestQueue.add( objectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
