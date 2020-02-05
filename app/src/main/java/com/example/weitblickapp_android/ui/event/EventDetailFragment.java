package com.example.weitblickapp_android.ui.event;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.noties.markwon.Markwon;

public class EventDetailFragment extends Fragment implements OnMapReadyCallback {

    static final String urlWeitblick = "https://weitblicker.org";

    private EventLocation location;
    private String title;
    private String date;
    private String text;
    private String hostName;
    private ArrayList<String> imageUrls = new ArrayList<String>();

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    private ViewPager mViewPager;

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    private int currentPage = 0;
    private Timer timer = null;

    EventDetailFragment(EventViewModel event){
        this.location = event.getLocation();
        this.title = event.getTitle();
        this.text = event.getText();
        this.date = event.getEventStartDate();
        this.hostName = event.getHostName();

        for(int i = 0; i < event.getImageUrls().size(); i++){
            this.imageUrls.add(i, urlWeitblick.concat(event.getImageUrls().get(i)));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.e("LOCATIONVALUES:", this.location.getLat() +"  " +  this.location.getLng());
        LatLng location = new LatLng(this.location.getLat(),this.location.getLng());

        MarkerOptions marker = new MarkerOptions().position(location).title(this.location.getAddress());

        marker.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.logo_location));
        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);


        //Add False URL so ViewPager tries to instatiate Item which returns Default-Image in Error-Case
        if(imageUrls.isEmpty()){
            imageUrls.add("FALSEURL");
        }
        //Set Image-Slider Adapter
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(getFragmentManager(), getActivity(), imageUrls);
        mViewPager.setAdapter(adapter);

        //SET Tab-Indicator-Dots for ViewPager
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabDots);

        if(mViewPager.getAdapter().getCount() > 1){
            tabLayout.setupWithViewPager(mViewPager, true);
            //Initiate Runnable for automatic Image-Slide
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == mViewPager.getAdapter().getCount()){
                        currentPage = 0;
                    }
                    mViewPager.setCurrentItem(currentPage, true);
                    currentPage ++;
                }
            };
            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }

        final Markwon markwon = Markwon.create(getContext());

        final TextView titleTextView = root.findViewById(R.id.detail_title);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        final TextView locationTextView = root.findViewById(R.id.detail_location);
        final TextView hostTextView = root.findViewById(R.id.partner);

        //markdown description Text
        markwon.setMarkdown(textTextView,this.text);
        titleTextView.setText(this.title);

        dateTextView.setText(this.date);

        locationTextView.setText(this.location.getAddress());

        StringBuilder B = new StringBuilder();
        for ( int i = 0; i < hostName.length(); i++ ) {
            char c = hostName.charAt( i );
            if(Character.isLowerCase(c)){
                B.append(Character.toUpperCase(c));
            }else{
                B.append(c);
            }
        }
        hostTextView.setText(B.toString());

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

    @Override
    public void onDestroy() {
        if(timer != null)
        timer.cancel();
        super.onDestroy();
    }
}
