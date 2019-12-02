package com.example.weitblickapp_android.ui.news;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

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

public class NewsListFragment extends ListFragment{

    ArrayList<NewsViewModel> newsList = new ArrayList<NewsViewModel>();
    private NewsListAdapter adapter;
    private ViewPager sliderPager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNews();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        adapter = new NewsListAdapter(getActivity(), newsList, getFragmentManager());
        this.setListAdapter(adapter);

        View detailsView = inflater.inflate(R.layout.fragment_news_detail, container, false);
/*
        ViewPager viewPager = detailsView.findViewById(R.id.view_pager);
        NewsPagerAdapter adapter = new NewsPagerAdapter(this, newsList);
        viewPager.setAdapter(adapter);

 */

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void loadNews(){

        // Talk to Rest API

        String URL = "https://new.weitblicker.org/rest/news/?limit=4";

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
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;

                    try {
                        responseObject = response.getJSONObject(i);
                        Integer newsId = responseObject.getInt("id");
                        String title = responseObject.getString("title");
                        String text = responseObject.getString("text");
                        String date = responseObject.getString("published");

                        String teaser = responseObject.getString("teaser");
                        text.trim();

                        //Get all image-Urls from Gallery
                        try {
                            galleryObject = responseObject.getJSONObject("gallery");
                            images = galleryObject.getJSONArray("images");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                Log.e("!!!!ImageUrl!!!!",url);
                                imageUrls.add(url);
                            }
                        }catch(JSONException e){
                            Log.e("Keine Gallery", "für" + title);
                        }

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser,date, imageUrls);
                        newsList.add(temp);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //for(NewsViewModel newsArticle:newsList){
                //  Log.e("NewsArticle",newsArticle.getUrls());
                //}

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
    public class Image{
        String url;
        String crop_from;
    }
}

