package com.example.weitblickapp_android.ui.news;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ImageSliderAdapter;

import java.util.ArrayList;

public class NewsDetailFragment extends Fragment {
    static final String urlWeitblick = "https://new.weitblicker.org";

    String location;
    String title;
    String text;
    String date;
    ArrayList<String> imageUrls = new ArrayList<String>();
    ViewPager mViewPager;
    public ImageSliderAdapter imageSlider;
    private LayoutInflater mLayoutInflator;

    public NewsDetailFragment(NewsViewModel article){
        this.title = article.getTitle();
        this.text = article.getText();
        this.date = article.getDate();
        //Concat imageUrls with Weitblick url and add values to "imageUrls"
        for(int i = 0; i < article.getImageUrls().size(); i++){
            this.imageUrls.add(i, urlWeitblick + article.getImageUrls().get(i));
        }
    }

    public NewsDetailFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news_detail, container, false);

        //Set Image-Slider Adapter
        mViewPager = (ViewPager) root.findViewById(R.id.view_pager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(getFragmentManager(), getActivity(), imageUrls);
        mViewPager.setAdapter(adapter);

        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView textTextView = root.findViewById(R.id.detail_text);

        //Parse HTML in TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textTextView.setText(Html.fromHtml(this.text, Html.FROM_HTML_MODE_COMPACT));
        }
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        dateTextView.setText(this.date);

        return root;
    }

}
