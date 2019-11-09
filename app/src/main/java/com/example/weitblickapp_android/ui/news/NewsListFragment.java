package com.example.weitblickapp_android.ui.news;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsListFragment extends ListFragment {
    ArrayList<NewsViewModel> news = new ArrayList<NewsViewModel>();


    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadNews();
    }

    private void loadNews(){

        // Talk to Rest API

        String URL = "https://new.weitblicker.org/rest/news/?limit=3&search=Benin";

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
                        Integer newsId = responseObject.getInt("id");
                        String title = responseObject.getString("title");

                        String text = responseObject.getString("text");
                        text.trim();

                        Integer image_id = responseObject.getInt("image");
                        String added = responseObject.getString("added");
                        String updated = responseObject.getString("updated");
                        String published = responseObject.getString("published");
                        String range = responseObject.getString("range");
                        String teaser = responseObject.getString("teaser");

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, image_id);
                        news.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(NewsViewModel newsArticle:news){
                    Log.e("NewsArticle",newsArticle.toString());
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
}

