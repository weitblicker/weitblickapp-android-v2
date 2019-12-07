package com.example.weitblickapp_android.ui.location;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.data.Session.SessionManager;
import com.example.weitblickapp_android.ui.login.Login_Activity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapOverviewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location currentLocation;
    private SessionManager session;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        session = new SessionManager(getActivity().getApplicationContext());

        ImageView img = root.findViewById(R.id.play);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!session.isLoggedIn()){
                    Intent redirect=new Intent(getActivity(), Login_Activity.class);
                    getActivity().startActivity(redirect);
                }
                else{
                    MapFragment fragment = new MapFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                }
            }
        });
        return root;
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