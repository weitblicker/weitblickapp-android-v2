package com.example.weitblickapp_android.ui.location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.login.Login_Activity;
import com.example.weitblickapp_android.ui.project.ProjectCycleListFragment;
import com.example.weitblickapp_android.ui.project.ProjectListFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapOverviewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location currentLocation;
    private SessionManager session;
    SharedPreferences defaultProjects;
    String PREF_NAME = "DefaultProject";
    String defaultproject;
    int projectID;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        defaultProjects = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = defaultProjects.edit();

        checkDefault();

        TextView defProject = root.findViewById(R.id.defaultProject);
        defProject.setText(defaultproject);

        session = new SessionManager(getActivity().getApplicationContext());

        ImageButton changeDef = root.findViewById(R.id.changeDefault);
        changeDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectCycleListFragment fragment = new ProjectCycleListFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
            }
        });

        ImageView img = root.findViewById(R.id.play);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!session.isLoggedIn()){
                    Intent redirect=new Intent(getActivity(), Login_Activity.class);
                    getActivity().startActivity(redirect);
                }
                else{
                    MapFragment fragment = new MapFragment(projectID);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                }
            }
        });
        return root;
    }

    private void checkDefault(){
        SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        if(settings.contains("projectid")){
            defaultproject = settings.getString("projectname", null);
            projectID = settings.getInt("projectid", -1);
        }else{
            defaultproject = "Kein Projekt ausgewählt. Bitte wähle ein Projekt aus!";
        }

        Toast.makeText(getContext(), Integer.toString(settings.getInt("projectid", -1)),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    private void setUpMapIfNeeded() {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
    }

}