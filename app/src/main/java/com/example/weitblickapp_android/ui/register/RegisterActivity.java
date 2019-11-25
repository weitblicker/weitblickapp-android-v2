package com.example.weitblickapp_android.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        //final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.sign_up);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final ImageView loginImage = findViewById(R.id.loginPicture);
        final TextView signInText = findViewById(R.id.sign_in);

        loginImage.setImageResource(R.drawable.login_logo);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMain();
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogin();
            }
        });
    }

    private void switchToMain(){
        Intent intent  = new Intent( this, MainActivity.class);
        startActivity(intent);
    }

    private void switchToLogin(){
        Intent intent  = new Intent( this, LoginActivity.class);
        startActivity(intent);
    }
}
