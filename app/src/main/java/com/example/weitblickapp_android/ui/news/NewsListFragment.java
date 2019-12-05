package com.example.weitblickapp_android.ui.news;

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
import androidx.fragment.app.ListFragment;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsListFragment extends ListFragment implements AbsListView.OnScrollListener {
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<NewsViewModel> newsList = new ArrayList<NewsViewModel>();
    private NewsListAdapter adapter;
    private int preLast;
    private String lastItemDate;
    private String lastItemDateCheck = "";
    private String url = "https://new.weitblicker.org/rest/news?limit=5";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNews(url);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        adapter = new NewsListAdapter(getActivity(), newsList, getFragmentManager());
        this.setListAdapter(adapter);

        ListView listView = (ListView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void loadNews(String URL){

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

                        //Get Date of last Item loaded in List loading more news starting at that date
                        try {
                            Date ItemDate = formatterRead.parse(date);
                            lastItemDate = formatterWrite.format(ItemDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String teaser = responseObject.getString("teaser");

                        text.trim();

                        //Get all image-Urls from Gallery
                        try {
                            galleryObject = responseObject.getJSONObject("gallery");
                            images = galleryObject.getJSONArray("images");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }
                        }catch(JSONException e){
                           // Log.e("Keine Gallery", "für" + title);
                        }

                        //Get inline-Urls from Text, then extract them
                        imageUrls = getImageUrls(text);
                        text = extractImageUrls(text);

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser,date, imageUrls);
                        newsList.add(temp);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount > 0) {
            final int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem == totalItemCount && !lastItemDateCheck.equals(lastItemDate)) {
                url = url.concat(("&end=" + lastItemDate));
                loadNews(url);
                lastItemDateCheck = lastItemDate;
            }
        }
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
        Log.e("TEXT", text);
        return text;
    }
}

