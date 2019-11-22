package com.example.weitblickapp_android.ui.profil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProfilFragment extends Fragment {

    private ProfilViewModel profilViewModel;

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
        final ImageView imageView = root.findViewById(R.id.imageProfil);
        imageView.setImageResource(R.drawable.ic_launcher_background);
        final TextView donationTextView = root.findViewById(R.id.donation);
        donationTextView.setText(this.donation);
        final TextView passwordTextView = root.findViewById(R.id.password);
        passwordTextView.setText(this.password);
        final TextView kmTextView = root.findViewById(R.id.km);
        kmTextView.setText(this.km);
        final TextView emailTextView = root.findViewById(R.id.email);
        emailTextView.setText(this.email);
        ImageButton back = (ImageButton) root.findViewById(R.id.back);

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