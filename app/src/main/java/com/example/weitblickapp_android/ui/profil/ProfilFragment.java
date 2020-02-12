package com.example.weitblickapp_android.ui.profil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.VolleyCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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

        if(session.getUserName() == null){
            loginData.getUserDetails(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    email = session.getUserName();
                    //Log.i("getUserData SUCCESS",result);

                }

                @Override
                public void onError(String result) {
                    //Log.e("getUserData ERROR",result);

                }
            });
        }
        email = session.getUserName();

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
        //final Button getUserDataButton = root.findViewById(R.id.getUserDataButton);

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
                    loginData.logout(new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }

                        @Override
                        public void onError(String result) {
                            Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        String pictureDirectoryPath = "/media/external/images/media/16395";

        Uri data = Uri.parse(pictureDirectoryPath);

        /*
        getUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!session.isLoggedIn()){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Du bist schon ausgeloggt!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    loginData.getUserDetails( new VolleyCallback() {
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
            }
        });
        */
        
        imageProfil = root.findViewById(R.id.imageProfil);
        /*
        loginData.getProfileImageAsString(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("String result",result);
            }

            @Override
            public void onError(String result) {
                Log.e("Error Getting Image",result);
            }
        });

         */
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


/*
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, IMAGE_GALLERY_REQUEST);


 */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



/*
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                String image = getStringImage(lastBitmap);
                Log.d("image", image);
                //passing the image to volley

                loginData.setProfileImage(filePath, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



 */

        Uri imageUri = null;


        InputStream inputStream;

                if (resultCode == RESULT_OK) {
                    try {
                        imageUri = data.getData();
                        inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                        Bitmap image = BitmapFactory.decodeStream(inputStream);

                        imageProfil.setImageBitmap(image);


                        loginData.setProfileImage(imageUri, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                //Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String result) {
                                //Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        });



                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch(NullPointerException np){
                        np.printStackTrace();
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Bildauswahl abgebrochen.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Bildauswahl abgebrochen.", Toast.LENGTH_SHORT).show();
                }








    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

}

