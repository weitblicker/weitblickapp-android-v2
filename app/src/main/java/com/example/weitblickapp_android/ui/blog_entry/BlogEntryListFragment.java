package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogEntryListFragment extends ListFragment implements AbsListView.OnScrollListener {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");

    ArrayList<BlogEntryViewModel> blogEntries = new ArrayList<BlogEntryViewModel>();
    private BlogEntryListAdapter adapter;

    private String lastItemDate;
    private String lastItemDateCheck = "";
    private String url = "https://new.weitblicker.org/rest/blog?limit=5";



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBlogs(url);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Blog");
        
        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        adapter = new BlogEntryListAdapter(getActivity(), blogEntries, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        // Button detail = (Button) v.findViewById(R.id.news_more_btn);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentTransaction replace = ft.replace(R.id.fragment_container, new BlogDetailFragment(blogEntries.get(position)));
                ft.commit();
            }
        });
    }
    public void loadBlogs(String URL){

        // Talk to Rest API

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();
                //Parse the JSON response array by iterating over it
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;
                    JSONObject imageObject = null;
                    BlogEntryViewModel temp = null;
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;
                    try {
                        responseObject = response.getJSONObject(i);
                        Integer blogId = responseObject.getInt("id");
                        String title = responseObject.getString("title");
                        String text = responseObject.getString("text");
                        text = text.trim();
                        text = text.replaceAll("\n{2,}", "\n");
                        String published = responseObject.getString("published");
                        String teaser = responseObject.getString("teaser");
                        imageUrls = getImageUrls(text);
                        text = extractImageUrls(text);
                        //Get all imageUrls from Gallery
                        /*try {
                            galleryObject = responseObject.getJSONObject("gallery");

                            images = galleryObject.getJSONArray("images");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }

                        }catch(JSONException e){

                        }*/
                        //TODO: Check if picture exists
                        //Get Date of last Item loaded in List loading more news starting at that date
                        try {
                            Date ItemDate = formatterRead.parse(published);
                            lastItemDate = formatterWrite.format(ItemDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        temp = new BlogEntryViewModel(blogId, title, text, teaser,published, imageUrls);

                        blogEntries.add(temp);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    public ArrayList <String> getImageUrls(String text){
        //Find image-tag markdowns and extract
        ArrayList <String> imageUrls = new ArrayList<>();
        Matcher m = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\\"")
                .matcher(text);
        while (m.find()) {
            //Log.e("ImageUrl", m.group(2));
            imageUrls.add(m.group(2));
        }
        return imageUrls;
    }

    public String extractImageUrls(String text){
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)","");
        return text;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount > 0) {
            final int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem == totalItemCount && !lastItemDateCheck.equals(lastItemDate)) {
                Log.e("LASTITEMDAte", lastItemDate);
                Log.e("FIRSTITEMDAte", lastItemDateCheck);
                url = url.concat(("&end=" + lastItemDate));
                loadBlogs(url);
                lastItemDateCheck = lastItemDate;
            }
        }
    }
}
