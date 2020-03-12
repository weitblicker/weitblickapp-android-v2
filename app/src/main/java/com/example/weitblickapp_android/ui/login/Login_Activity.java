package com.example.weitblickapp_android.ui.login;

import android.content.Intent;
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

/** Activity to handle all Authentication processes. The Activity gets called everytime the User wants to access a feature for which he needs to be logged in*/

public class Login_Activity extends AppCompatActivity {


    private SessionManager session;
    private LoginData loginData;
    private LoginPreferences loginPref;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView forgotPasswordTextView;
    private Button loginButton;
    private Button registerButton;
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

        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.change_password_button);

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

        //onClick listener for button that sends login form
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

        //button for user to switch to register activity
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
        //launches the forgot password fragment
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.INVISIBLE);
                registerButton.setVisibility(View.INVISIBLE);
                ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                //fragment.geView().setBackgroundColor(Color.WHITE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loginButton =findViewById(R.id.login);
        registerButton = findViewById(R.id.change_password_button);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        //Toast.makeText(getApplicationContext(), "Back Pressed", Toast.LENGTH_SHORT).show();
        if (count == 0) {
            super.onBackPressed();

        } else {
            getSupportFragmentManager().popBackStack();
            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
        }
    }

    private void switchToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }




}

