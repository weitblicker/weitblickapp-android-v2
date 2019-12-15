package com.example.weitblickapp_android.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.LoginPreferences;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.VolleyCallback;
import com.example.weitblickapp_android.ui.register.RegisterActivity;
import com.example.weitblickapp_android.ui.register.RegisterFragment;

public class Login_Activity extends AppCompatActivity {


    private SessionManager session;
    private LoginData loginData;
    private LoginPreferences loginPref;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView forgotPasswordTextView;
    private boolean saveLogin = false;
    private boolean debug = false;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        loginPref = new LoginPreferences(getApplicationContext());
        loginData = new LoginData(Login_Activity.this);



        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.new_password);

        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.change_password_button);

        final ImageButton back = findViewById(R.id.back);
        final ImageView loginImage = findViewById(R.id.loginPicture);
        final CheckBox checkBox = findViewById(R.id.save_login);
        forgotPasswordTextView = findViewById(R.id.text_forgot_password);

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

                    loginData.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), saveLogin, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(String result) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        }
                    });

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                //fragment.geView().setBackgroundColor(Color.WHITE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }


    private void switchToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }




}

