package com.example.weitblickapp_android.ui.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    static final String url = "https://new.weitblicker.org/rest/cycle/segment/";

    private GoogleMap mMap;
    private Location currentLocation;
    private Location lastLocation;
    private SessionManager session;
    private String token;
    private boolean paused = false;
    private boolean load = false;
    static private double km = 0;
    private final Handler handler = new Handler();
    private final Handler segmentHandler = new Handler();
    private int delay = 1000; //milliseconds
    private int segmentSendDelay = 30000; //milliseconds
    private TextView distance;
    private TextView speedKmh;
    private TextView donation;
    private double betrag = 0.10;
    static private double don = 0;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchLastLocation();
        startFetchLocation();
        sendRouteSegments();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        this.token = session.getKey();

        final ImageView pause = root.findViewById(R.id.pause);
        ImageView stop = root.findViewById(R.id.stop);
        distance = root.findViewById(R.id.distance);
        speedKmh = root.findViewById(R.id.speed);
        donation = root.findViewById(R.id.donation);

        if(getActivity() != null)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!paused){
                    pause.setImageResource(R.mipmap.ic_play_foreground);
                    paused=true;
                    delay = 0;
                }else{
                    pause.setImageResource(R.mipmap.ic_pause);
                    paused=false;
                    delay = 1000;
                    startFetchLocation();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndFragment fragment = new EndFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                delay = 0;
                ft.commit();
               // sendSegment(url);
                paused = true;
            }
        });
        return root;
    }
    private void fetchLastLocation(){
        if(getContext() != null  && getActivity() != null){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    if(!load) {
                        setUpMapIfNeeded();
                    }
                }
            }
        });
        checkKm();
    }

    private void startFetchLocation(){
        handler.postDelayed(new Runnable(){
            public void run(){
                fetchLastLocation();
                if(delay != 0){
                    handler.postDelayed(this, 2000);
                }
            }
        }, 2000);
    }
    private void sendRouteSegments(){
        segmentHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //Send Segment here
                if(!paused) {
                    Log.e("SEGMENT-INFO", "SEGMENT-SENT with KM: " + km);
                    //sendSegment(url);
                }
                segmentHandler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //LatLng latLng = new LatLng( -33.865143, 151.209900);
        this.mMap = googleMap;
        if(isLocationEnabled()) {
            if(currentLocation != null){
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void checkKm(){
        if(lastLocation != null){
            double dis = currentLocation.distanceTo(lastLocation) / 1000;
            double speed = currentLocation.getSpeed();
            km += dis;
            don = (betrag * km) / 100;
            speedKmh.setText((String.valueOf(Math.round(speed * 10.00) / 10.00)) + "km/h");
            distance.setText((String.valueOf(Math.round(km * 100.00) / 100.00)) + " km");
            donation.setText((String.valueOf(Math.round(don * 100.00) / 100.00)) + " €");
        }
        lastLocation = currentLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult){
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResult.length<0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
            default:
                break;
        }
    }

    private void setUpMapIfNeeded() {
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        load = true;
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void sendSegment(String URL){

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("start", "2019-10-01T07:08");
            jsonBody.put("end", "2019-10-01T07:08");
            jsonBody.put("distance", km);
            jsonBody.put("project", "1");
            jsonBody.put("tour", "1");
            jsonBody.put("token", this.token);
        }catch(JSONException e) {
            Log.e("SegmentJsonException:", e.toString());
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("Server Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Server Response onError", error.toString());
            }
        }){
            //Override getHeaders() to set Credentials for REST-Authentication
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "surfer:hangloose";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Media-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }

    public class Segment{
        Date startDate;
        Date endDate;
        private double km;
        private int project_id;
        private int tour_id;

        public Segment(Date startDate, Date endDate, double km, int project_id, int tour_id) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.km = km;
            this.project_id = project_id;
            this.tour_id = tour_id;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public double getKm() {
            return km;
        }

        public void setKm(double km) {
            this.km = km;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public int getTour_id() {
            return tour_id;
        }

        public void setTour_id(int tour_id) {
            this.tour_id = tour_id;
        }
    }
}


