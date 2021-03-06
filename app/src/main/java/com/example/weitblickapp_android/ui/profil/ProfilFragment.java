package com.example.weitblickapp_android.ui.profil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.LoginData;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.data.model.VolleyCallback;
import com.example.weitblickapp_android.ui.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ProfilFragment extends Fragment {

    private final String baseUrl = "https://weitblicker.org/";

    private ProfilViewModel profilViewModel;
    private SessionManager session;
    LoginData loginData;

    private String password = "******";
    private String email;

    private ImageView imageProfil;
    private TextView donationTextView;
    private TextView kmTextView;
    private TextView username;

    private Context mContext;
    private RequestQueue requestQueue;

    private String token;

    public static final int IMAGE_GALLERY_REQUEST = 20;

    private String userName;
    private String imageUrl;
    private String kmTotal;
    private String donationTotal;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestQueue = Volley.newRequestQueue(mContext);
        session = new SessionManager(mContext);
        this.token = session.getKey();
        this.userName = session.getUserName();
        this.imageUrl = baseUrl.concat(session.getImageURL());
        this.email = session.getEmail();
        loadUserData();
    }

    private void loadUserData() {

        String url = "https://weitblicker.org/rest/cycle/ranking/";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Save Data into Model
                JSONArray userField = null;
                JSONArray bestField = null;

                try {
                    bestField = response.getJSONArray("best_field");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Parse the JSON response array by iterating over it
                //load User
                JSONObject userObject = null;
                for (int x = 0; x < bestField.length(); x++) {
                    try {
                        userObject = bestField.getJSONObject(x);


                        if (userObject.getString("username").equals(userName)) {
                            double distance = userObject.getDouble("km");
                            double donation = userObject.getDouble("euro");
                            String url = userObject.getString("image");

                            kmTotal = String.format("%.2f", distance).concat(" km");
                            donationTotal = String.format("%.2f", donation).concat(" €");

                            kmTextView.setText(kmTotal);
                            donationTextView.setText(donationTotal);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Ranking ErrorResponse", error.toString());
            }
        }) {
            //Override getHeaders() to set Credentials for REST-Authentication
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "surfer:hangloose";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profil, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        loginData = new LoginData(getActivity().getApplicationContext());

        ImageButton changeProfile = root.findViewById(R.id.changeProfil);

        //set onClickListener changeProfil
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get permission
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    //openGallery
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
                if (!session.isLoggedIn()) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Du bist schon ausgeloggt!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
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

        username = (TextView) root.findViewById(R.id.username);
        username.setText(session.getUserName());


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

        imageProfil = root.findViewById(R.id.imageProfil);

        Log.e("imageUrlFromSession", imageUrl + "!");

        if (imageUrl != null) {
            Picasso.get().load(imageUrl).transform(new CircleTransform()).fit().centerCrop()
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageProfil);
        } else {
            Picasso.get().load(R.mipmap.ic_launcher_foreground).transform(new CircleTransform()).fit().centerCrop()
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageProfil);
        }

        donationTextView = root.findViewById(R.id.donation);


        final TextView passwordTextView = root.findViewById(R.id.new_password);
        passwordTextView.setText(this.password);

        kmTextView = root.findViewById(R.id.km);

        final TextView email = root.findViewById(R.id.email);
        email.setText(this.email);

        return root;
    }

    private void startGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

                Picasso.get().load(imageUri).transform(new CircleTransform()).fit().centerCrop().
                        placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                        .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageProfil);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException np) {
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

    private String getToken(){
        return this.token;
    }
}





