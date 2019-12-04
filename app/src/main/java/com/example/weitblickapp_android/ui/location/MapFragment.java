package com.example.weitblickapp_android.ui.location;

import android.Manifest;
import android.content.Context;
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

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.ranking.RankingFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location currentLocation;
    private Location lastLocation;
    private SessionManager session;
    private boolean paused = false;
    private boolean load = false;
    static private double km = 0;
    private Handler handler = new Handler();
    private int delay = 1000; //milliseconds
    private TextView distance;
    private TextView speedKmh;
    private TextView donation;
    private double betrag = 0.10;
    static private double don = 0;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);

        session = new SessionManager(getActivity().getApplicationContext());

        final ImageView pause = root.findViewById(R.id.pause);
        ImageView stop = root.findViewById(R.id.stop);
        distance = root.findViewById(R.id.distance);
        speedKmh = root.findViewById(R.id.speed);
        donation = root.findViewById(R.id.donation);

        if(getActivity() != null)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();
        startFetchLocation();
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
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
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
}
