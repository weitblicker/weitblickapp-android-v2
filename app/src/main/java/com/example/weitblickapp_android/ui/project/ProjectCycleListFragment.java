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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryViewModel;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectCycleListFragment extends ListFragment {
    ArrayList<ProjectViewModel> projectList = new ArrayList<ProjectViewModel>();
    private ProjectCycleListAdapter adapter;
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProjects();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project_cycle, container, false);

        adapter = new ProjectCycleListAdapter(getActivity(), projectList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        /*ImageButton detail = (ImageButton) v.findViewById(R.id.news_more_btn);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentTransaction replace = ft.replace(R.id.fragment_container, new ProjectDetailFragment(projectList.get(position)));
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/
    }
    public void loadProjects(){

        // Talk to Rest API
        String URL = "https://weitblicker.org/rest/projects/";

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
                    ArrayList<Integer> newsIds = new ArrayList<Integer>();
                    ArrayList<Integer> blogIds = new ArrayList<Integer>();
                    ArrayList<BlogEntryViewModel> blogsArr = new ArrayList<BlogEntryViewModel>();
                    ArrayList<NewsViewModel> newsArr = new ArrayList<NewsViewModel>();
                    ArrayList<ProjectPartnerViewModel> partnerArr = new ArrayList<ProjectPartnerViewModel>();
                    ArrayList<Integer> partnerIds = new ArrayList<Integer>();
                    JSONObject newPost = null;
                    JSONObject blog = null;
                    JSONArray images = null;
                    JSONArray news = null;
                    JSONArray blogs = null;
                    JSONArray partners = null;
                    try {
                        responseObject = response.getJSONObject(i);
                        int projectId = responseObject.getInt("id");
                        String title = responseObject.getString("name");

                        String text = responseObject.getString("description");

                        imageUrls = getImageUrls(text);
                        text = extractImageUrls(text);

                        try {
                            images = responseObject.getJSONArray("photos");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }

                        }catch(JSONException e){

                        }
                        try {
                            news = responseObject.getJSONArray("news");
                            for (int x = 0; x < news.length(); x++) {
                                newsIds.add(news.getInt(x));
                            }
                            newsArr = loadNews(newsIds);
                        }catch(JSONException e){

                        }
                        try {
                            blogs = responseObject.getJSONArray("blog");
                            for (int x = 0; x < blogs.length(); x++) {
                                blogIds.add(blogs.getInt(x));
                            }
                            blogsArr = loadBlog(blogIds);
                        }catch(JSONException e){

                        }
                        try {
                            partners = responseObject.getJSONArray("partner");
                            for (int x = 0; x < partners.length(); x++) {
                                partnerIds.add(partners.getInt(x));
                            }
                        }catch(JSONException e){

                        }

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

                        ProjectViewModel temp = new ProjectViewModel(projectId, title, text, lat, lng, address, name, current_amount, cycle_donation,finished, cycle_id, goal_amount, imageUrls, partnerArr, newsArr, blogsArr);
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

    public ArrayList<BlogEntryViewModel> loadBlog(ArrayList<Integer> blogsId){

        ArrayList<BlogEntryViewModel> blogs = new ArrayList<BlogEntryViewModel>();

        // Talk to Rest API
        for(int i = 0; i < blogsId.size(); i++){
            String url = "https://weitblicker.org/rest/blog/" + blogsId.get(i);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    //Save Data into Model
                    String jsonData = response.toString();
                    //Parse the JSON response array by iterating over it
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject responseObject = null;
                        JSONObject imageObject = null;
                        BlogEntryViewModel temp = null;
                        JSONObject galleryObject = null;
                        JSONObject image = null;
                        ArrayList<String> imageUrls = new ArrayList<String>();
                        JSONArray images = null;
                        try {
                            responseObject = response.getJSONObject(i);
                            Integer blogId = responseObject.getInt("id");
                            String title = responseObject.getString("title");
                            String text = responseObject.getString("text");
                            text = text.trim();
                            text = text.replaceAll("\n{2,}", "\n");
                            String published = responseObject.getString("published");
                            String teaser = responseObject.getString("teaser");
                            imageUrls = getImageUrls(text);
                            text = extractImageUrls(text);
                            //Get all imageUrls from Gallery
                            try {
                                galleryObject = responseObject.getJSONObject("gallery");

                                images = galleryObject.getJSONArray("images");
                                for (int x = 0; x < images.length(); x++) {
                                    image = images.getJSONObject(x);
                                    String url = image.getString("url");
                                    imageUrls.add(url);
                                }

                            }catch(JSONException e){

                            }
                            //TODO: Check if picture exists
                            //Get Date of last Item loaded in List loading more news starting at that date
                            try {
                                Date ItemDate = formatterRead.parse(published);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            BlogEntryViewModel blog = new BlogEntryViewModel(blogId, title, text, teaser,published, imageUrls);

                            blogs.add(blog);
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
        }
        return blogs;
    }

    public ArrayList<NewsViewModel> loadNews(ArrayList<Integer> newsId){

        ArrayList<NewsViewModel> news = new ArrayList<NewsViewModel>();
        // Talk to Rest API
        for(int i = 0; i < newsId.size(); i++){
            String url = "https://weitblicker.org/rest/news/" + newsId.get(i);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    //Save Data into Model
                    //Parse the JSON response array by iterating over it
                    JSONObject imageObject = null;
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;
                    try {
                        Integer newsId = responseObject.getInt("id");
                        String title = responseObject.getString("title");
                        String text = responseObject.getString("text");
                        String date = responseObject.getString("published");
                        // String date = "2009-09-26T14:48:36Z";

                        try{
                            Date ItemDate = formatterRead.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String teaser = responseObject.getString("teaser");

                        text.trim();

                        //Get all image-Urls from Gallery
                        try {
                            images = responseObject.getJSONArray("photos");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }

                        }catch(JSONException e){

                        }

                        //Get inline-Urls from Text, then extract them
                        // imageUrls = getImageUrls(text);
                        text = extractImageUrls(text);

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser,date, imageUrls);
                        news.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        return news;
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
