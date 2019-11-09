package com.example.weitblickapp_android.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMain();
            }
        });
    }

    private void switchToMain(){
        Intent intent  =new Intent( this, MainActivity.class);
        startActivity(intent);
    }
}
