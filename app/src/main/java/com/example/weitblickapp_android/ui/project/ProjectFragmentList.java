package com.example.weitblickapp_android.ui.project;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectFragmentList {
    ArrayList<ProjectViewModel> projects = new ArrayList<ProjectViewModel>();

/*
    @Override
    public void onActivityCreated(Bundle saveInstanceState) {

        loadProjects();
    }
    */


    private void loadProjects(){

        //TODO put this to options
        final String url = "https://new.weitblicker.org/rest/projects/";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        // check whether an update is necessary or not, maybe merge
                        // update news -> clear existing list of news
                        projects.clear();

                        try {
                            System.out.println(response.toString());
                            JSONArray articllist = response.getJSONArray("news-list");
                            try {
                                for (int i = 0; i < articllist.length(); i++) {
                                    JSONObject container = articllist.getJSONObject(i);
                                    JSONObject project = container.getJSONObject("news-article");
                                    String title = project.getString("article-title");
                                    String description = project.getString("description");
                                    //Creates project-object and assigns values to it
                                    ProjectViewModel projectX = new ProjectViewModel();
                                    projectX.setName(title);
                                    projectX.setDescription(description);

                                    projects.add(projectX);
                                }
                            } catch (JSONException e){
                                // TODO log this error
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            // TODO log this error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        error.printStackTrace();
                    }
                });
    }
}

