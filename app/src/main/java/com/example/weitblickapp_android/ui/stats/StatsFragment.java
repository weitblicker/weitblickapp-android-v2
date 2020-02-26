package com.example.weitblickapp_android.ui.stats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.utils.MyJsonArrayRequest;
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

    private Context mContext;
    private RequestQueue requestQueue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestQueue = Volley.newRequestQueue(mContext);
        session = new SessionManager(mContext);

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

        return view;
    }

    private String getToken(){
        return this.token;
    }


    private void loadStats() {

        // Talk to Rest API

        String URL = "https://weitblicker.org/rest/cycle/tours/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token", this.token);
            Log.e("TOKEN:", this.token);
        } catch (JSONException e) {
            Log.e("TourJsonException:", e.toString());
        }

        MyJsonArrayRequest objectRequest = new MyJsonArrayRequest(Request.Method.GET, URL, jsonBody, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();

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
                headers.put("Media-Type", "application/json");
                headers.put("Authorization", "Token " + getToken());
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }
}



