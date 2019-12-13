package com.example.weitblickapp_android.ui.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.RegistrationData;
import com.example.weitblickapp_android.data.model.VolleyCallback;
import com.example.weitblickapp_android.ui.login.Login_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class RegisterActivity extends AppCompatActivity {

    private RequestQueue queue;
    private RegistrationData registrationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        queue = Volley.newRequestQueue(this);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText password_confirmEditText = findViewById(R.id.password_confirm);
        //final CheckBox agb_checkbox = findViewById(R.id.agb_accept);
        final Button registerButton = findViewById(R.id.sign_up);
        final TextView signInText = findViewById(R.id.sign_in);
        final ImageButton infoButton = findViewById(R.id.infoButton);
        final ImageView loginImage = findViewById(R.id.loginPicture);

        registrationData = new RegistrationData(RegisterActivity.this);

        registerButton.setEnabled(false);
        //Bild initialisieren
        loginImage.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationData.register(usernameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), password_confirmEditText.getText().toString(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    }
                });
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

            }
        });

        CheckBox checkbox = (CheckBox)findViewById(R.id.checkBox1);
        TextView textView = (TextView)findViewById(R.id.textView2);
        TextView textView2 = (TextView)findViewById(R.id.textView3);

        checkbox.setText("");
        textView.setText("Ich akzeptiere die ");
        textView2.setText("AGB");
        //textView2.setTextColor(1);


        textView2.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                switchToMain();
            }
        });
        /*

        agb_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    registerButton.setEnabled(true);

                } else {
                    registerButton.setEnabled(false);
                }
            }
        });

        String checkBoxText = "Ich akzeptiere die <a href='login.mainactivity' > AGB   </a>";

        agb_checkbox.setText(Html.fromHtml(checkBoxText));
        agb_checkbox.setMovementMethod(LinkMovementMethod.getInstance());*/
    }

    private void switchToMain(){
        Intent intent  = new Intent( this, MainActivity.class);
        startActivity(intent);
    }

    private void switchToLogin(){
        /*Intent intent  = new Intent( this, Login_Activity.class);
        startActivity(intent);*/
        finish();
    }

}
