package com.example.weitblickapp_android.ui.project;

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

import androidx.fragment.app.ListFragment;

public class ProjectListFragment extends ListFragment {
    ArrayList<ProjectViewModel> projects = new ArrayList<ProjectViewModel>();


    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadProjects();
    }

    private void loadProjects(){

        // Talk to Rest API
        String URL = "https://new.weitblicker.org/rest/projects/?limit=3&search=Benin";

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
                        Integer projectId = responseObject.getInt("id");
                        String projectName = responseObject.getString("name");
                        String projectDescription = responseObject.getString("description");
                        projectDescription.trim();
                        Integer locationId = responseObject.getInt("location");

                        ProjectViewModel temp = new ProjectViewModel(projectId, projectName, projectDescription, locationId);
                        projects.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                for(ProjectViewModel project:projects){
                    Log.e("NewsArticle",project.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Rest Response", error.toString());
            }
        }){
            //Override header-Information to set Credentials
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

