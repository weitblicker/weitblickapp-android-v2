package com.example.weitblickapp_android.ui.profil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProfilFragment extends Fragment {

    private ProfilViewModel profilViewModel;
    private SessionManager session;


    private String donation = "10,40 €";
    private String password = "******";
    private String km = "20,4 km";
    private String email = "max.mustermann@hs-osnabrueck.de";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Dein Profil");
        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profil, container, false);

        session = new SessionManager(getActivity().getApplicationContext());

        final Button logOutButton = root.findViewById(R.id.log_out);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!session.isLoggedIn()){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Du bist schon ausgeloggt!",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
                else {
                    session.logoutUser();
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Erfolgreich ausgeloggt!",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            }
        });

        final ImageView imageView = root.findViewById(R.id.imageProfil);
        imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
        final TextView donationTextView = root.findViewById(R.id.donation);
        donationTextView.setText(this.donation);
        final TextView passwordTextView = root.findViewById(R.id.password);
        passwordTextView.setText(this.password);
        final TextView kmTextView = root.findViewById(R.id.km);
        kmTextView.setText(this.km);
        final TextView emailTextView = root.findViewById(R.id.email);
        emailTextView.setText(this.email);
        return root;
    }
}