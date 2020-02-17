package com.example.weitblickapp_android.ui.project;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ImageSliderAdapter;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListAdapterShort;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListDetailFragment;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryViewModel;
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.event.EventListDetailFragment;
import com.example.weitblickapp_android.ui.event.EventShortAdapter;
import com.example.weitblickapp_android.ui.event.EventViewModel;
import com.example.weitblickapp_android.ui.location.MapOverviewFragment;
import com.example.weitblickapp_android.ui.milenstone.MilenstoneListAdapter;
import com.example.weitblickapp_android.ui.milenstone.MilenstoneViewModel;
import com.example.weitblickapp_android.ui.news.NewsListDetailFragment;
import com.example.weitblickapp_android.ui.news.NewsShortAdapter;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerAdapter;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerViewModel;
import com.example.weitblickapp_android.ui.sponsor.SponsorAdapter;
import com.example.weitblickapp_android.ui.sponsor.SponsorViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.noties.markwon.Markwon;


public class ProjectDetailFragment extends Fragment implements OnMapReadyCallback {

    static final String urlWeitblick = "https://new.weitblicker.org";
    String PREF_NAME = "DefaultProject";
    String location;
    String title;
    String text;
    double lng;
    double lat;
    static Bitmap smallMarker;
    String current_amount;
    String goal_amount;
    String goalDescription;
    CycleViewModel cycle;
    ArrayList <String> imageUrls = new ArrayList<String>();
    ArrayList <NewsViewModel> newsId = new ArrayList<NewsViewModel>();
    ArrayList <EventViewModel> eventId = new ArrayList<EventViewModel>();
    ArrayList <BlogEntryViewModel> blogId = new ArrayList<BlogEntryViewModel>();
    ArrayList <ProjectPartnerViewModel> partnerId = new ArrayList<ProjectPartnerViewModel>();
    ArrayList <SponsorViewModel> sponsorId = new ArrayList<SponsorViewModel>();
    ArrayList <String> hosts = new ArrayList<String>();
    Boolean favorite = false;
    View root;
    private GoogleMap mMap;
    String bankname;
    String bic;
    String iban;
    String descriptionLocation;
    int projectId;

    private static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    ArrayList<ProjectPartnerViewModel> partnerList = new ArrayList<ProjectPartnerViewModel>();
    ArrayList<SponsorViewModel> sponsorList = new ArrayList<SponsorViewModel>();
    ArrayList<MilenstoneViewModel> milenstoneList = new ArrayList<MilenstoneViewModel>();
    ArrayList<NewsViewModel> newsList = new ArrayList<NewsViewModel>();
    ArrayList<BlogEntryViewModel> blogList = new ArrayList<BlogEntryViewModel>();
    ArrayList<EventViewModel> eventList = new ArrayList<EventViewModel>();
    ArrayList<MilenstoneViewModel> mileList = new ArrayList<MilenstoneViewModel>();
    ListView listNews = null;

    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;
    ViewPager mViewPager;

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    private int currentPage = 0;
    private Timer timer = null;

    public ProjectDetailFragment() {
    }

    public ProjectDetailFragment(ProjectViewModel project){
        this.title = project.getName();
        this.text = project.getDescription();
        this.location = project.getAddress();
        this.lat = project.getLat();
        this.lng = project.getLng();
        this.current_amount = project.getCurrentAmount();
        this.goal_amount = project.getDonationGoal();
        this.cycle = project.getCycle();
        this.newsId = project.getNew_ids();
        this.blogId = project.getBlog_ids();
        this.partnerId = project.getPartner_ids();
        this.sponsorId = project.getSponsor_ids();
        this.goalDescription = project.getGoalDescription();
        //Concat imageUrls with Weitblick url and add values to "imageUrls"
        for(int i = 0; i < project.getImageUrls().size(); i++){
            if(!project.getImageUrls().get(i).contains("http://weitblicker.org")) {
                this.imageUrls.add(i, urlWeitblick + project.getImageUrls().get(i));
            }else{
                this.imageUrls.add(i, project.getImageUrls().get(i));
            }
            Log.e("IMAGEURLS:",  this.imageUrls.get(i) + ",");
        }
        this.hosts = project.getHosts();
        this.mileList = project.getMileStones();
        this.eventId = project.getEvent_ids();
        this.descriptionLocation = project.getDescriptionLocation();
        this.projectId = project.getId();
        this.bankname = project.getBankName();
        this.iban = project.getIban();
        this.bic = project.getBic();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_project_detail, container, false);


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
                    if (mViewPager.getCurrentItem() == (mViewPager.getAdapter().getCount()-1)){
                        mViewPager.setCurrentItem(0, true);
                    }else {
                        mViewPager.setCurrentItem((mViewPager.getCurrentItem()+1), true);
                    }
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


