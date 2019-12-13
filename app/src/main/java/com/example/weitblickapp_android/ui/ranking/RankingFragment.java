package com.example.weitblickapp_android.ui.ranking;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class RankingFragment extends ListFragment{

    private RankingViewModel rankingViewModel;
    private RankingListAdapter adapter;
    ArrayList<RankingViewModel> rankings = new ArrayList<RankingViewModel>();
    private String url = "https://new.weitblicker.org/rest/cycle/ranking/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        adapter = new RankingListAdapter(getActivity(), rankings, getFragmentManager());
        this.setListAdapter(adapter);
        ListView listView = (ListView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRankingData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void sortByKm(){
        Collections.sort(rankings, new Comparator<RankingViewModel>() {
            @Override
            public int compare(RankingViewModel o1, RankingViewModel o2) {
                return Double.compare(o1.getCycledKm(), o2.getCycledKm());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByDonation(){
        Collections.sort(rankings, new Comparator<RankingViewModel>() {
            @Override
            public int compare(RankingViewModel o1, RankingViewModel o2) {
                return Double.compare(o2.getCycledDonation(), o1.getCycledDonation());
            }
        });
        adapter.notifyDataSetChanged();
    }


    public void getRankingData(){

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();
                Log.e("RANKING:", jsonData);

                //Parse the JSON response array by iterating over it
                for (int i = response.length()-1; i >= 0; i--) {
                    JSONObject responseObject = null;

                    try {
                        responseObject = response.getJSONObject(i);

                        String username = responseObject.getString("username");
                        String imageUrl = responseObject.getString("image");
                        double distance = responseObject.getDouble("km");
                        double donation = responseObject.getDouble("euro");

                        RankingViewModel temp = new RankingViewModel(imageUrl, username, distance, donation);
                        rankings.add(temp);
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
                Log.e("Ranking ErrorResponse", error.toString());
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