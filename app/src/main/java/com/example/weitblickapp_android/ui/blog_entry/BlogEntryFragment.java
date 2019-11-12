package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class BlogEntryFragment extends Fragment {

    private BlogEntryViewModel blogEntryViewModel;
    private ArrayList<BlogEntryViewModel> blogEntries = new ArrayList<BlogEntryViewModel>();

    String[] title = {"Save the turtles", "Kinder in Not", "Baue eine Schule"};
    String[] location = {"Sydney", "Namibia", "Deutschland"};
    String[] shorttext = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};
    String[] date = {"06.11.2019" , "17.04.2018", "25.08.2009"};

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBlogEntries();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        blogEntryViewModel =
                ViewModelProviders.of(this).get(BlogEntryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blog, container, false);
        ListView listview = (ListView)root.findViewById(R.id.listView);

        CustomAdapter customAdapter = new CustomAdapter();
        listview.setAdapter(customAdapter);
        return root;
    }

    public void loadBlogEntries(){
        String URL = "https://new.weitblicker.org/rest/blog/?limit=3";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();
                //Parse the JSON response array by iterating over it
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = response.getJSONObject(i);
                        Integer blogId = responseObject.getInt("id");
                        String title = responseObject.getString("title");

                        String text = responseObject.getString("text");
                        text.trim();

                        BlogEntryViewModel temp = new BlogEntryViewModel(blogId, title, text);
                        blogEntries.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(BlogEntryViewModel blogEntry:blogEntries){
                    Log.e("BlogEntry",blogEntry.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Rest Response", error.toString());
            }
        }){
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

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.fragment_blog_list, null);

                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                TextView textView_title = (TextView) view.findViewById(R.id.title);
                TextView textView_location = (TextView) view.findViewById(R.id.location);
                TextView textView_shorttext = (TextView) view.findViewById(R.id.shorttext);
                TextView textView_date = (TextView) view.findViewById(R.id.date);

                imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
                textView_title.setText(title[position]);
                textView_location.setText(location[position]);
                textView_shorttext.setText(shorttext[position]);
                textView_date.setText(date[position]);
            }

            Button detail = (Button) view.findViewById(R.id.blog_more_btn);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new BlogDetailFragment(location[position], title[position], shorttext[position], date[position]));
                    ft.commit();
                }
            });

            return view;
        }
    }



}