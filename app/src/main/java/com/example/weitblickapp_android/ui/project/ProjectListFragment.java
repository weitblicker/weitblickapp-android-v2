package com.example.weitblickapp_android.ui.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerViewModel;
import com.example.weitblickapp_android.ui.sponsor.SponsorViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class ProjectListFragment extends Fragment implements OnMapReadyCallback {
    static ArrayList<ProjectViewModel> projectList = new ArrayList<ProjectViewModel>();
    private ProjectListAdapter adapter;
    static private GoogleMap mMap;
    static SupportMapFragment mapFrag;
    private BlogEntryViewModel tempBLog = null;
    ListView list;
    static private Map<Marker, Integer> allMarkersMap = new HashMap<Marker, Integer>();
    static Bitmap smallMarker;
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProjects();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bitmap bitmapdraw = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_marker_foreground);
        smallMarker = Bitmap.createScaledBitmap(bitmapdraw, 100, 100, false);
        for(int i = 0; i < projectList.size(); i++){
            LatLng location = new LatLng( projectList.get(i).getLat(), projectList.get(i).getLng());
            Marker marker = mMap.addMarker( new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(projectList.get(i).getName()));
            allMarkersMap.put(marker, i);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index = allMarkersMap.get(marker);
                list.setSelection(index);
                adapter.select(index);
                CameraUpdate cu = CameraUpdateFactory.newLatLng(marker.getPosition());
                mMap.animateCamera(cu);
                int height = list.getHeight();
                list.smoothScrollToPositionFromTop(index, height/6);
                return true;

            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project, container, false);
        list = (ListView) view.findViewById(R.id.liste);
        adapter = new ProjectListAdapter(getActivity(), projectList, getFragmentManager());
        list.setAdapter(adapter);

        adapter.setOnItemClickedListener(
                new ProjectListAdapter.OnItemClicked(){
                    @Override
                    public void onClick(int position) {
                        ProjectViewModel pro = adapter.getItem(position);
                        LatLng posLatLng = new LatLng(pro.getLat(),pro.getLng());
                        CameraUpdate cu = CameraUpdateFactory.newLatLng(posLatLng);
                        mMap.animateCamera(cu);
                        adapter.select(position);
                        int height = list.getHeight();
                        list.smoothScrollToPositionFromTop(position,height/6);
                    }
                });

        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        return view;
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
                    JSONArray partnerJSONObject = null;
                    JSONObject partnerObject = null;
                    JSONObject cycleObject = null;
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    ArrayList<Integer> newsIds = new ArrayList<Integer>();
                    ArrayList<Integer> blogIds = new ArrayList<Integer>();
                    ArrayList<Integer> sponsorenid = new ArrayList<Integer>();
                    ArrayList<String> hostsId = new ArrayList<String>();
                    ArrayList<BlogEntryViewModel> blogsArr = new ArrayList<BlogEntryViewModel>();
                    ArrayList<NewsViewModel> newsArr = new ArrayList<NewsViewModel>();
                    ArrayList<ProjectPartnerViewModel> partnerArr = new ArrayList<ProjectPartnerViewModel>();
                    ArrayList<SponsorViewModel> sponsorArr = new ArrayList<SponsorViewModel>();
                    ArrayList<Integer> partnerIds = new ArrayList<Integer>();
                    JSONArray images = null;
                    JSONArray news = null;
                    JSONArray blogs = null;
                    JSONArray hosts = null;
                    CycleViewModel cycle = null;
                    ArrayList<String> allHosts = new ArrayList<String>();
                    try {
                        responseObject = response.getJSONObject(i);
                        int projectId = responseObject.getInt("id");
                        String title = responseObject.getString("name");

                        String text = responseObject.getString("description");
                        String goal_description = responseObject.getString("goal_description");

                        float currentAmountDonationGoal = 0;
                        float donationGoalDonationGoal = 0;

                       // currentAmountDonationGoal = responseObject.getLong("donation_current");
                       // donationGoalDonationGoal = responseObject.getLong("donation_goal");

                        currentAmountDonationGoal = 1250.30f;
                        donationGoalDonationGoal = 20000;

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
                            hosts = responseObject.getJSONArray("hosts");
                            for (int x = 0; x < hosts.length(); x++) {
                                hostsId.add(hosts.getString(x));
                            }
                            allHosts = loadHosts(hostsId);
                        }catch(JSONException e){

                        }
                        Toast toast= Toast. makeText(getContext(),allHosts.toString(),Toast. LENGTH_SHORT);
                        toast. setMargin(50,50);
                        toast. show();


                        locationObject = responseObject.getJSONObject("location");

                        float lat = locationObject.getLong("lat");
                        float lng = locationObject.getLong("lng");
                        String name = locationObject.getString("name");
                        String address = locationObject.getString("address");

                        cycleJSONObject = responseObject.getJSONArray("cycle");
                        partnerJSONObject = responseObject.getJSONArray("partners");

                        float current_amount = 0;
                        float cycle_donation = 0;
                        boolean finished = false;
                        int cycle_id = 0;
                        float goal_amount = 0;
                        float rateProKm = 0;


                        for (int x = 0; x < cycleJSONObject.length(); x++) {
                            cycleObject = cycleJSONObject.getJSONObject(x);
                             current_amount = cycleObject.getLong("current_amount");
                             cycle_donation = cycleObject.getLong("goal_amount");
                             finished = cycleObject.getBoolean("finished");
                             cycle_id = cycleObject.getInt("cycle_donation");
                             goal_amount = cycleObject.getLong("goal_amount");
                             cycle = new CycleViewModel(current_amount, cycle_donation, finished, cycle_id, goal_amount);
                             sponsorenid.add(cycle_id);
                        }
                        if(cycle != null){
                            sponsorArr = loadSponsor(sponsorenid);
                        }

                        String logo = null;
                        String description = null;
                        String weblink = null;
                        String partnerName = null;

                        for(int y = 0; y < partnerJSONObject.length(); y++){
                            partnerObject = partnerJSONObject.getJSONObject(y);
                            logo = partnerObject.getString("logo");
                            description = partnerObject.getString("description");
                            partnerName = partnerObject.getString("name");
                            weblink = partnerObject.getString("link");
                            partnerArr.add(new ProjectPartnerViewModel(partnerName,description,weblink,logo));
                        }
                        text.trim();
                        ProjectViewModel temp = new ProjectViewModel(projectId, title, text, lat, lng, address, name, cycle, imageUrls, partnerArr, newsArr, blogsArr, sponsorArr, currentAmountDonationGoal, donationGoalDonationGoal, goal_description, allHosts);
                        projectList.add(temp);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for(ProjectViewModel newsArticle:projectList){
                   // Log.e("Projects",newsArticle.toString());
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

    public ArrayList<String> loadHosts(ArrayList<String> hostId){
        ArrayList <String> hosts = new ArrayList<String>();

        for(int i = 0; i < hostId.size(); i++){
            String url = "https://weitblicker.org/rest/unions/" + hostId.get(i);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {

                    try {
                        String name =  responseObject.getString("name");
                        hosts.add(name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
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
        return hosts;
    }

    public ArrayList<SponsorViewModel> loadSponsor(ArrayList<Integer> sponsorenId){
        ArrayList <SponsorViewModel> sponsoren = new ArrayList<SponsorViewModel>();

        for(int i = 0; i < sponsorenId.size(); i++){
            String url = "https://weitblicker.org/rest/cycle/donations/" + sponsorenId.get(i);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    //Save Data into Model
                    //Parse the JSON response array by iterating over it
                    JSONObject partner = null;
                    SponsorViewModel temp = null;

                    try {
                        partner = responseObject.getJSONObject("partner");
                        String name =  partner.getString("name");
                        String desc = partner.getString("description");
                        String logo = partner.getString("logo");
                        float rateProKm = responseObject.getLong("rate_euro_km");
                        String address = partner.getString("link");

                        temp = new SponsorViewModel(name, desc, address, logo, rateProKm);
                        sponsoren.add(temp);
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
        return sponsoren;
    }

    public ArrayList<BlogEntryViewModel> loadBlog(ArrayList<Integer> blogsId){

        ArrayList<BlogEntryViewModel> blogs = new ArrayList<BlogEntryViewModel>();
        // Talk to Rest API
        for(int i = 0; i < blogsId.size(); i++){
            String url = "https://weitblicker.org/rest/blog/" + blogsId.get(i);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    //Save Data into Model
                    //Parse the JSON response array by iterating over it
                    JSONObject imageObject = null;
                    BlogEntryViewModel temp = null;
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;

                    try {
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
                        } catch (JSONException e) {

                        }
                        //TODO: Check if picture exists
                        //Get Date of last Item loaded in List loading more news starting at that date
                        try {
                            Date ItemDate = formatterRead.parse(published);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tempBLog = new BlogEntryViewModel(blogId, title, text, teaser, published, imageUrls);
                        blogs.add(tempBLog);
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
           // Log.e("ImageUrl", m.group(2));

            imageUrls.add(m.group(2));
        }
        return imageUrls;
    }

    public String extractImageUrls(String text){
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)","");
        return text;
    }
}
