package com.example.weitblickapp_android.ui.location;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.login.Login_Activity;
import com.example.weitblickapp_android.ui.project.ProjectCycleListFragment;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapOverviewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location currentLocation;
    private SessionManager session;
    private SharedPreferences defaultProjects;
    private String PREF_NAME = "DefaultProject";
    private String defaultproject;
    private int projectID;
    private float lat;
    private float lng;
    private ProjectViewModel project = null;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        defaultProjects = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        checkDefault();

        Button defaultProject = root.findViewById(R.id.defaultProject);
        defaultProject.setText(defaultproject);

        session = new SessionManager(getActivity().getApplicationContext());

        defaultProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectCycleListFragment fragment = new ProjectCycleListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        ImageView img = root.findViewById(R.id.play);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!session.isLoggedIn()){
                    Intent redirect= new Intent(getActivity(), Login_Activity.class);
                    getActivity().startActivity(redirect);
                }
                else{
                    SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
                    if(settings.contains("projectid")) {
                        MapFragment fragment = new MapFragment(projectID);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment);
                        ft.commit();
                    }else{
                        Toast.makeText(getActivity(), "Du hast noch kein Projekt zum Spenden ausgewählt.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        setUpMapIfNeeded();
        return root;
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

}