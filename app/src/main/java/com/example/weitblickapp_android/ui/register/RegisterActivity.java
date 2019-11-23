package com.example.weitblickapp_android.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.login.LoginActivity;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class RegisterActivity extends AppCompatActivity {

    private boolean show =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button registerButton = findViewById(R.id.sign_up);
        final TextView signInText = findViewById(R.id.sign_in);
        final ImageButton infoButton = findViewById(R.id.infoButton);
        final ImageView loginImage = findViewById(R.id.loginPicture);

        //Bild initialisieren
        loginImage.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);



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
        Intent intent  = new Intent( this, LoginActivity.class);
        startActivity(intent);
    }


}
