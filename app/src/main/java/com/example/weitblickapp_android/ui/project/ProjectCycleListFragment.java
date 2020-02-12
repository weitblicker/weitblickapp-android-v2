package com.example.weitblickapp_android.ui.project;

import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.event.EventLocation;
import com.example.weitblickapp_android.ui.event.EventViewModel;
import com.example.weitblickapp_android.ui.milenstone.MilenstoneViewModel;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerViewModel;
import com.example.weitblickapp_android.ui.sponsor.SponsorViewModel;

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
    private ProjectAdapterShort adapter;
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProjects();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project_cycle, container, false);

        adapter = new ProjectAdapterShort(getActivity(), projectList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    JSONObject accountObject = null;
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
                    JSONObject host = null;
                    JSONObject bankAccount = null;
                    ArrayList<String> allHosts = new ArrayList<String>();
                    JSONArray mileStoneArray = null;
                    JSONObject mileStone = null;
                    ArrayList<MilenstoneViewModel> allMilestone = new ArrayList<MilenstoneViewModel>();
                    JSONArray donations = null;
                    JSONObject donation = null;
                    JSONArray events = null;
                    ArrayList<Integer> eventIds = new ArrayList<Integer>();
                    ArrayList<EventViewModel> eventArr = new ArrayList<EventViewModel>();

                    try {
                        responseObject = response.getJSONObject(i);
                        int projectId = responseObject.getInt("id");
                        String title = responseObject.getString("name");

                        String text = responseObject.getString("description");
                        String goal_description = responseObject.getString("goal_description");

                        String currentAmountDonationGoal = null;
                        String donationGoalDonationGoal = null;

                        currentAmountDonationGoal = responseObject.getString("donation_current");
                        donationGoalDonationGoal = responseObject.getString("donation_goal");


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
                            events = responseObject.getJSONArray("events");
                            for (int x = 0; x < events.length(); x++) {
                                eventIds.add(events.getInt(x));
                            }
                            eventArr = loadEvents(eventIds);
                        }catch(JSONException e){

                        }


                        hosts = responseObject.getJSONArray("hosts");
                        String bankname = null;
                        String iban = null;
                        String bic = null;

                        for(int x = 0; x < hosts.length(); x++){
                            host = hosts.getJSONObject(x);
                            allHosts.add(host.getString("city"));
                        }

                        if(!responseObject.getString("donation_account").contains("null")){
                            accountObject = responseObject.getJSONObject("donation_account");
                            bankname = accountObject.getString("account_holder");
                            iban = accountObject.getString("iban");
                            bic = accountObject.getString("bic");
                        }


                        locationObject = responseObject.getJSONObject("location");

                        float lat = locationObject.getLong("lat");
                        float lng = locationObject.getLong("lng");
                        String name = locationObject.getString("name");
                        String address = locationObject.getString("address");
                        String descriptionLocation = locationObject.getString("description");
                        partnerJSONObject = responseObject.getJSONArray("partners");



                        String current_amount = null;
                        String cycle_donation = null;
                        int cyclist = 0;
                        String km_sum = null;


                        if(!responseObject.getString("cycle").contains("null")){
                            cycleObject = responseObject.getJSONObject("cycle");
                            current_amount = cycleObject.getString("euro_sum");
                            cycle_donation = cycleObject.getString("euro_goal");
                            cyclist = cycleObject.getInt("cyclists");
                            km_sum = cycleObject.getString("km_sum");
                            donations = cycleObject.getJSONArray("donations");
                            for(int y = 0; y < donations.length(); y++){
                                donation = donations.getJSONObject(y);
                                sponsorenid.add(donation.getInt("id"));
                            }
                            cycle = new CycleViewModel(current_amount, cycle_donation, cyclist, km_sum);
                            if(donations.length() > 0) {
                                sponsorArr = loadSponsor(sponsorenid);
                            }
                        }else{
                            cycle = null;
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

                        String nameMile;
                        String descr;
                        String date;
                        boolean reached;

                        try {
                            mileStoneArray = responseObject.getJSONArray("milestones");
                            for (int x = 0; x < mileStoneArray.length(); x++) {
                                mileStone = mileStoneArray.getJSONObject(x);
                                nameMile = mileStone.getString("name");
                                descr = mileStone.getString("description");
                                date = mileStone.getString("date");
                                reached = mileStone.getBoolean("reached");
                                allMilestone.add(new MilenstoneViewModel(nameMile, date, descr, reached));
                            }
                        }catch(JSONException e){

                        }

                        if(cycle != null){
                            ProjectViewModel temp = new ProjectViewModel(projectId, title, text, lat, lng, address, descriptionLocation, name, cycle, imageUrls, partnerArr, newsArr, blogsArr, sponsorArr, currentAmountDonationGoal, donationGoalDonationGoal, goal_description, allHosts, bankname, iban, bic, allMilestone, eventArr);
                            projectList.add(temp);
                            adapter.notifyDataSetChanged();
                        }
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

    public ArrayList<EventViewModel> loadEvents(ArrayList<Integer> eventId){
        ArrayList <EventViewModel> events = new ArrayList<EventViewModel>();
        for(int i = 0; i < eventId.size(); i++) {
            String URL = "https://weitblicker.org/rest/events/" + eventId.get(i);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    Location eventLocation;
                    JSONArray images = null;
                    JSONObject image = null;

                    JSONObject locationObject = null;
                    EventLocation location = null;

                    String name;
                    String address;
                    double lat;
                    double lng;

                    JSONObject hostObject = null;
                    String hostName;
                    String locationDescription;

                    try {
                        Integer eventId = responseObject.getInt("id");
                        String title = responseObject.getString("title");
                        String description = responseObject.getString("description");
                        String startDate = responseObject.getString("start");
                        String endDate = responseObject.getString("end");

                        locationObject = responseObject.getJSONObject("location");
                        name = locationObject.getString("name");
                        address = locationObject.getString("address");
                        lat = locationObject.getDouble("lat");
                        lng = locationObject.getDouble("lng");
                        locationDescription = locationObject.getString("description");

                        hostObject = responseObject.getJSONObject("host");
                        hostName = hostObject.getString("city");


                        try {
                            images = responseObject.getJSONArray("photos");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }

                        } catch (JSONException e) {

                        }

                        //Get inline-Urls from Text, then extract them
                        // imageUrls = getImageUrls(text);
                        description = extractImageUrls(description);

                        location = new EventLocation(name, address, lat, lng, locationDescription);

                        EventViewModel temp = new EventViewModel(eventId, title, description, startDate, endDate, hostName, location, imageUrls);
                        events.add(temp);
                        adapter.notifyDataSetChanged();
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
        return events;
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
                        String rateProKm = responseObject.getString("rate_euro_km");
                        String goal_amount_Sponsor = responseObject.getString("goal_amount");
                        String address = partner.getString("link");

                        temp = new SponsorViewModel(name, desc, address, logo, rateProKm, goal_amount_Sponsor);

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
                        JSONObject author = null;
                        JSONObject hosts = null;
                        JSONObject host = null;
                        ArrayList<String> allHosts = new ArrayList<String>();

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
                            String location = responseObject.getString("location");

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

                            hosts = responseObject.getJSONObject("host");
                            allHosts.add(hosts.getString("city"));

                            author = responseObject.getJSONObject("author");
                            String name = author.getString("name");
                            String profilPic = author.getString("image");

                            //TODO: Check if picture exists
                            //Get Date of last Item loaded in List loading more news starting at that date
                            try {
                                Date ItemDate = formatterRead.parse(published);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            BlogEntryViewModel blog = new BlogEntryViewModel(blogId, title, text, teaser,published, imageUrls, name,profilPic,allHosts, location);

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
                    JSONObject author = null;
                    JSONObject hosts = null;
                    JSONObject host = null;
                    ArrayList<String> allHosts = new ArrayList<String>();

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

                        author = responseObject.getJSONObject("author");
                        String name = author.getString("name");
                        String profilPic = author.getString("image");

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

                        hosts = responseObject.getJSONObject("host");
                        allHosts.add(hosts.getString("city"));

                        //Get inline-Urls from Text, then extract them
                        // imageUrls = getImageUrls(text);
                        text = extractImageUrls(text);

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser,date, imageUrls, name, profilPic, allHosts);

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
