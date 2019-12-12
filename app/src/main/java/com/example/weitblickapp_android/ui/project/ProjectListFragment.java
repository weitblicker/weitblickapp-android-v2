package com.example.weitblickapp_android.ui.project;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectListFragment extends ListFragment {
    ArrayList<ProjectViewModel> projectList = new ArrayList<ProjectViewModel>();
    private ProjectListAdapter adapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProjects();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project, container, false);

        adapter = new ProjectListAdapter(getActivity(), projectList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        ImageButton detail = (ImageButton) v.findViewById(R.id.news_more_btn);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentTransaction replace = ft.replace(R.id.fragment_container, new ProjectDetailFragment(projectList.get(position)));
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
    public void loadProjects(){

        // Talk to Rest API

        String URL = "https://new.weitblicker.org/rest/projects/?limit=5";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();
                //Parse the JSON response array by iterating over it
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;
                    JSONObject locationObject = null;
                    JSONArray cycleJSONObject = null;
                    JSONObject cycleObject = null;
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;
                    try {
                        responseObject = response.getJSONObject(i);
                        int projectId = responseObject.getInt("id");
                        String title = responseObject.getString("name");

                        String text = responseObject.getString("description");
                        //String teaser = responseObject.getString("teaser");

                        //galleryObject = responseObject.getJSONObject("gallery");

                        /*if (galleryObject != null) {
                            images = galleryObject.getJSONArray("images");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                Log.e("!!!!ImageUrl!!!!",url);
                                imageUrls.add(url);
                            }
                        }*/

                        locationObject = responseObject.getJSONObject("location");

                        float lat = locationObject.getLong("lat");
                        float lng = locationObject.getLong("lng");
                        String name = locationObject.getString("name");
                        String address = locationObject.getString("address");

                        cycleJSONObject = responseObject.getJSONArray("cycle");

                        float current_amount = 0;
                        float cycle_donation = 0;
                        boolean finished = false;
                        int cycle_id = 0;
                        float goal_amount = 0;

                        for (int x = 0; x < cycleJSONObject.length(); x++) {
                            cycleObject = cycleJSONObject.getJSONObject(x);
                             current_amount = cycleObject.getLong("current_amount");
                             cycle_donation = cycleObject.getLong("goal_amount");
                             finished = cycleObject.getBoolean("finished");
                             cycle_id = cycleObject.getInt("cycle_donation");
                             goal_amount = cycleObject.getLong("goal_amount");
                        }

                        text.trim();

                        imageUrls = getImageUrls(text);
                        text = extractImageUrls(text);



                        ProjectViewModel temp = new ProjectViewModel(projectId, title, text, lat, lng, address, name, current_amount, cycle_donation,finished, cycle_id, goal_amount, imageUrls);
                        projectList.add(temp);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(ProjectViewModel newsArticle:projectList){
                    Log.e("Projects",newsArticle.toString());
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

    public ArrayList <String> getImageUrls(String text){
        //Find image-tag markdowns and extract
        ArrayList <String> imageUrls = new ArrayList<>();
        Matcher m = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)")
                .matcher(text);
        while (m.find()) {
            Log.e("ImageUrl", m.group(2));

            imageUrls.add(m.group(2));
        }
        return imageUrls;
    }

    public String extractImageUrls(String text){
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)","");
        return text;
    }
}
