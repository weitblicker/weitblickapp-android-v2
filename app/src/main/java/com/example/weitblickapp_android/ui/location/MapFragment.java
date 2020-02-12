package com.example.weitblickapp_android.ui.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.weitblickapp_android.ui.project.ProjectAdapterShort;
import com.example.weitblickapp_android.ui.project.ProjectDetailFragment;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    static final String url = "https://new.weitblicker.org/rest/cycle/segment/";
    final private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private GoogleMap mMap;
    private Tour currentTour;
    private Location currentLocation;
    private Location lastLocation;
    private Location badLocation;
    private SessionManager session;

    //Receiver for Change of GPS-Turned ON/OFF
    private BroadcastReceiver locationSwitchStateReceiver;

    private boolean paused = false;
    private boolean load = false;
    private boolean gpsIsEnabled;
    private boolean projectFinished;

    //Segment-Information
    static private double kmSegment;
    static private double kmTotal;
    private String segmentStartTime;
    private String segmentEndTime;
    private String token;
    private int projectId;
    private int tourId = 0;
    private ProjectViewModel project;

    //Handler for GPS & Segment requests
    private final Handler handler = new Handler();
    private Runnable locationRunnable;
    private Runnable segmentSendRunnable;

    private final int fetchLocationDelay = 1000; //milliseconds
    private final int segmentSendDelay = 30000; //milliseconds

    private TextView distance;
    private TextView donation;

    //get ration for
    static private double don = 0;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private OnSuccessListener <Location> locationListener;
    private static final int REQUEST_CODE = 101;

    private RequestQueue requestQueue;
    private Context mContext;

    LocationManager locationManager;

    public MapFragment(ProjectViewModel project){
        this.project = project;
        ServicesHelper.addLocationServiceInterface(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askGpsPermission();
        setUpGpsStateReceiver();
        setUpLocationProvider();
        initializeTour();
        getCurrentLocation();
        startFetchLocation();
        sendRouteSegments();

        Log.e("ONCREATE", "!!!!");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.e("ONCREATEVIEW", "!!!!");


        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        this.gpsIsEnabled = isLocationEnabled();

        View root = inflater.inflate(R.layout.fragment_location, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        this.token = session.getKey();

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        createNewTour();

        TextView partner = (TextView) root.findViewById(R.id.partner);
        TextView titel = (TextView) root.findViewById(R.id.titel);
        TextView location = (TextView) root.findViewById(R.id.location);

        SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences("DefaultProject", 0);
        location.setText(settings.getString("location", ""));
        titel.setText(settings.getString("projectname", ""));
        partner.setText(settings.getString("hosts", ""));

        final ImageView pause = root.findViewById(R.id.pause);
        ImageView stop = root.findViewById(R.id.stop);
        ImageView projectDetail = root.findViewById(R.id.projectDetail);
        distance = root.findViewById(R.id.distance);
        donation = root.findViewById(R.id.donation);

        projectDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(project != null){
                    ProjectDetailFragment fragment = new ProjectDetailFragment(project);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused) {
                    pause.setImageResource(R.mipmap.ic_play_foreground);
                    paused = true;
                    sendSegment();
                    resetLocations();
                } else {
                    getCurrentLocation();
                    pause.setImageResource(R.mipmap.ic_pause_foreground);
                    paused = false;
                    segmentStartTime = MapFragment.this.getFormattedDate();

                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paused = true;
                EndFragment fragment = new EndFragment(currentTour, project);
                FragmentTransaction ft = MapFragment.this.getChildFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
                sendSegment();
                handler.removeCallbacksAndMessages(null);
                kmTotal = 0;
            }
        });
        return root;
    }



    private void setUpLocationProvider(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void resetLocations(){
        currentLocation = null;
        lastLocation = null;
    }
    private void resetTour(){
        kmSegment = 0.0;
        kmTotal = 0.0;
        don = 0.0;
    }

    private void initializeTour(){
        currentTour = new Tour(projectId);
    }


    //Checks every Second if GPS is enabled, if so -> fetchLastLocation
    private void startFetchLocation() {
        handler.postDelayed(locationRunnable = new Runnable() {
            public void run() {
                if(!paused && gpsIsEnabled) {
                    Log.e("FETCHING LOCATION", "!!!");
                    fetchLastLocation();
                }
                handler.postDelayed(this, fetchLocationDelay);
            }
        }, fetchLocationDelay);
    }

    private void getCurrentLocation(){
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(locationListener= new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
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
        task.addOnSuccessListener(locationListener = new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
               // Log.e("LOCATION", location.toString());
                if (location != null && location != currentLocation) {
                    Log.e("ACCURACY", location.getAccuracy() +"");
                    if (location.getAccuracy() < 20) {
                       // Log.e("LOCATION-LAT", location.getLatitude()+"");

                        lastLocation = currentLocation;
                        currentLocation = location;
                        currentTour.addLocationToTour(location);

                        Log.e("CURRENTLOCATION", currentLocation.getLatitude() +"");
                        Log.e("LASTLOCATION", lastLocation.getLatitude()+"");

                        if(checkSpeedAndAcceleration()) {
                            calculateKm();
                        }

                    }else{
                        badLocation = location;
                        currentLocation = null;
                    }
                    if (!load) {
                        setUpMapIfNeeded();
                    }
                }else{
                    RequestLocationUpdate();
                }
            }
        });

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

        handler.postDelayed(segmentSendRunnable = new Runnable() {
            @Override
            public void run() {
                //Send Segment here
                if ((!paused) && gpsIsEnabled) {
                    Log.e("SEGMENT SENT", "!!!");
                    sendSegment();
                }
                handler.postDelayed(this, segmentSendDelay);
            }
        }, segmentSendDelay);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        if (isLocationEnabled()) {
            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.setMyLocationEnabled(true);
            }else if(badLocation != null){
                LatLng latLng = new LatLng(badLocation.getLatitude(), badLocation.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void calculateKm() {
                double dis = currentLocation.distanceTo(lastLocation)/1000;
                Log.e("DISTANCE", dis + "");
                kmSegment += dis;
                kmTotal += dis;

                don = currentTour.getEurosTotal();

                String distanceTotal = (Math.round(kmTotal * 100.00) / 100.00) + (" km");
                String donationTotal = (Math.round(don * 100.00) / 100.00) + (" €");

                distance.setText(distanceTotal);
                donation.setText(donationTotal);

                currentTour.setDistanceTotal(kmTotal);
    }

    private boolean checkSpeedAndAcceleration(){
        if(currentLocation != null) {
            if (currentLocation.hasSpeed()) {
                float currentSpeedInKmh = Math.round((currentLocation.getSpeed() * 3.6f) * 100)/100;
                if (currentSpeedInKmh > 60.0f) {
                    Toast toast= Toast.makeText(mContext,"Geschwindigkeit: " + currentSpeedInKmh + " km/h" ,Toast. LENGTH_SHORT);
                    toast.show();
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
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
    private void createNewTour(){
            // Talk to Rest API
            Log.e("CREATETOUR", "!");
            String URL = "https://new.weitblicker.org/rest/cycle/tours/new";

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("RESPONSE",response.toString());
                    try {
                        tourId = response.getInt("tour_index");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TourError Response", error.toString());
                }
            }) {
                //Override getHeaders() to set Credentials for REST-Authentication
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Media-Type", "application/json");
                    headers.put("Authorization", "Token " + getToken());
                    return headers;
                }
            };
            this.requestQueue.add(objectRequest);
        }

    private void sendSegment() {

        segmentEndTime = getFormattedDate();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("start", segmentStartTime);
            jsonBody.put("end", segmentEndTime);
            jsonBody.put("distance", kmSegment);
            jsonBody.put("project", projectId);
            jsonBody.put("tour", tourId);
            jsonBody.put("token", token);
        } catch (JSONException e) {
            Log.e("SegmentJsonException:", e.toString());
        }

        Log.e("JSON:", jsonBody.toString());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    double eurosTotal = 0;
                    try {
                        projectFinished = response.getBoolean("finished");
                        eurosTotal = response.getDouble("euro");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    currentTour.setEurosTotal(eurosTotal);

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
                    headers.put("Media-Type", "application/json");
                    headers.put("Authorization", "Token " + getToken());
                    return headers;
                }
            };
            this.requestQueue.add(objectRequest);
            //Reset Km-Counter for Segment
            segmentStartTime = segmentEndTime;
            kmTotal += kmSegment;
            kmSegment = 0;
    }

    private void setUpGpsStateReceiver(){
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
        registerGpsReceiver();
    }
    private void registerGpsReceiver(){
        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        mContext.registerReceiver(locationSwitchStateReceiver, filter);
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
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        MapOverviewFragment fragment = new MapOverviewFragment();
                        ft.add(R.id.fragment_container, fragment);
                        ft.commit();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    public ArrayList <String> getImageUrls(String text){
        //Find image-tag markdowns and extract
        ArrayList <String> imageUrls = new ArrayList<>();
        Matcher m = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)")
                .matcher(text);
        while (m.find()) {
            imageUrls.add(m.group(2));
        }
        return imageUrls;
    }

    public String extractImageUrls(String text){
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)","");
        return text;
    }

    private String getToken(){
        return this.token;
    }


    //Gets actual Date of TODAY in Format: "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private String getFormattedDate(){
        Date date = new Date();
        return formatter.format(date);
    }

    //Prevent user from leaving fragment
    private boolean confirmBackPressedMessage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final boolean[] answer = new boolean[1];
        builder.setMessage("Wollen Sie die Tour wirklich beenden?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        answer[0] = true;
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        answer[0] = false;
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        return answer[0];
    }

    // FAKE LOCATION UPDATE REQUEST FOR FUSED LOCATION PROVIDER
    private void RequestLocationUpdate(){
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //Log.e("location-LAT", location.getLatitude()+"");
                    }
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("RESUMED", "!!!!!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("STOPPED", "!!!!!!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("PAUSED", "!!!!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("STARTED", "!!!!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendSegment();
        handler.removeCallbacksAndMessages(null);
        mContext.unregisterReceiver(locationSwitchStateReceiver);
        Log.e("DESTROYED", "!!!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("DETACHED", "!!!!!");
    }
}



