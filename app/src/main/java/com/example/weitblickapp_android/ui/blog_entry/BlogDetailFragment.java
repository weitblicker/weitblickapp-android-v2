package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ImageSliderAdapter;
import com.example.weitblickapp_android.ui.project.ProjectListAdapter;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.noties.markwon.Markwon;

public class BlogDetailFragment extends Fragment {
    static final String urlWeitblick = "https://new.weitblicker.org";
    //String location;
    String title;
    String text;
    ArrayList<String> imageUrls = new ArrayList<>();
    String date;
    ArrayList<String> host = new ArrayList<String>();

    ViewPager mViewPager;
    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    private int currentPage = 0;
    private Timer timer;

    String name;
    String picture;
    String location;
    ArrayList<ProjectViewModel> projectArr = new ArrayList<ProjectViewModel>();
    ArrayList<ProjectViewModel> projectList = new ArrayList<ProjectViewModel>();


    public BlogDetailFragment(BlogEntryViewModel blogEntry) {
        this.title = blogEntry.getTitle();
        this.text = blogEntry.getText();
        this.date = blogEntry.getPublished();
        for(int i = 0; i < blogEntry.getImageUrls().size(); i++){
            this.imageUrls.add(i, urlWeitblick + blogEntry.getImageUrls().get(i));
        }
        this.date = blogEntry.getPublished();
        this.host = blogEntry.getHosts();
        this.name = blogEntry.getName();
        this.picture = blogEntry.getImage();
        this.location = blogEntry.getLocation();
        this.projectArr = blogEntry.getProject();
    }

    BlogDetailFragment(String title, String text, String date){
        //this.location=location;
        this.title=title;
        this.text=text;
        this.date=date;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_blog_detail, container, false);

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

        final TextView authorName = root.findViewById(R.id.authorname);
        final TextView location = root.findViewById(R.id.location);
        final ImageView authorImages = root.findViewById(R.id.authorpicture);

        location.setText(this.location);

        String url = urlWeitblick + this.picture;
        authorName.setText(this.name);
        Picasso.get().load(url).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(authorImages);

        final TextView partner = root.findViewById(R.id.partner);
        StringBuilder b = new StringBuilder();
        for(String s : this.host){
            b.append(s);
            b.append(" ");
        }
        partner.setText(b.toString());
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView textTextView = root.findViewById(R.id.detail_text);

        //Parse HTML in TextView
        final Markwon markwon = Markwon.create(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            markwon.setMarkdown(textTextView,this.text);
        }

        final TextView dateTextView = root.findViewById(R.id.detail_date);
        dateTextView.setText(this.date);
        ImageButton back = (ImageButton) root.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        if(projectArr != null && projectArr.size() != 0){
            ListView listProject = (ListView) root.findViewById(R.id.projectList);
            ProjectListAdapter adapterProject = new ProjectListAdapter(getActivity(), projectList, getFragmentManager());
            listProject.setAdapter(adapterProject);
            for(int i = 0; i < projectArr.size(); i++){
                projectList.add(projectArr.get(0));
            }
        }else{
            ConstraintLayout projectCon = (ConstraintLayout) root.findViewById(R.id.projectContainer);
            projectCon.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

}
