package com.example.weitblickapp_android.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventDetailFragment extends Fragment implements OnMapReadyCallback {
    String location;
    int location_id;
    String title;
    String date;
    String text;

    private GoogleMap mMap;
    SupportMapFragment mapFrag;

    EventDetailFragment(EventViewModel event){
        this.location_id = event.getLocation();
        this.title = event.getTitle();
        this.text = event.getText();
        this.date = event.getEventStartDate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(2342134,657543);
        mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_location)));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);

        final ImageView imageView = root.findViewById(R.id.detail_image);
        imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
        final TextView locationTextView = root.findViewById(R.id.detail_location);
        locationTextView.setText(this.location);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        textTextView.setText(this.text);
        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        dateTextView.setText(this.date);

        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        ImageButton back = (ImageButton) root.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return root;
    }
}
