package com.example.weitblickapp_android.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;

public class ForgotPasswordFragment extends Fragment {

        private TextView emailTextView;
        private ImageButton back;
        private ImageView logoImageView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_forgot_password, container, false);

            logoImageView = getActivity().findViewById(R.id.loginPicture);
            logoImageView.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);

            //agbTextView.setMovementMethod(new ScrollingMovementMethod());

            back = (ImageButton) root.findViewById(R.id.back);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                        getFragmentManager().popBackStack();
                    }
                }
            });


            return root;
        }




    }

