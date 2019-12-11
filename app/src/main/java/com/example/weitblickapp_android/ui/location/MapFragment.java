package com.example.weitblickapp_android.ui.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.weitblickapp_android.ui.MyJsonArrayRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    static final String url = "https://new.weitblicker.org/rest/cycle/segment/";
    final private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private GoogleMap mMap;
    private Tour currentTour;
    private Location currentLocation;
    private Location lastLocation;
    private SessionManager session;

    private BroadcastReceiver locationSwitchStateReceiver;

    private boolean paused = false;
    private boolean load = false;
    private boolean gpsIsEnabled;
    private boolean projectFinished;

    //Segment-Information
    static private double km = 0;
    private String segmentStartTime;
    private String segmentEndTime;
    private String token;
    private int projectId;
    private int tourId;

    //Handler for GPS & Segment requests
    private final Handler handler = new Handler();
    private final Handler segmentHandler = new Handler();
    private final int fetchLocationDelay = 1000; //milliseconds
    private final int segmentSendDelay = 30000; //milliseconds

    private TextView distance;
    private TextView donation;

    private double betrag = 0.10;
    static private double don = 0;



    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    LocationManager locationManager;

    MapFragment(int projectId){
        this.projectId = projectId;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askGpsPermission();
        setUpGpsReceiver();
        registerGpsReceiver();
        initializeTour();
        startFetchLocation();
        sendRouteSegments();
    }

    private String getFormattedDate(){
       // formatter.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Date date = new Date();
        return formatter.format(date);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        this.gpsIsEnabled = isLocationEnabled();


        View root = inflater.inflate(R.layout.fragment_location, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        this.token = session.getKey();

        getAmountTours();

        final ImageView pause = root.findViewById(R.id.pause);
        ImageView stop = root.findViewById(R.id.stop);
        distance = root.findViewById(R.id.distance);
        donation = root.findViewById(R.id.donation);

        if (getActivity() != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused) {
                    pause.setImageResource(R.mipmap.ic_play_foreground);
                    paused = true;
                } else {
                    pause.setImageResource(R.mipmap.ic_pause);
                    paused = false;
                    segmentStartTime = MapFragment.this.getFormattedDate();
                    //sendSegment(url);
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndFragment fragment = new EndFragment(currentTour);
                FragmentTransaction ft = MapFragment.this.getChildFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
                MapFragment.this.sendSegment();
                paused = true;
            }
        });
        return root;
    }

    private void initializeTour(){
    currentTour = new Tour(projectId);
    }


    //Checks every Second if GPS is enabled, if so -> fetchLastLocation
    private void startFetchLocation() {
        handler.postDelayed(new Runnable() {
            public void run() {
                if(!paused && gpsIsEnabled) {
                    fetchLastLocation();
                }
                handler.postDelayed(this, fetchLocationDelay);
            }
        }, fetchLocationDelay);
    }

    private void getCurrentLocation(){
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    lastLocation = location;
                    currentLocation = location;
                    checkKm();
                }
            }
        });
    }

    //Fetches last GPS-Location and calculates resulting km
    private void fetchLastLocation() {
        if (getContext() != null && getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    if (!load) {
                        setUpMapIfNeeded();
                    }
                }
            }
        });
        checkKm();
    }

    private void askGpsPermission(){
        if (getContext() != null && getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
        }
    }

    // Sends Segment every 10 Seconds
    private void sendRouteSegments() {
        segmentStartTime = getFormattedDate();

        segmentHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Send Segment here
                if ((!paused) && gpsIsEnabled) {
                    sendSegment();
                }
                segmentHandler.postDelayed(this, segmentSendDelay);
            }
        }, segmentSendDelay);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //LatLng latLng = new LatLng( -33.865143, 151.209900);
        this.mMap = googleMap;
        if (isLocationEnabled()) {
            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.setMyLocationEnabled(true);
            }
        }
    }


    private void checkKm() {
        if(paused == false){
            if (lastLocation != null) {
                double dis = currentLocation.distanceTo(lastLocation) / 1000;
                km += dis;
                don = (betrag * km) ;
                distance.setText((String.valueOf(Math.round(km * 100.00) / 100.00)) + " km");
                donation.setText((String.valueOf(Math.round(don * 100.00) / 100.00)) + " €");
            }
        }else{
            if (lastLocation != null) {
                double dis = currentLocation.distanceTo(lastLocation) / 1000;
                km += dis;
                don = (betrag * km) / 100;
                distance.setText((String.valueOf(Math.round(km * 100.00) / 100.00)) + " km");
                donation.setText((String.valueOf(Math.round(don * 100.00) / 100.00)) + " €");
            }
            startFetchLocation();
        }
        lastLocation = currentLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResult.length < 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
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

    private boolean isLocationEnabled() {
         locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
         boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                 LocationManager.NETWORK_PROVIDER
         );
         if(!enabled){
             buildAlertMessageNoGps();
         }
        return enabled;
    }

    //Checks totalAmount of Tours and assigns totalAmount + 1 to next tour
    private void getAmountTours(){
            // Talk to Rest API

            String URL = "https://new.weitblicker.org/rest/cycle/tours/";

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("token", this.token);
            } catch (JSONException e) {
                Log.e("TourJsonException:", e.toString());
            }

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            MyJsonArrayRequest objectRequest = new MyJsonArrayRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    tourId = (response.length() + 1);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display Error Message
                    Log.e("TourError Response", error.toString());
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

    private void sendSegment() {

        segmentEndTime = getFormattedDate();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("start", segmentStartTime);
            jsonBody.put("end", segmentEndTime);
            jsonBody.put("distance", km);
            jsonBody.put("project", projectId);
            jsonBody.put("tour", tourId);
            jsonBody.put("token", token);
        } catch (JSONException e) {
            Log.e("SegmentJsonException:", e.toString());
        }

        Log.e("JSON:", jsonBody.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                double eurosTotal = 0;
                double kmTotal = 0;
                try {
                    projectFinished = response.getBoolean("finished");
                    eurosTotal = response.getDouble("euro");
                    kmTotal = response.getDouble("km");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                currentTour.setEurosTotal(eurosTotal);
                currentTour.setDistanceTotal(kmTotal);

                Log.e("Server Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Server Response onError", error.toString());
            }
        }) {
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

        segmentStartTime = segmentEndTime;
    }

    private void setUpGpsReceiver(){
        locationSwitchStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean isGpsEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

                    if (isGpsEnabled || isNetworkEnabled) {
                        gpsIsEnabled = true;
                    } else {
                        gpsIsEnabled = false;
                        buildAlertMessageNoGps();
                    }
                }
            }
        };
    }
    private void registerGpsReceiver(){
        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        getActivity().registerReceiver(locationSwitchStateReceiver, filter);
    }

    private void buildAlertMessageNoGps () {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Dein GPS scheint deaktiviert zu sein, möchtest du es aktivieren?")
                .setCancelable(true)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}



