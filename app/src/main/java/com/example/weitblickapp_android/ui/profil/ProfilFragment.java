package com.example.weitblickapp_android.ui.profil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.Session.SessionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.Session.SessionManager;

public class ProfilFragment extends Fragment {

    private ProfilViewModel profilViewModel;
    private SessionManager session;
    LoginData loginData;

    private String donation = "10,40 €";
    private String password = "******";
    private String km = "20,4 km";
    private String email = "max.mustermann@hs-osnabrueck.de";
    ImageView imageProfil = null;
    public static final int IMAGE_GALLERY_REQUEST = 20;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profil, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        loginData = new LoginData(getActivity().getApplicationContext());
        email = session.getEmail();

        ImageButton changeProfile = root.findViewById(R.id.changeProfil);

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    startGallery();
                }
            }
        });


        final Button logOutButton = root.findViewById(R.id.log_out);
        final ImageButton changePasswordButton = root.findViewById(R.id.changePassword);

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
                    loginData.logout();

                    getActivity().onBackPressed();
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Erfolgreich ausgeloggt!",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            }
        });


        imageProfil = root.findViewById(R.id.imageProfil);
        imageProfil.setImageResource(R.drawable.ic_launcher_background);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        
        final TextView donationTextView = root.findViewById(R.id.donation);
        donationTextView.setText(this.donation);
        final TextView passwordTextView = root.findViewById(R.id.new_password);
        passwordTextView.setText(this.password);
        final TextView kmTextView = root.findViewById(R.id.km);
        kmTextView.setText(this.km);
        final TextView emailTextView = root.findViewById(R.id.old_password);
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

    private void startGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Uri imageUri = data.getData();

        InputStream inputStream;

        try {
            inputStream = getActivity().getContentResolver().openInputStream(imageUri);

            Bitmap image = BitmapFactory.decodeStream(inputStream);

            imageProfil.setImageBitmap(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}