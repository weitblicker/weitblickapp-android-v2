package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.event.EventViewModel;
import com.example.weitblickapp_android.ui.milenstone.MilenstoneViewModel;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerViewModel;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
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

public class RegisterFragment extends ListFragment implements AbsListView.OnScrollListener {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<BlogEntryViewModel> blogEntries = new ArrayList<BlogEntryViewModel>();
    private BlogEntryListAdapter adapter;

    private String lastItemDate;
    private String lastItemDateCheck = "";
    int limitLoadedBlogs = 20;
    private String url = "https://weitblicker.org/rest/blog?limit=5";



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBlogs(url);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Blog");

        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        adapter = new BlogEntryListAdapter(getActivity(), blogEntries, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        // Button detail = (Button) v.findViewById(R.id.news_more_btn);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentTransaction replace = ft.replace(R.id.fragment_container, new BlogDetailFragment(blogEntries.get(position)));
                ft.commit();
            }
        });
    }
    public void loadBlogs(String URL){

        // Talk to Rest API

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();
                //Parse the JSON response array by iterating over it
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;
                    JSONObject imageObject = null;
                    JSONObject galleryObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    JSONArray images = null;
                    JSONObject author = null;
                    JSONObject hosts = null;
                    JSONObject host = null;
                    ArrayList<String> allHosts = new ArrayList<String>();
                    ArrayList<ProjectViewModel> projectArr = new ArrayList<ProjectViewModel>();
                    ArrayList<Integer> projectIds = new ArrayList<Integer>();


                    try {
                        responseObject = response.getJSONObject(i);
                        Integer blogId = responseObject.getInt("id");
                        String title = responseObject.getString("title");
                        String text = responseObject.getString("text");
                        String teaser = responseObject.getString("teaser");
                        String location = responseObject.getString("location");

                        try {
                            String projectID = responseObject.getString("project");
                            if(!projectID.contentEquals("null")){
                                projectIds.add(Integer.parseInt(projectID));
                            }
                            projectArr = loadProjects(projectIds);
                        }catch(JSONException e){

                        }

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
                        hosts = responseObject.getJSONObject("host");
                        allHosts.add(hosts.getString("city"));

                        author = responseObject.getJSONObject("author");
                        String name = author.getString("name");
                        String profilPic = author.getString("image");


                        String date = responseObject.getString("published");
                        try{
                            Date ItemDate = formatterRead.parse(date);
                            lastItemDate = formatterRead.format(ItemDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        BlogEntryViewModel temp = new BlogEntryViewModel(blogId, title, text, teaser,date, imageUrls, name, profilPic, allHosts, location, projectArr);

                        blogEntries.add(temp);
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

    public ArrayList<ProjectViewModel> loadProjects(ArrayList<Integer> id){
        ArrayList <ProjectViewModel> projects = new ArrayList<ProjectViewModel>();
        // Talk to Rest API
        for(int i = 0; i < id.size(); i++) {
            String URL = "https://weitblicker.org/rest/projects/" + id.get(i);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    //Save Data into Model
                    JSONObject locationObject = null;
                    JSONArray partnerJSONObject = null;
                    JSONObject partnerObject = null;
                    JSONObject cycleObject = null;
                    JSONObject image = null;
                    ArrayList<String> imageUrls = new ArrayList<String>();
                    ArrayList<Integer> sponsorenid = new ArrayList<Integer>();
                    ArrayList<BlogEntryViewModel> blogsArr = new ArrayList<BlogEntryViewModel>();
                    ArrayList<EventViewModel> eventArr = new ArrayList<EventViewModel>();
                    ArrayList<NewsViewModel> newsArr = new ArrayList<NewsViewModel>();
                    ArrayList<ProjectPartnerViewModel> partnerArr = new ArrayList<ProjectPartnerViewModel>();
                    ArrayList<SponsorViewModel> sponsorArr = new ArrayList<SponsorViewModel>();
                    JSONArray images = null;
                    JSONArray hosts = null;
                    CycleViewModel cycle = null;
                    JSONObject host = null;
                    ArrayList<String> allHosts = new ArrayList<String>();
                    JSONArray mileStoneArray = null;
                    JSONObject mileStone = null;
                    ArrayList<MilenstoneViewModel> allMilestone = new ArrayList<MilenstoneViewModel>();
                    JSONArray donations = null;
                    JSONObject donation = null;
                    ProjectViewModel temp = null;
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

                        try {
                            images = responseObject.getJSONArray("photos");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }

                        } catch (JSONException e) {

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
                        } catch (JSONException e) {

                        }

                        hosts = responseObject.getJSONArray("hosts");
                        String bankname = null;
                        String iban = null;
                        String bic = null;

                        for (int x = 0; x < hosts.length(); x++) {
                            host = hosts.getJSONObject(x);
                            allHosts.add(host.getString("city"));
                            /*if( host.getJSONObject("bank_account") !=  null){
                                bankAccount = host.getJSONObject("bank_account");
                                bankname = bankAccount.getString("account_holder");
                                iban = bankAccount.getString("iban");
                                bic = bankAccount.getString("bic");
                            }*/
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

                        cycleObject = responseObject.getJSONObject("cycle");

                        current_amount = cycleObject.getString("euro_sum");
                        cycle_donation = cycleObject.getString("euro_goal");
                        cyclist = cycleObject.getInt("cyclists");
                        km_sum = cycleObject.getString("km_sum");
                        donations = cycleObject.getJSONArray("donations");
                        for (int y = 0; y < donations.length(); y++) {
                            donation = donations.getJSONObject(y);
                            sponsorenid.add(donation.getInt("id"));
                        }
                        cycle = new CycleViewModel(current_amount, cycle_donation, cyclist, km_sum);
                        if (donations.length() > 0) {
                            sponsorArr = loadSponsor(sponsorenid);
                        }
                        String logo = null;
                        String description = null;
                        String weblink = null;
                        String partnerName = null;

                        for (int y = 0; y < partnerJSONObject.length(); y++) {
                            partnerObject = partnerJSONObject.getJSONObject(y);
                            logo = partnerObject.getString("logo");
                            description = partnerObject.getString("description");
                            partnerName = partnerObject.getString("name");
                            weblink = partnerObject.getString("link");
                            partnerArr.add(new ProjectPartnerViewModel(partnerName, description, weblink, logo));
                        }
                        text.trim();
                        temp = new ProjectViewModel(projectId, title, text, lat, lng, address, descriptionLocation, name, cycle, imageUrls, partnerArr, newsArr, blogsArr, sponsorArr, currentAmountDonationGoal, donationGoalDonationGoal, goal_description, allHosts, bankname, iban, bic, allMilestone, eventArr);
                        projects.add(temp);
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
        return projects;
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

    public ArrayList <String> getImageUrls(String text){
        //Find image-tag markdowns and extract
        ArrayList <String> imageUrls = new ArrayList<>();
        Matcher m = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\\"")
                .matcher(text);
        while (m.find()) {
            //Log.e("ImageUrl", m.group(2));
            imageUrls.add(m.group(2));
        }
        return imageUrls;
    }

    public String extractImageUrls(String text){
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)","");
        return text;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount > 0) {
            final int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem == totalItemCount && !lastItemDateCheck.equals(lastItemDate)) {
               url = url.concat(("&end=" + lastItemDate));
               loadBlogs(url);
               lastItemDateCheck = lastItemDate;
            }
        }
    }
}
