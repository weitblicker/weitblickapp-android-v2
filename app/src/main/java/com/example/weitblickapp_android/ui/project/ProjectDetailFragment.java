package com.example.weitblickapp_android.ui.project;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ImageSliderAdapter;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListAdapter;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListAdapterShort;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryViewModel;
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.event.EventShortAdapter;
import com.example.weitblickapp_android.ui.event.EventViewModel;
import com.example.weitblickapp_android.ui.milenstone.MilenstoneListAdapter;
import com.example.weitblickapp_android.ui.milenstone.MilenstoneViewModel;
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


    String location;
    String title;
    String text;
    float lng;
    float lat;
    float current_amount;
    float goal_amount;
    String goalDescription;
    CycleViewModel cycle;
    ArrayList <String> imageUrls = new ArrayList<String>();
    ArrayList <NewsViewModel> newsId = new ArrayList<NewsViewModel>();
    ArrayList <BlogEntryViewModel> blogId = new ArrayList<BlogEntryViewModel>();
    ArrayList <ProjectPartnerViewModel> partnerId = new ArrayList<ProjectPartnerViewModel>();
    ArrayList <SponsorViewModel> sponsorId = new ArrayList<SponsorViewModel>();
    ArrayList <String> hosts = new ArrayList<String>();
    Boolean favorite = false;
    View root;
    private GoogleMap mMap;

    private int statsID = 1;
    private int milenstoneID = 1;
    private int eventsID = 0;

    private static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    ArrayList<ProjectPartnerViewModel> partnerList = new ArrayList<ProjectPartnerViewModel>();
    ArrayList<SponsorViewModel> sponsorList = new ArrayList<SponsorViewModel>();
    ArrayList<MilenstoneViewModel> milenstoneList = new ArrayList<MilenstoneViewModel>();
    ArrayList<NewsViewModel> newsList = new ArrayList<NewsViewModel>();
    ArrayList<BlogEntryViewModel> blogList = new ArrayList<BlogEntryViewModel>();
    ArrayList<EventViewModel> eventList = new ArrayList<EventViewModel>();

    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;
    ViewPager mViewPager;

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    private int currentPage = 0;
    private Timer timer;

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
            this.imageUrls.add(i, urlWeitblick + project.getImageUrls().get(i));
        }
        this.hosts = project.getHosts();
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
        tabLayout.setupWithViewPager(mViewPager, true);

        //Initiate Runnable for automatic Image-Slide
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                Log.e("currentPage:", currentPage +"");
                Log.e("PAGECOUNT:", mViewPager.getAdapter().getCount() + "");
                if (currentPage == mViewPager.getAdapter().getCount()){
                    Log.e("LASTPAGE", "!!!");
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
            partner.setText(b.toString());
        }
        final TextView textTextView = root.findViewById(R.id.detail_text);
        final TextView currentNumber = root.findViewById(R.id.currentNumber);
        final TextView goalNumber = root.findViewById(R.id.goalnumber);
        final TextView goalDesc = root.findViewById(R.id.donationgoaldescription);


        final Markwon markwon = Markwon.create(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            markwon.setMarkdown(textTextView,this.text);
        }

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        final TextView goalTextView = root.findViewById(R.id.amount_goal_number);
        final TextView amountTextView = root.findViewById(R.id.amountNumber);

        //DonationGoal
        goalTextView.setText(goal_amount + " €");
        amountTextView.setText(current_amount + " €");
        goalDesc.setText(goalDescription);

        if( cycle != null){
            drawPie(true);
            //Sponsor
            ListView listViewSponsor = (ListView) root.findViewById(R.id.sponsorlist);
            SponsorAdapter adapterSponsor = new SponsorAdapter(getActivity(), sponsorList, getFragmentManager());
            listViewSponsor.setAdapter(adapterSponsor);
            if(sponsorId != null){
                for(int i = 0; i < sponsorId.size(); i++){
                    sponsorList.add(sponsorId.get(i));
                }
            }
            setListViewHeightBasedOnChildren(listViewSponsor);

            //Stats
            currentNumber.setText(this.cycle.getCurrentAmount() + " €");
            goalNumber.setText(this.cycle.getCycleDonation() + " €");

        }else{
            ConstraintLayout stats = (ConstraintLayout) root.findViewById(R.id.statsContainer);
            stats.setVisibility(View.GONE);
            ConstraintLayout sponsor = (ConstraintLayout) root.findViewById(R.id.sponsorContainer);
            sponsor.setVisibility(View.GONE);
            drawPie(false);
        }
        if(newsId.size() != 0){
            ListView listNews = (ListView) root.findViewById(R.id.news);
            NewsShortAdapter adapterNews = new NewsShortAdapter(getActivity(), newsList, getFragmentManager());
            listNews.setAdapter(adapterNews);
            for(int i = 0; i < newsId.size(); i++){
                newsList.add(newsId.get(i));
            }
            setListViewHeightBasedOnChildren(listNews);
        }else{
            ConstraintLayout news = (ConstraintLayout) root.findViewById(R.id.newsContainer);
            news.setVisibility(View.GONE);
        }
        if(blogId.size() != 0){
            ListView listblog = (ListView) root.findViewById(R.id.blog);
            BlogEntryListAdapterShort adapterBlog = new BlogEntryListAdapterShort(getActivity(), blogList, getFragmentManager());
            listblog.setAdapter(adapterBlog);
            for(int i = 0; i< blogId.size(); i++){
                blogList.add(blogId.get(i));
            }
            setListViewHeightBasedOnChildren(listblog);
        }else{
            ConstraintLayout blog = (ConstraintLayout) root.findViewById(R.id.blogContainer);
            blog.setVisibility(View.GONE);
        }
        if(eventsID != 0){
            ListView listEvent = (ListView) root.findViewById(R.id.events);
            //EventViewModel test1 = new EventViewModel(1,"Heute wird ein guter Tag", "HAOHBJkcvheuöwoehiasclknv", "12h","20.02.20", "Osnabrück");
            //EventViewModel test2 = new EventViewModel(2, "Test"," jebuwfhilksbvwiu wlkhbvowubv ilw wilh" , "20h", "13.06.19", "Münster");
            EventShortAdapter adapterEvent = new EventShortAdapter(getActivity(), eventList, getFragmentManager());
            listEvent.setAdapter(adapterEvent);
            /*eventList.add(test1);
            eventList.add(test2);
            eventList.add(test2);*/
            setListViewHeightBasedOnChildren(listEvent);
        }else{
            ConstraintLayout event = (ConstraintLayout) root.findViewById(R.id.eventsContainer);
            event.setVisibility(View.GONE);
        }
        if(partnerId.size() != 0){
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
        if(milenstoneID != 0){
            ListView listMilenstone = (ListView) root.findViewById(R.id.milenstone);
            MilenstoneViewModel test1 = new MilenstoneViewModel("Test1","Heute", "HAOHBJkcvheuöwoehiasclknv jebuwfhilksbvwiu wlkhbvowubv ilw wilh");
            MilenstoneViewModel test2 = new MilenstoneViewModel("Test2", "13.06.19","oijtghbjklihoguzibhjoiguöizfltzfuzlgiuhöuigzlfutvghbilgzflzuflizfgzuffzlflzzugzglluuucgvjhbglzfvguhzfugvhizfucgvjhftcgukvhftucgvuftckhvjuftckhvjufchvjufchvjuftcvufztcgvuzftcgvuzftgvuzftcgvzuftgvuzlufgvhzufgvhvghgzfuvgjhb jebuwfhilksbvwiu wlkhbvowubv ilw wilh");
            MilenstoneListAdapter adapterMilenstone = new MilenstoneListAdapter(getActivity(), milenstoneList, getFragmentManager());
            listMilenstone.setAdapter(adapterMilenstone);
            milenstoneList.add(test2);
            milenstoneList.add(test2);
            milenstoneList.add(test2);
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

    public int  loadHeight(ListView list){
        ListAdapter LvAdapter = list.getAdapter();
        int listviewElementsheight = 0;
        for (int i = 0; i < LvAdapter.getCount(); i++) {
            View mView = LvAdapter.getView(i, null, list);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            listviewElementsheight += mView.getMeasuredHeight();
        }
        listviewElementsheight += list.getDividerHeight()*3;
        return listviewElementsheight;
    }


    public void drawPie(boolean draw){
        AnimatedPieView mAnimatedPieView = root.findViewById(R.id.pieChart);
        if(draw == true){
            float current = (100 / goal_amount) * current_amount;
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
        mMap.addMarker(new MarkerOptions().position(location).title(this.location).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_location)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
