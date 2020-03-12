package com.example.weitblickapp_android.ui.profil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.model.VolleyCallback;



public class ChangePasswordFragment extends Fragment {

    private LoginData loginData;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText newPasswordConfirmEditText;
    private Button changePasswordButton;
    private ImageButton back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);

        loginData = new LoginData(getActivity());

        changePasswordButton = root.findViewById(R.id.change_password_button);
        //oldPasswordEditText = root.findViewById(R.id.old_password);
        newPasswordEditText = root.findViewById(R.id.new_password);
        newPasswordConfirmEditText = root.findViewById(R.id.new_password_confirm);

        final ImageView loginImage = root.findViewById(R.id.loginPicture);

        loginImage.setImageResource(R.drawable.ic_wbcd_logo_standard_black_font);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginData.changePassword(newPasswordEditText.getText().toString(), newPasswordConfirmEditText.getText().toString(), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

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
