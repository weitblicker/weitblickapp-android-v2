package com.example.weitblickapp_android.ui.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.RegistrationData;
import com.example.weitblickapp_android.data.model.VolleyCallback;

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
        final EditText emailEditText = findViewById(R.id.old_password);
        final EditText passwordEditText = findViewById(R.id.new_password);
        final EditText password_confirmEditText = findViewById(R.id.new_password_confirm);
        //final CheckBox agb_checkbox = findViewById(R.id.agb_accept);
        final Button registerButton = findViewById(R.id.change_password_button);
        final TextView signInText = findViewById(R.id.sign_in);
        final ImageButton infoButton = findViewById(R.id.infoButton);
        final ImageButton back = findViewById(R.id.back);
        final ImageView loginImage = findViewById(R.id.loginPicture);

        final CheckBox checkbox = (CheckBox)findViewById(R.id.checkBox1);
        final TextView acceptTextView = (TextView)findViewById(R.id.textView2);
        final TextView agbTextView = (TextView)findViewById(R.id.textView3);


        checkbox.setText("");
        acceptTextView.setText("Ich akzeptiere die ");
        agbTextView.setText("AGB");

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
                //TODO: Text für Datenweitergabe vervollständigen
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

        agbTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    RegisterFragment fragment = new RegisterFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

            }
        });


        //TODO: enhance visibility of disabled button
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    registerButton.setEnabled(true);

                } else {
                    registerButton.setEnabled(false);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

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
