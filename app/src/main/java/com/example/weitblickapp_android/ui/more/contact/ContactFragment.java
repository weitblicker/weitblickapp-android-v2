package com.example.weitblickapp_android.ui.more.contact;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.noties.markwon.Markwon;


public class ContactFragment extends Fragment {

    static final String urlWeitblick = "https://weitblicker.org";

    private TextView contactTextView;
    private TextView titleTextView;
    private ImageView contactImageView;
    private ImageButton back;

    private Context mContext;

    private RequestQueue requestQueue;

    String text;
    String title;
    String imageUrl;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestQueue = Volley.newRequestQueue(mContext);
        loadAGB();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);


        contactTextView = root.findViewById(R.id.text);
        titleTextView = root.findViewById(R.id.title);
        contactImageView = root.findViewById(R.id.contactImage);
        //agbTextView.setText(getAgbString());
        //agbTextView.setMovementMethod(new ScrollingMovementMethod());

        back = root.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });


        return root;
    }


    private void loadAGB() {


        String URL = "https://weitblicker.org/rest/info/contact";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Save Data into Model
                String jsonData = response.toString();

                try {
                    text = response.getString("text");
                    title = response.getString("title");
                    imageUrl = response.getString("image");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final Markwon markwon = Markwon.create(mContext);

                markwon.setMarkdown(contactTextView,text);

                titleTextView.setText(title);

                Picasso.get().load(urlWeitblick.concat(imageUrl)).
                        placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                        .error(R.drawable.ic_wbcd_logo_standard_svg2).into(contactImageView);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Rest Response", error.toString());
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


}