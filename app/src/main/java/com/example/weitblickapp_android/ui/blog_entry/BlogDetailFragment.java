package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Build;
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

import java.util.ArrayList;

import io.noties.markwon.Markwon;

public class BlogDetailFragment extends Fragment {
    static final String urlWeitblick = "https://new.weitblicker.org";
    //String location;
    String title;
    String text;
    ArrayList<String> imageUrls = new ArrayList<>();
    String date;

    ViewPager mViewPager;
    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;

    public BlogDetailFragment(BlogEntryViewModel blogEntry) {
        this.title = blogEntry.getTitle();
        this.text = blogEntry.getText();
        this.date = blogEntry.getPublished();
        for(int i = 0; i < blogEntry.getImageUrls().size(); i++){
            this.imageUrls.add(i, urlWeitblick + blogEntry.getImageUrls().get(i));
        }
        this.date = blogEntry.getPublished();
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


        return root;
    }

}
