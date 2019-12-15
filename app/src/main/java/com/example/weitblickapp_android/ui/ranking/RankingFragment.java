package com.example.weitblickapp_android.ui.ranking;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private ListView listView;
    ArrayList<RankingViewModel> bestRankings = new ArrayList<RankingViewModel>();
    ArrayList<RankingViewModel> userFieldRankings = new ArrayList<RankingViewModel>();
    private boolean km_donation = false;
    private boolean km = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        adapter = new RankingListAdapter(getActivity(), bestRankings, getFragmentManager(), km_donation);
        this.setListAdapter(adapter);
        listView = (ListView) view.findViewById(R.id.list);

        ImageView toggle = (ImageView) view.findViewById(R.id.toogleButton);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(km == true){
                    km_donation = false;
                    bestRankings.clear();
                    getRankingData(km_donation);
                    toggle.setImageResource(R.drawable.ic_switchkm);
                    adapter = new RankingListAdapter(getActivity(), bestRankings, getFragmentManager(), km_donation);
                    setListAdapter(adapter);
                    km = false;
                }else{
                    km_donation = true;
                    bestRankings.clear();
                    getRankingData(km_donation);
                    toggle.setImageResource(R.drawable.ic_switcheuro);
                    adapter = new RankingListAdapter(getActivity(), bestRankings, getFragmentManager(), km_donation);
                    setListAdapter(adapter);
                    km = true;
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRankingData(km_donation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void sortByKm(){
        Collections.sort(bestRankings, new Comparator<RankingViewModel>() {
            @Override
            public int compare(RankingViewModel o1, RankingViewModel o2) {
                return Double.compare(o1.getCycledKm(), o2.getCycledKm());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByDonation(){
        Collections.sort(bestRankings, new Comparator<RankingViewModel>() {
            @Override
            public int compare(RankingViewModel o1, RankingViewModel o2) {
                return Double.compare(o2.getCycledDonation(), o1.getCycledDonation());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void getRankingData(boolean km){

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = null;
        if(km == true){
            url = "https://new.weitblicker.org/rest/cycle/ranking/";
        }else{
            url = "https://new.weitblicker.org/rest/cycle/ranking/";
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Save Data into Model
                JSONArray userField = null;
                JSONArray bestField = null;

                String jsonData = response.toString();
                Log.e("RANKING:", jsonData);

                try {
                    bestField = response.getJSONArray("best_field");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    userField = response.getJSONArray("best_field");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Parse the JSON response array by iterating over it
                for (int i = 0; i < bestField.length(); i++) {
                    JSONObject userObject = null;

                    try {
                        userObject = bestField.getJSONObject(i);
                        String username = userObject.getString("username");
                        String imageUrl = userObject.getString("image");
                        double distance = userObject.getDouble("km");
                        double donation = userObject.getDouble("euro");

                        RankingViewModel temp = new RankingViewModel(imageUrl, username, distance, donation);
                        bestRankings.add(temp);
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