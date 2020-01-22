package com.example.weitblickapp_android.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ImageSliderAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import io.noties.markwon.Markwon;

public class EventDetailFragment extends Fragment implements OnMapReadyCallback {
    private EventLocation location;
    private String title;
    private String date;
    private String text;
    private ArrayList<String> imageUrls = new ArrayList<String>();

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    private ViewPager mViewPager;

    EventDetailFragment(EventViewModel event){
        this.location = event.getLocation();
        this.title = event.getTitle();
        this.text = event.getText();
        this.date = event.getEventStartDate();
        this.imageUrls = event.getImageUrls();
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

        //Set Image-Slider Adapter
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(getFragmentManager(), getActivity(), imageUrls);
        mViewPager.setAdapter(adapter);

        final Markwon markwon = Markwon.create(getContext());

        final TextView titleTextView = root.findViewById(R.id.detail_title);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        final TextView locationTextView = root.findViewById(R.id.detail_location);


        locationTextView.setText(this.location.getAddress());

        //markdown description Text
        markwon.setMarkdown(textTextView,this.text);
        titleTextView.setText(this.title);

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
