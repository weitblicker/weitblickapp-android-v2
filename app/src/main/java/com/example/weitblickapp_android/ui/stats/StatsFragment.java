package com.example.weitblickapp_android.ui.stats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.MyJsonArrayRequest;
import com.example.weitblickapp_android.ui.location.MapFragment;
import com.example.weitblickapp_android.ui.login.Login_Activity;

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

    private SessionManager session;
    private String token;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!session.isLoggedIn()){
            
            Intent redirect=new Intent(getActivity(), Login_Activity.class);
            getActivity().startActivity(redirect);
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else{
            this.token = session.getKey();
            loadStats();
        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        adapter = new StatsListAdapter(getActivity(), statsList, getFragmentManager());
        this.setListAdapter(adapter);

        session = new SessionManager(getActivity().getApplicationContext());




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void loadStats() {

        // Talk to Rest API

        String URL = "https://new.weitblicker.org/rest/cycle/tours/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", this.token);
            Log.e("TOKEN:", this.token);
        } catch (JSONException e) {
            Log.e("TourJsonException:", e.toString());
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        MyJsonArrayRequest objectRequest = new MyJsonArrayRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();

                Log.e("STATSRESPONSE:", jsonData);

                //Parse the JSON response array by iterating over it
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = response.getJSONObject(i);

                        double distance = responseObject.getDouble("km");
                        double donation = responseObject.getDouble("euro");
                        int tourId = responseObject.getInt("tour");
                        String tourDate = responseObject.getString("start");
                        int duration = responseObject.getInt("duration");

                        JSONObject projectObject = responseObject.getJSONObject("project");
                        int projectId = projectObject.getInt("id");


                        StatsViewModel temp = new StatsViewModel(projectId, tourId, distance, donation, duration, tourDate);
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
                Log.e("StatsError Response", error.toString());
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



