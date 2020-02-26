package com.example.weitblickapp_android.ui.news;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.utils.CircleTransform;
import com.example.weitblickapp_android.ui.utils.ImageSliderAdapter;
import com.example.weitblickapp_android.ui.project.ProjectAdapterShort;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.noties.markwon.Markwon;

public class NewsDetailFragment extends Fragment {
    static final String urlWeitblick = "https://weitblicker.org";

    private String location;
    private String title;
    private String text;
    private String date;
    String name;
    String picture;
    ArrayList<String> hosts = new ArrayList<String>();
    private ArrayList<String> imageUrls = new ArrayList<String>();
    private ViewPager mViewPager;
    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;
    ArrayList<ProjectViewModel> projectArr = new ArrayList<ProjectViewModel>();
    ArrayList<ProjectViewModel> projectList = new ArrayList<ProjectViewModel>();


    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 6000; // time in milliseconds between successive task executions.

    private int currentPage = 0;
    private Timer timer = null;

    public NewsDetailFragment(NewsViewModel article){
        this.title = article.getTitle();
        this.text = article.getText();
        this.date = article.getDate();
        this.name = article.getName();
        this.picture = urlWeitblick.concat(article.getImage());
        this.hosts = article.getHosts();
        //Concat imageUrls with Weitblick url and add values to "imageUrls"
        this.projectArr = article.getProject();

        if(article.getImageUrls().isEmpty()){
            //Add Default-Url so we can instantiate right default Picture in ImageSliderAdapter
            this.imageUrls.add("news_default");
        }else{
            for(int i = 0; i < article.getImageUrls().size(); i++){
                this.imageUrls.add(i, urlWeitblick + article.getImageUrls().get(i));
            }
        }
    }

    public NewsDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news_detail, container, false);

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
        }else{

        }

        final TextView titleTextView = root.findViewById(R.id.detail_title);
        final TextView authorName = root.findViewById(R.id.authorname);
        final ImageView authorImages = root.findViewById(R.id.authorpicture);
        titleTextView.setText(this.title);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        final TextView partner = root.findViewById(R.id.partner);

        final ImageView image = root.findViewById(R.id.image);



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

        if(this.picture.contains("null")){
                Picasso.get().load(R.mipmap.ic_launcher_foreground).fit().centerCrop()
                        .error(R.drawable.ic_wbcd_logo_standard_svg2).into(authorImages);
        }else{
            Picasso.get().load(this.picture).transform(new CircleTransform()).fit().centerCrop().
                    placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(authorImages);
        }

        authorName.setText(this.name);

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
            ProjectAdapterShort adapterProject = new ProjectAdapterShort(getActivity(), projectList, getFragmentManager());
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
    public void onPause() {
        if(timer != null)
        timer.cancel();
        super.onPause();
    }
}
