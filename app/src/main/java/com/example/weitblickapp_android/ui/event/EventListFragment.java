package com.example.weitblickapp_android.ui.event;

import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EventListFragment extends ListFragment implements AbsListView.OnScrollListener {
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<EventViewModel> events = new ArrayList<EventViewModel>();
    private EventListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadEvents();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        adapter = new EventListAdapter(getActivity(), events, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void loadEvents(){
        String URL = "https://weitblicker.org/rest/events/";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Parse the JSON response array by iterating over it & Save Data into Model
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;

                    ArrayList<String> imageUrls = new ArrayList<String>();
                    Location eventLocation;
                    JSONArray images = null;
                    JSONObject image = null;

                    JSONArray occurrences = null;


                    JSONObject locationObject = null;
                    EventLocation location = null;

                    String name;
                    String address;
                    double lat;
                    double lng;
                    String descriptionLocation;
                    String imageString;

                    JSONObject hostObject = null;
                    String hostName;

                    try {
                        //load data
                        responseObject = response.getJSONObject(i);
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
                        descriptionLocation = locationObject.getString("description");

                        hostObject = responseObject.getJSONObject("host");
                        hostName = hostObject.getString("city");

                        occurrences = responseObject.getJSONArray("occurrences");



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
                                if(!imageUrls.contains(url)) {
                                    imageUrls.add(url);
                                }
                            }

                        }catch(JSONException e){

                        }

                        description = extractImageUrls(description);
                        title = extractImageUrls(title);

                        location = new EventLocation(name, address, lat, lng, descriptionLocation);

                        //load occurrences
                        for(int y = 0; y < occurrences.length(); y++){
                            String occurrenceStart = occurrences.getJSONObject(y).getString("start");
                            String occurrenceEnd = occurrences.getJSONObject(y).getString("end");
                            if(!occurrenceStart.equals(startDate) && formatterRead.parse(occurrenceStart).after(new Date())) {
                                EventViewModel occurrence = new EventViewModel(eventId, title, description, occurrenceStart, occurrenceEnd, hostName, location, imageUrls);
                                events.add(occurrence);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        if(formatterRead.parse(startDate).after(new Date())) {
                            EventViewModel temp = new EventViewModel(eventId, title, description, startDate, endDate, hostName, location, imageUrls);
                            events.add(temp);
                            events.sort((o1,o2) -> o1.getEventDateStart().compareTo(o2.getEventDateStart()));
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
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

    public String extractImageUrls(String text){
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)","");
        return text;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}