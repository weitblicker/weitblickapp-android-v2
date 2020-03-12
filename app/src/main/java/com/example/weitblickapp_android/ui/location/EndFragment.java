package com.example.weitblickapp_android.ui.location;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.example.weitblickapp_android.ui.project.sponsor.SponsorAdapter;
import com.example.weitblickapp_android.ui.project.sponsor.SponsorViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EndFragment extends Fragment {

    ArrayList<SponsorModel> sponsors = new ArrayList<SponsorModel>();
    String distanceTotal;
    String eurosTotal;
    String projectName;
    SharedPreferences settings = null;
    private SponsorAdapter adapter;
    ArrayList<SponsorViewModel> sponsoren = new ArrayList<SponsorViewModel>();
    ArrayList<String> imageUrls = new ArrayList<String>();


    private ProjectViewModel project;

     EndFragment(Tour currentTour, ProjectViewModel project){
        this.project = project;
        this.distanceTotal = String.format("%.2f", currentTour.getDistanceTotal()).concat(" km");
        this.eurosTotal = String.format("%.2f", currentTour.getEurosTotal()).concat(" €");
        //this.projectName = project.getName();
     }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get DefaultProject from UserDevice
        settings = getContext().getApplicationContext().getSharedPreferences("DefaultProject", 0);
        int id = settings.getInt("projectid", 0);
        this.projectName = settings.getString("projectname", "");
        if(id != 0) loadProject(id);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_end, container, false);

        //set data
        TextView distanceText = root.findViewById(R.id.distance);
        distanceText.setText(distanceTotal);

        TextView eurosText = root.findViewById(R.id.donation);
        eurosText.setText(eurosTotal);

        TextView projectNameText = root.findViewById(R.id.project);
        projectNameText.setText(projectName);

        TextView partner = (TextView) root.findViewById(R.id.partner);
        TextView location = (TextView) root.findViewById(R.id.location);
        ImageView projectPic  = (ImageView) root.findViewById(R.id.imageView21);

        //load Images from Project
        String url = "https://weitblicker.org";
        if(project.getImageUrls().size() > 0){
            url = url.concat(project.getImageUrls().get(0));
        }
        Picasso.get().load(url).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(projectPic);

        location.setText(settings.getString("location", ""));
        partner.setText(settings.getString("hosts", ""));

        //list of all sponsors
        ListView listview = (ListView) root.findViewById(R.id.list);
        adapter = new SponsorAdapter(getActivity(), sponsoren, getFragmentManager());
        listview.setAdapter(adapter);

        return root;
    }

    public void loadProject(int id){
        ArrayList <ProjectViewModel> projects = new ArrayList<ProjectViewModel>();
        // Talk to Rest API
            String URL = "https://weitblicker.org/rest/projects/" + id;

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    //Save Data into Model
                    JSONObject cycleObject = null;
                    ArrayList<Integer> sponsorenid = new ArrayList<Integer>();
                    JSONArray donations = null;
                    JSONObject donation = null;
                    JSONObject image = null;
                    JSONArray images = null;
                    try {
                        String text = responseObject.getString("description");

                        imageUrls = getImageUrls(text);

                        //load Images
                        try {
                            images = responseObject.getJSONArray("photos");
                            for (int x = 0; x < images.length(); x++) {
                                image = images.getJSONObject(x);
                                String url = image.getString("url");
                                imageUrls.add(url);
                            }

                        }catch(JSONException e){

                        }
                        //load Donation
                        cycleObject = responseObject.getJSONObject("cycle");
                        donations = cycleObject.getJSONArray("donations");
                        for (int y = 0; y < donations.length(); y++) {
                            donation = donations.getJSONObject(y);
                            sponsorenid.add(donation.getInt("id"));
                        }
                        if (donations.length() > 0) {
                            loadSponsors(sponsorenid);
                        }
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

    public void loadSponsors(ArrayList<Integer> sponsorenId) {

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
                            //load datas
                            partner = responseObject.getJSONObject("partner");
                            String name =  partner.getString("name");
                            String desc = partner.getString("description");
                            String logo = partner.getString("logo");
                            String rateProKm = responseObject.getString("rate_euro_km");
                            String goal_amount_Sponsor = responseObject.getString("goal_amount");
                            String address = partner.getString("link");

                            temp = new SponsorViewModel(name, desc, address, logo, rateProKm, goal_amount_Sponsor);
                            sponsoren.add(temp);
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
    }


    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
       // loadEnd();
        setStaticSponsor();
    }

    public void setStaticSponsor(){
       // EndViewModel temp = new EndViewModel(1,"Weihnaachtsmann & CO. KG", "Baue eine Schule in Afrika");
       // end.add(temp);
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            if(view == null){
                view = getLayoutInflater().inflate(R.layout.fragment_end_list,null);

                TextView textView_sponsor = (TextView)view.findViewById(R.id.sponsor);
            }
            return view;
        }
    }
}