        //final ImageButton changeImage = (ImageButton) root.findViewById(R.id.heart);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);

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
        final TextView locationTextView2 = root.findViewById(R.id.detail_location2);
        locationTextView2.setText(this.location);
        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView partner = root.findViewById(R.id.partner);
        ImageView logo_icon = root.findViewById(R.id.logo_icon);
        if(hosts.isEmpty()){
            partner.setVisibility(View.GONE);
            logo_icon.setVisibility(View.GONE);
        }else{
            StringBuilder b = new StringBuilder();
            for(String s : hosts){
                b.append(s);
                b.append(" ");
            }
            StringBuilder B = new StringBuilder();
            for ( int i = 0; i < b.length(); i++ ) {
                char c = b.charAt( i );
                if(Character.isLowerCase(c)){
                    B.append(Character.toUpperCase(c));
                }else{
                    B.append(c);
                }
            }
            partner.setText(B.toString());
        }
        final TextView textTextView = root.findViewById(R.id.detail_text);
        final TextView currentNumber = root.findViewById(R.id.currentNumber);
        final TextView goalNumber = root.findViewById(R.id.goalnumber);
        final TextView goalDesc = root.findViewById(R.id.donationgoaldescription);
        final TextView bankName = root.findViewById(R.id.bankname);
        final TextView IBAN = root.findViewById(R.id.IBAN);
        final TextView BIC = root.findViewById(R.id.BIC);
        final TextView newsMore = root.findViewById(R.id.moreNews);
        final TextView blogsMore = root.findViewById(R.id.moreBlog);
        final TextView eventsMore = root.findViewById(R.id.moreEvents);
        final TextView goalTextView = root.findViewById(R.id.amount_goal_number);
        final TextView amountTextView = root.findViewById(R.id.amountNumber);
        final TextView bikeText = root.findViewById(R.id.bikeText2);
        final TextView locationDescription = root.findViewById(R.id.detail_location3);
        final Markwon markwon = Markwon.create(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            markwon.setMarkdown(textTextView,this.text);
        }

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        if(this.descriptionLocation.contains("null")){
            locationDescription.setVisibility(View.GONE);
        }else{
            locationDescription.setText(this.descriptionLocation);
        }
        ImageButton bike = root.findViewById(R.id.bike);
        ImageButton bike2 = root.findViewById(R.id.bike2);

        //DonationGoal
        if(this.goal_amount.contains("null") && this.current_amount.contains("null") && this.goalDescription.length() == 0 && this.bankname == null){
            ConstraintLayout goalDonation = (ConstraintLayout) root.findViewById(R.id.donationGoalContainer);
            goalDonation.setVisibility(View.GONE);
        }else{
            if(current_amount.contains("null")){
                TextView text = (TextView) root.findViewById(R.id.amount_goal_text);
                goalTextView.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }else{
                goalTextView.setText(current_amount + " €");
            }
            if(goal_amount.contains("null")){
                TextView text = (TextView) root.findViewById(R.id.amountText);
                text.setVisibility(View.GONE);
                amountTextView.setVisibility(View.GONE);
            }else{
                amountTextView.setText(goal_amount + " €");
            }
            if(goalDescription.length() == 0){
                goalDesc.setVisibility(View.GONE);
            }else{
                goalDesc.setText(goalDescription);
            }
            if(bankname == null){
                IBAN.setVisibility(View.GONE);
                bankName.setVisibility(View.GONE);
                BIC.setVisibility(View.GONE);
            }else{
                IBAN.setText(iban);
                bankName.setText(bankname);
                BIC.setText(bic);
            }
        }

