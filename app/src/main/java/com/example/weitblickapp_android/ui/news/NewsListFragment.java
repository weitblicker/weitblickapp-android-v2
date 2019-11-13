package com.example.weitblickapp_android.ui.news;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import com.example.weitblickapp_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsListFragment extends ListFragment {

    ArrayList<NewsViewModel> newsList = new ArrayList<NewsViewModel>();
    private NewsListAdapter adapter;


      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          loadNews();
      }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        adapter = new NewsListAdapter(getActivity(), newsList);
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
                FragmentTransaction replace = ft.replace(R.id.fragment_container, new NewsDetailFragment(newsList.get(position)));
                ft.commit();
            }
        });
    }
    public void loadNews(){

        // Talk to Rest API

        String URL = "https://new.weitblicker.org/rest/news/?limit=5";

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
                        String teaser = responseObject.getString("teaser");

                        text.trim();

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser);
                        newsList.add(temp);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(NewsViewModel newsArticle:newsList){
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