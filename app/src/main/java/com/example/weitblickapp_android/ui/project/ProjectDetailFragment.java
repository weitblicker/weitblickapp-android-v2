package com.example.weitblickapp_android.ui.project;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.weitblickapp_android.ui.ImageSliderAdapter;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;


public class ProjectDetailFragment extends Fragment implements OnMapReadyCallback {

    static final String urlWeitblick = "https://new.weitblicker.org";


    String location;
    String title;
    String text;
    ArrayList <String> imageUrls = new ArrayList<String>();
    Boolean favorite = false;
    View root;
    private GoogleMap mMap;

    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;
    ViewPager mViewPager;

    public ProjectDetailFragment() {
    }

    public ProjectDetailFragment(ProjectViewModel project){
        this.title = project.getName();
        this.text = project.getDescription();
        //Concat imageUrls with Weitblick url and add values to "imageUrls"
        for(int i = 0; i < project.getImageUrls().size(); i++){
            this.imageUrls.add(i, urlWeitblick + project.getImageUrls().get(i));
        }
    }

    ProjectDetailFragment(String location, String title, String text){
        this.location=location;
        this.title=title;
        this.text=text;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_project_detail, container, false);

        //Set Image-Slider Adapter
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(getFragmentManager(), getActivity(), imageUrls);
        mViewPager.setAdapter(adapter);

        final ImageButton changeImage = (ImageButton) root.findViewById(R.id.heart);

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!favorite){
                    changeImage.setImageResource(R.drawable.ic_heart_filled);
                    favorite=true;
                }else{
                    changeImage.setImageResource(R.drawable.ic_heart_outline);
                    favorite=false;
                }

            }
        });

        ImageButton back = (ImageButton) root.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });


        final TextView locationTextView = root.findViewById(R.id.detail_location);
        locationTextView.setText(this.location);
        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);

        final TextView textTextView = root.findViewById(R.id.detail_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textTextView.setText(Html.fromHtml(this.text, Html.FROM_HTML_MODE_COMPACT));
        }

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        drawPie();

        return root;
    }

    public void drawPie(){
        AnimatedPieView mAnimatedPieView = root.findViewById(R.id.pieChart);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo(30, Color.parseColor("#ff9900"), "Noch zu sammelnde Spenden"))//Data (bean that implements the IPieInfo interface)
                .addData(new SimplePieInfo(18.0f, Color.parseColor("#d9e2ed"), "Gesammelte Spenden")).duration(2000);// draw pie animation duration
        // The following two sentences can be replace directly 'mAnimatedPieView.start (config); '
        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng( 52.2984, 8.0132);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