        if(this.sponsorId.size() > 0){
            drawPie(true);
            //Sponsor
            sponsorList.clear();
            ListView listViewSponsor = (ListView) root.findViewById(R.id.sponsorlist);
            SponsorAdapter adapterSponsor = new SponsorAdapter(getActivity(), sponsorList, getFragmentManager());
            listViewSponsor.setAdapter(adapterSponsor);
            for(int i = 0; i < sponsorId.size(); i++){
                sponsorList.add(sponsorId.get(i));
            }
            setListViewHeightBasedOnChildren(listViewSponsor);
            double rateProKm = 0;
            for(int i = 0; i < sponsorId.size(); i++){
                rateProKm += Float.parseFloat(sponsorId.get(i).getRateProKm());
            }
            float currentRound = Math.round( rateProKm * 100);
            currentRound = currentRound / 100.0f;

            bikeText.setText("Für jeden mit dem Fahrrad gefahrenen Kilometer spenden unsere Sponsoren für das Projekt.");

            StringBuilder b = new StringBuilder();
            for(String s : hosts){
                b.append(s);
                b.append(" ");
            }
            StringBuilder B = new StringBuilder();
            for ( int i = 0; i < b.length(); i++ ) {
                char c = b.charAt( i );
                if(Character.isLowerCase(c)){
                    B.append(Character.toUpperCase(c));
                }else{
                    B.append(c);
                }
            }
            partner.setText(B.toString());

            bike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences("", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("projectid", projectId);
                    editor.putString("projectname", title);
                    editor.putFloat("lat", (long) lat);
                    editor.putFloat("lng", (long) lng);
                    editor.putString("hosts", B.toString());
                    editor.putString("location", location);
                    editor.commit();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new MapOverviewFragment());
                    ft.commit();
                }
            });
            bike2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences("", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("projectid", projectId);
                    editor.putString("projectname", title);
                    editor.putFloat("lat", (long) lat);
                    editor.putFloat("lng", (long) lng);
                    editor.putString("hosts", B.toString());
                    editor.putString("location", location);
                    editor.commit();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new MapOverviewFragment());
                    ft.commit();
                }
            });

            //Stats
            currentRound = Math.round( Float.parseFloat(this.cycle.getCurrentAmount()) *100);
            currentRound = currentRound / 100.0f;
            currentNumber.setText( currentRound + " €");
            currentRound = Math.round( Float.parseFloat(this.cycle.getCycleDonation()) *100);
            currentRound = currentRound / 100.0f;
            goalNumber.setText(currentRound + " €");
            TextView km = root.findViewById(R.id.reachedNumber);
            currentRound = Math.round( Float.parseFloat(this.cycle.getKm_sum()) *100);
            currentRound = currentRound / 100.0f;
            km.setText(currentRound + " km");
            TextView cyclist = root.findViewById(R.id.bikernumber);
            cyclist.setText(String.valueOf(cycle.getCyclist()));

        }else{
            ConstraintLayout stats = (ConstraintLayout) root.findViewById(R.id.statsContainer);
            stats.setVisibility(View.GONE);
            ConstraintLayout sponsor = (ConstraintLayout) root.findViewById(R.id.sponsorContainer);
            sponsor.setVisibility(View.GONE);
            drawPie(false);
            bike.setVisibility(View.GONE);
        }
        if(newsId.size() != 0){
            listNews = (ListView) root.findViewById(R.id.news);
            newsList.clear();
            NewsShortAdapter adapterNews = new NewsShortAdapter(getActivity(), newsList, getFragmentManager());
            listNews.setAdapter(adapterNews);
            if(newsId.size() <= 3){
                for(int i = 0; i < newsId.size(); i++){
                    newsList.add(newsId.get(i));
                }
                newsMore.setVisibility(View.GONE);
            }else{
                for(int i = 0; i < 3; i++){
                    newsList.add(newsId.get(i));
                }
                newsMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewsListDetailFragment fragment = new NewsListDetailFragment(newsId);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
            }
            setListViewHeightBasedOnChildren(listNews);
        }else{
            ConstraintLayout news = (ConstraintLayout) root.findViewById(R.id.newsContainer);
            news.setVisibility(View.GONE);
        }
        if(blogId.size() != 0){
            ListView listblog = (ListView) root.findViewById(R.id.blog);
            blogList.clear();
            BlogEntryListAdapterShort adapterBlog = new BlogEntryListAdapterShort(getActivity(), blogList, getFragmentManager());
            listblog.setAdapter(adapterBlog);
            if(blogId.size() <= 3){
                for(int i = 0; i < blogId.size(); i++){
                    blogList.add(blogId.get(i));
                }
                blogsMore.setVisibility(View.GONE);
            }else{
                for(int i = 0; i < 3; i++){
                    blogList.add(blogId.get(i));
                }
                blogsMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BlogEntryListDetailFragment fragment = new BlogEntryListDetailFragment(blogId);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
            }
            setListViewHeightBasedOnChildren(listblog);
        }else{
            ConstraintLayout blog = (ConstraintLayout) root.findViewById(R.id.blogContainer);
            blog.setVisibility(View.GONE);
        }
        if(eventId.size() != 0){
            ListView listEvent = (ListView) root.findViewById(R.id.events);
            eventList.clear();
            EventShortAdapter adapterEvent = new EventShortAdapter(getActivity(), eventList, getFragmentManager());
            listEvent.setAdapter(adapterEvent);
            if(eventId.size() <= 3){
                for(int i = 0; i < eventId.size(); i++){
                    eventList.add(eventId.get(i));
                }
                eventsMore.setVisibility(View.GONE);
            }else{
                for(int i = 0; i < 3; i++){
                    eventList.add(eventId.get(i));
                }
                eventsMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventListDetailFragment fragment = new EventListDetailFragment(eventId);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        FragmentTransaction replace = ft.replace(R.id.fragment_container, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
            }
            setListViewHeightBasedOnChildren(listEvent);
        }else{
            ConstraintLayout event = (ConstraintLayout) root.findViewById(R.id.eventsContainer);
            event.setVisibility(View.GONE);
        }
        if(partnerId.size() != 0){
            partnerList.clear();
            ListView listPartner = (ListView) root.findViewById(R.id.projectpartner);
            ProjectPartnerAdapter adapterPartner = new ProjectPartnerAdapter(getActivity(), partnerList, getFragmentManager());
            listPartner.setAdapter(adapterPartner);
            for(int i = 0; i < partnerId.size(); i++){
                partnerList.add(partnerId.get(i));
            }
            setListViewHeightBasedOnChildren(listPartner);
        }else{
            ConstraintLayout projectPartner = (ConstraintLayout) root.findViewById(R.id.projectPartnerContainer);
            projectPartner.setVisibility(View.GONE);
        }
        if(mileList.size() != 0){
            milenstoneList.clear();
            ListView listMilenstone = (ListView) root.findViewById(R.id.milenstone);
            MilenstoneListAdapter adapterMilenstone = new MilenstoneListAdapter(getActivity(), milenstoneList, getFragmentManager());
            listMilenstone.setAdapter(adapterMilenstone);
            for(int i = 0; i < mileList.size(); i++){
                milenstoneList.add(mileList.get(i));
            }
            setListViewHeightBasedOnChildren(listMilenstone);
        }else{
            ConstraintLayout mile = (ConstraintLayout) root.findViewById(R.id.milenstoneContainer);
            mile.setVisibility(View.GONE);
        }
        return root;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 350 * (listView.getResources().getDisplayMetrics().density);
                item.measure(
                        View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                int height = item.getMeasuredHeight();
                totalItemsHeight += height;
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }


    public void drawPie(boolean draw){
        AnimatedPieView mAnimatedPieView = root.findViewById(R.id.pieChart);
        if(draw == true){
            double current = (100 / Float.parseFloat(cycle.getCycleDonation())) * Float.parseFloat(cycle.getCurrentAmount());
            TextView procent = (TextView) root.findViewById(R.id.procent);
            current = Math.round(current * 100) / 100.0;
            procent.setText(current + " %");
            AnimatedPieViewConfig config = new AnimatedPieViewConfig();
            config.startAngle(-90)// Starting angle offset
                    .addData(new SimplePieInfo(100 - current, Color.parseColor("#ff9900"), "Noch zu sammelnde Spenden"))//Data (bean that implements the IPieInfo interface)
                    .addData(new SimplePieInfo(current, Color.parseColor("#d9e2ed"), "Gesammelte Spenden")).duration(2000);// draw pie animation duration
            // The following two sentences can be replace directly 'mAnimatedPieView.start (config); '
            mAnimatedPieView.applyConfig(config);
            mAnimatedPieView.start();
        }else{
            mAnimatedPieView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng( this.lat, this.lng);
        Bitmap bitmapdraw = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_marker_foreground);
        smallMarker = Bitmap.createScaledBitmap(bitmapdraw, 100, 100, false);
        mMap.addMarker(new MarkerOptions().position(location).title(this.location).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    @Override
    public void onDestroy() {
        if(timer != null)
        timer.cancel();
        super.onDestroy();
    }
}
