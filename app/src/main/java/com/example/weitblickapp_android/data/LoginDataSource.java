package com.example.weitblickapp_android.data;

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
import com.example.weitblickapp_android.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String email, String password) {

        try {
            // handle loggedInUser authentication
            try {
                //RequestQueue requestQueue = Volley.newRequestQueue(this);

                String URL = "https://new.weitblicker.org/rest/auth/registration/";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", "");
                jsonBody.put("email", email);
                jsonBody.put("password1", password);

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(getApplicationContext(),"Erfolgreich registriert!" , Toast.LENGTH_SHORT).show();
                        Log.e("REGISTRATION SUCCESFUL", "VERY sucessful ---------------------------------------------------------------------------------");


                        try {
                            if (response.has("key")) {
                                String key = response.getString("username");

                            }

                            /*


                            NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser,date, imageUrls);
                            newsList.add(temp);
                            adapter.notifyDataSetChanged();

                             */
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


                /*
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
                */

                };

                //requestQueue.add( objectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (email.equals("admin") && password.equals("123456")) {
                LoggedInUser user =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                email);


                return new Result.Success<>(user);
            } else {
                return new Result.Error(new IOException("Error logging in"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


}
