package com.example.weitblickapp_android.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.model.VolleyCallback;

public class ForgotPasswordFragment extends Fragment {

        private TextView emailTextView;
        private ImageButton back;
        private ImageView logoImageView;
        private Button resetPasswordButton;
        private EditText emailEditText;

        private LoginData loginData;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_forgot_password, container, false);

            logoImageView = root.findViewById(R.id.loginPicture);
            logoImageView.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);

            loginData = new LoginData(getActivity());
            emailEditText = root.findViewById(R.id.emailEditText);
            resetPasswordButton = root.findViewById(R.id.resetPasswordButton);
            back = (ImageButton) root.findViewById(R.id.back);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                        getFragmentManager().popBackStack();
                    }
                }
            });

            resetPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginData.resetPassword(emailEditText.getText().toString(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(String result) {
                            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });


            return root;
        }




    }

