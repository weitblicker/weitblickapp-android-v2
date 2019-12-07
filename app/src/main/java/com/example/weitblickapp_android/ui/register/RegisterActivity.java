package com.example.weitblickapp_android.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.login.Login_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class RegisterActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        queue = Volley.newRequestQueue(this);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText password_confirmEditText = findViewById(R.id.password_confirm);

        final Button registerButton = findViewById(R.id.sign_up);
        final TextView signInText = findViewById(R.id.sign_in);
        final ImageButton infoButton = findViewById(R.id.infoButton);
        final ImageView loginImage = findViewById(R.id.loginPicture);

        //Bild initialisieren
        loginImage.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(usernameEditText.getText().toString(),emailEditText.getText().toString(), passwordEditText.getText().toString(), password_confirmEditText.getText().toString() );
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogin();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                new SimpleTooltip.Builder(RegisterActivity.this)
                        .anchorView(infoButton)
                        .text("Daten werden nicht weitergegeben bliblablub.")
                        .backgroundColor(Color.parseColor("#ff9900"))
                        .arrowColor(Color.parseColor("#ff9900"))
                        .gravity(Gravity.BOTTOM)
                        .animated(true)
                        .transparentOverlay(true)
                        .build()
                        .show();
                /*
                if(!show){

                    Toast.makeText(getApplicationContext(),"Die Info wird angezeigt.",Toast.LENGTH_SHORT).show();
                    show = true;
                }
                else{
                    Toast.makeText(getApplicationContext(),"Die Info ist zugeklappt.",Toast.LENGTH_SHORT).show();
                    show = false;
                }
                */

            }
        });
    }

    private void switchToMain(){
        Intent intent  = new Intent( this, MainActivity.class);
        startActivity(intent);
    }

    private void switchToLogin(){
        Intent intent  = new Intent( this, Login_Activity.class);
        startActivity(intent);
    }

    private boolean register(final String username, String email, String password, String password_confirm) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            String URL = "https://new.weitblicker.org/rest/auth/registration/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password1", password);
            jsonBody.put("password2", password_confirm);


            final String requestBody = jsonBody.toString();


            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody , new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(),"Erfolgreich registriert!" , Toast.LENGTH_SHORT).show();
                    Log.i("REGISTRATION SUCCESFUL", "VERY sucessful ---------------------------------------------------------------------------------");
                    //String jsonData = response.toString();

                    //JSONObject responseObject = response.;

                    //Parse the JSON response array by iterating over it
                    for (int i = 0; i < response.length(); i++) {
                        //JSONObject responseObject = n;

                        try {

                            if(response.has("username")) {
                                String usernameResp = response.getString("username");
                                Log.e("USERNAME ERROR", usernameResp);
                            }

                            switchToLogin();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    //for(NewsViewModel newsArticle:newsList){
                    //  Log.e("NewsArticle",newsArticle.getUrls());
                    //}





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY ERROR", error.toString());

                    String body;
                    //get status code here
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    //get response body and parse with appropriate encoding
                    if(error.networkResponse.data!=null) {
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
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

            requestQueue.add( objectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;

    }
}
