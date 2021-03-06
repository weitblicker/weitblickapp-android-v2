package com.example.weitblickapp_android.ui.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.project.ProjectDetailFragment;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    static final String url = "https://weitblicker.org/rest/cycle/segment/";
    final private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // SessionManager to retrieve User-Data
    private SessionManager session;

    // Maps & Locations
    private GoogleMap mMap;
    public LocationService locationService;
    private Tour currentTour;
    private Location currentLocation;
    private Location lastLocation;

    // Receiver for Change of GPS & Location
    private BroadcastReceiver locationSwitchStateReceiver;
    private BroadcastReceiver locationUpdateReceiver;
    private BroadcastReceiver predictedLocationReceiver;

    // Boolean for change of behaviour
    private boolean paused = false;
    private boolean load = false;
    private boolean gpsIsEnabled;
    private boolean projectFinished = false;

    //Segment-Information
    static private double kmSegment;
    private String segmentStartTime;
    private String segmentEndTime;
    private String token;
    private int projectId;
    private int tourId = 0;
    private ProjectViewModel project;

    //Handler for GPS & Segment requests
    private final Handler handler = new Handler();
    private Runnable segmentSendRunnable;
    private final int segmentSendDelay = 30000; //milliseconds


    //TextViews to set values at runtime
    private TextView distance;
    private TextView donation;

    // Donation & kmTotal
    static private double don = 0;
    static private double kmTotal = 0;

    private RequestQueue requestQueue;
    private Context mContext;

    LocationManager locationManager;
    private static final int REQUEST_CODE = 101;

    public MapFragment(ProjectViewModel project){
        projectId = project.getId();
        this.project = project;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent locationService = new Intent(mContext, LocationService.class);
        getActivity().startService(locationService);
        getActivity().bindService(locationService, serviceConnection, Context.BIND_AUTO_CREATE);

        requestQueue = Volley.newRequestQueue(mContext);
        session = new SessionManager(getActivity().getApplicationContext());
        this.token = session.getKey();

        predictedLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Location tmp = intent.getParcelableExtra("location");

                Log.e("LocationReceived", "!!!");
                Log.e("Long ",  tmp.getLongitude()+ "...");
                Log.e("Lat ", tmp.getLatitude() + "...");

                if(!paused && gpsIsEnabled) {
                    if (currentLocation == null) {
                        currentLocation = tmp;

                    } else {
                        lastLocation = currentLocation;
                        currentLocation = tmp;

                        if(checkSpeedAndAcceleration()) {
                            calculateKm();
                        }
                    }

                    if (!load) {
                        setUpMapIfNeeded();
                    }
                }
            }
        };
/*
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                locationUpdateReceiver,
                new IntentFilter("LocationUpdated"));
*/
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                predictedLocationReceiver,
                new IntentFilter("PredictLocation"));


        askGpsPermission();
        setUpGpsStateReceiver();
        initializeTour();
        sendRouteSegments();

    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        //check if Location is Enabled
        this.gpsIsEnabled = isLocationEnabled();

        View root = inflater.inflate(R.layout.fragment_location, container, false);

        TextView partner = (TextView) root.findViewById(R.id.partner);
        TextView titel = (TextView) root.findViewById(R.id.titel);
        TextView location = (TextView) root.findViewById(R.id.location);

        //get DefaultProject
        SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences("DefaultProject", 0);
        location.setText(project.getAddress());
        titel.setText(project.getName());
        StringBuilder b = new StringBuilder();

        //Makes one String out of HostArray
        for(String s : project.getHosts()){
            b.append(s);
            b.append(" ");
        }

        //Makes character Uppercase
        StringBuilder B = new StringBuilder();
        for ( int i = 0; i < b.length(); i++ ) {
            char c = b.charAt( i );
            if(Character.isLowerCase(c)){
                B.append(Character.toUpperCase(c));
            }else{
                B.append(c);
            }
        }
        partner.setText(B);

        final ImageView pause = root.findViewById(R.id.pause);
        ImageView stop = root.findViewById(R.id.stop);
        ImageView projectDetail = root.findViewById(R.id.projectDetail);
        distance = root.findViewById(R.id.distance);
        donation = root.findViewById(R.id.donation);

        //set onClickListener for ProjectDetailPage
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

        //set onClickListener for PauseButtom
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stwich between pause and start Image
                if (!paused) {
                    pause.setImageResource(R.mipmap.icon_start_foreground);
                    paused = true;
                    //start send Segments and resetLocation
                    sendSegment();
                    resetLocations();
                } else {
                    pause.setImageResource(R.mipmap.icon_pause_foreground);
                    paused = false;
                    segmentStartTime = MapFragment.this.getFormattedDate();
                }
            }
        });

        //set onclickListener for stop Button
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
        this.segmentStartTime = getFormattedDate();
        createNewTour();
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

                    if(load && currentLocation != null) {
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                                15));
                    }
                    sendSegment();
                }
                handler.postDelayed(this, segmentSendDelay);
            }
        }, segmentSendDelay);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        //set Position to current User Position or DefaultPosition
        if (isLocationEnabled()) {
            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void calculateKm() {
        if(currentLocation != null && lastLocation != null) {
            double dis = currentLocation.distanceTo(lastLocation) / 1000;
            kmSegment += dis;
            kmTotal += dis;

            Log.e("km-Total ", kmTotal+"");
            Log.e("km-Segment ", kmSegment+"");

            don = currentTour.getEurosTotal();

            String distanceTotal = String.format("%.2f", kmTotal) + " km";
            String donationTotal = String.format("%.2f", don) + " €";


            distance.setText(distanceTotal);
            donation.setText(donationTotal);

            currentTour.setDistanceTotal(kmTotal);
        }
    }

    private boolean checkSpeedAndAcceleration(){
        if(currentLocation != null) {
            if (currentLocation.hasSpeed()) {
                float currentSpeedInKmh = Math.round((currentLocation.getSpeed() * 3.6f) * 100)/100;
                if (currentSpeedInKmh > 60.0f) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
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
            String URL = "https://weitblicker.org/rest/cycle/tours/new";

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

        }

        Log.e("JSON-SEGMENT", jsonBody.toString());

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


    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            String name = className.getClassName();

            if (name.endsWith("LocationService")) {
                locationService = ((LocationService.LocationServiceBinder) service).getService();

                locationService.startUpdatingLocation();


            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            if (className.getClassName().equals("LocationService")) {
                locationService.stopUpdatingLocation();
                locationService = null;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        sendSegment();
        handler.removeCallbacksAndMessages(null);
        mContext.unregisterReceiver(locationSwitchStateReceiver);

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(predictedLocationReceiver);
        Log.e("Receiver unregistered" , "!!");

        final Intent locationService = new Intent(mContext, LocationService.class);
        getActivity().stopService(locationService);
    }

}



