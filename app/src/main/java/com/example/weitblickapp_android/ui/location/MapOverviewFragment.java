package com.example.weitblickapp_android.ui.location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryViewModel;
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.event.EventLocation;
import com.example.weitblickapp_android.ui.event.EventViewModel;
import com.example.weitblickapp_android.ui.login.Login_Activity;
import com.example.weitblickapp_android.ui.project.milenstone.MilenstoneViewModel;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.project.partner.ProjectPartnerViewModel;
import com.example.weitblickapp_android.ui.project.ProjectCycleListFragment;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.example.weitblickapp_android.ui.project.sponsor.SponsorViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

public class MapOverviewFragment extends Fragment implements OnMapReadyCallback {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");

    private GoogleMap mMap;
    private SessionManager session;
    private SharedPreferences defaultProjects;
    private String PREF_NAME = "DefaultProject";
    private String defaultproject;
    private float lat;
    private float lng;
    private ProjectViewModel project = null;
    private int projectID;

    private MapFragment fragmentMap = null;

    private boolean pending = false;

    private final static String TAG_FRAGMENT = "MAP_FRAGMENT";

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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
            View root = inflater.inflate(R.layout.fragment_map, container, false);
            SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
            defaultProjects = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
            checkDefault();

            if (settings.contains("projectid")){
                loadProject(settings.getInt("projectid", -1));
            }

            Button defaultProject = root.findViewById(R.id.defaultProject);
            defaultProject.setText(defaultproject);

            session = new SessionManager(getActivity().getApplicationContext());

            defaultProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectCycleListFragment fragment = new ProjectCycleListFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment, "Map");
                    ft.addToBackStack(null);
                    ft.commit();
                }

            });

            ImageView img = root.findViewById(R.id.play);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!session.isLoggedIn()) {
                        Intent redirect = new Intent(getActivity(), Login_Activity.class);
                        getActivity().startActivity(redirect);
                    } else {
                            if (project != null) {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                fragmentMap = new MapFragment(project);
                                ft.add(R.id.fragment_container, fragmentMap, TAG_FRAGMENT);
                                ft.commit();
                            } else {
                                Toast.makeText(getActivity(), "Bitte wählen Sie ein Projekt zum Spenden aus!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }

            });
            setUpMapIfNeeded();
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment fragment = getFragmentManager().findFragmentByTag("MAP_FRAGMENT");

        if(fragment != null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment, TAG_FRAGMENT);
            ft.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment fragment = getFragmentManager().findFragmentByTag("MAP_FRAGMENT");
        if(fragment != null) {
            Log.e("Fragment ", "existiert!");
           // getFragmentManager().popBackStack();
        }
        if(pending){
            Log.e("TOURPENDING", "PENDING");
        }
    }

    public void loadProject(int projectID){
        // Talk to Rest API
        String URL = "https://weitblicker.org/rest/projects/" + projectID;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseObject) {
                //Save Data into Model
                JSONObject locationObject = null;
                JSONArray cycleJSONObject = null;
                JSONArray partnerJSONObject = null;
                JSONObject partnerObject = null;
                JSONObject cycleObject = null;
                JSONObject galleryObject = null;
                JSONObject accountObject = null;
                JSONObject image = null;
                ArrayList<String> imageUrls = new ArrayList<String>();
                ArrayList<Integer> newsIds = new ArrayList<Integer>();
                ArrayList<Integer> blogIds = new ArrayList<Integer>();
                ArrayList<Integer> sponsorenid = new ArrayList<Integer>();
                ArrayList<BlogEntryViewModel> blogsArr = new ArrayList<BlogEntryViewModel>();
                ArrayList<EventViewModel> eventArr = new ArrayList<EventViewModel>();
                ArrayList<NewsViewModel> newsArr = new ArrayList<NewsViewModel>();
                ArrayList<ProjectPartnerViewModel> partnerArr = new ArrayList<ProjectPartnerViewModel>();
                ArrayList<SponsorViewModel> sponsorArr = new ArrayList<SponsorViewModel>();
                ArrayList<Integer> partnerIds = new ArrayList<Integer>();
                ArrayList<Integer> eventIds = new ArrayList<Integer>();
                JSONArray images = null;
                JSONArray news = null;
                JSONArray blogs = null;
                JSONArray events = null;
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
                String imageString = null;
                try {
                    int projectId = responseObject.getInt("id");
                    String title = responseObject.getString("name");
                    MilenstoneViewModel mile = null;

                    String text = responseObject.getString("description");
                    String goal_description = responseObject.getString("goal_description");

                    String currentAmountDonationGoal = null;
                    String donationGoalDonationGoal = null;

                    currentAmountDonationGoal = responseObject.getString("donation_current");
                    donationGoalDonationGoal = responseObject.getString("donation_goal");


                    imageUrls = getImageUrls(text);
                    text = extractImageUrls(text);

                    //Get Main-Image
                    try {
                        image = responseObject.getJSONObject("image");
                        imageString = image.getString("url");

                        imageUrls.add(imageString);
                    }catch (JSONException e){

                    }

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

                    ProjectViewModel temp = new ProjectViewModel(projectId, title, text, lat, lng, address, descriptionLocation, name, cycle, imageUrls, partnerArr, newsArr, blogsArr, sponsorArr, currentAmountDonationGoal, donationGoalDonationGoal, goal_description, allHosts, bankname,iban, bic, allMilestone, eventArr);
                    project = temp;
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
        this.requestQueue.add(objectRequest);
    }

    public ArrayList<EventViewModel> loadEvents(ArrayList<Integer> eventId){
        ArrayList <EventViewModel> events = new ArrayList<EventViewModel>();
        for(int i = 0; i < eventId.size(); i++) {
            String URL = "https://weitblicker.org/rest/events/" + eventId.get(i);

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
            this.requestQueue.add(objectRequest);
        }
        return events;
    }

    public ArrayList<SponsorViewModel> loadSponsor(ArrayList<Integer> sponsorenId){
        ArrayList <SponsorViewModel> sponsoren = new ArrayList<SponsorViewModel>();

        for(int i = 0; i < sponsorenId.size(); i++){
            String url = "https://weitblicker.org/rest/cycle/donations/" + sponsorenId.get(i);

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
            this.requestQueue.add(objectRequest);
        }
        return sponsoren;
    }

    public ArrayList<BlogEntryViewModel> loadBlog(ArrayList<Integer> blogsId){

        ArrayList<BlogEntryViewModel> blogs = new ArrayList<BlogEntryViewModel>();
        // Talk to Rest API
        for(int i = 0; i < blogsId.size(); i++){
            String url = "https://weitblicker.org/rest/blog/" + blogsId.get(i);

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
                    JSONObject author = null;
                    JSONObject hosts = null;
                    JSONObject host = null;
                    ArrayList<String> allHosts = new ArrayList<String>();

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
                        String location = null;

                        hosts = responseObject.getJSONObject("host");
                        allHosts.add(hosts.getString("city"));
                        location = responseObject.getString("location");

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
                        BlogEntryViewModel tempBLog = new BlogEntryViewModel(blogId, title, text, teaser, published, imageUrls, name, profilPic, allHosts, location);

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
            this.requestQueue.add(objectRequest);
        }
        return blogs;
    }

    public ArrayList<NewsViewModel> loadNews(ArrayList<Integer> newsId){

        ArrayList<NewsViewModel> news = new ArrayList<NewsViewModel>();
        // Talk to Rest API
        for(int i = 0; i < newsId.size(); i++){
            String url = "https://weitblicker.org/rest/news/" + newsId.get(i);

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
                    JSONArray hosts = null;
                    JSONObject host = null;
                    ArrayList<String> allHosts = new ArrayList<String>();

                    try {
                        Integer newsId = responseObject.getInt("id");
                        String title = responseObject.getString("title");
                        String text = responseObject.getString("text");
                        String date = responseObject.getString("published");

                        author = responseObject.getJSONObject("author");
                        String name = author.getString("name");
                        String profilPic = author.getString("image");



                        host = responseObject.getJSONObject("host");
                        allHosts.add(host.getString("city"));

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

                        NewsViewModel temp = new NewsViewModel(newsId, title, text, teaser, date, imageUrls, name, profilPic, allHosts);

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
            this.requestQueue.add(objectRequest);
        }
        return news;
    }

    private void checkDefault(){
        SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        if(settings.contains("projectid")){
            defaultproject = settings.getString("projectname", null);
            projectID = settings.getInt("projectid", -1);
            lat = settings.getFloat("lat", -1);
            lng = settings.getFloat("lng", -1);
        }else{
            lat = 0;
            lng = 0;
            defaultproject = "Kein Projekt ausgewählt. Bitte wähle ein Projekt aus.";
        }
    }

    private void setMarker(){
        LatLng location = new LatLng( this.lat, this.lng);
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(8.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setMarker();
    }

    private void setUpMapIfNeeded() {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
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