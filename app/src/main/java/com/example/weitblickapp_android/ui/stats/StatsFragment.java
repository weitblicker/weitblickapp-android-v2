package com.example.weitblickapp_android.ui.stats;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class StatsFragment extends ListFragment {

    ArrayList<StatsViewModel> statsList = new ArrayList<StatsViewModel>();
    private StatsViewModel statsViewModel;
    private StatsListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadStats();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        adapter = new StatsListAdapter(getActivity(), statsList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void loadStats(){

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
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;

                    try {
                        responseObject = response.getJSONObject(i);
                        Integer statsID = responseObject.getInt("id");
                        String distance = responseObject.getString("title");
                        String duration = responseObject.getString("text");
                        String date = responseObject.getString("published");
                        String donation = responseObject.getString("teaser");

                        StatsViewModel temp = new StatsViewModel(distance, donation, duration, date);
                        statsList.add(temp);
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
}