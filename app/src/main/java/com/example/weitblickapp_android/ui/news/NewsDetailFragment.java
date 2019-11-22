package com.example.weitblickapp_android.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

public class NewsDetailFragment extends Fragment {
    static final String urlWeitblick = "https://new.weitblicker.org";

    String location;
    String title;
    String text;
    String date;
    ArrayList<String> imageUrls = new ArrayList<String>();
    private ViewPager pagerAdaptor;
    private LayoutInflater mLayoutInflator;


    public NewsDetailFragment() {
    }
    public NewsDetailFragment(NewsViewModel article){
        this.title = article.getTitle();
        this.text = article.getText();
        this.date = article.getDate();
        //Concat imageUrls with Weitblick url and add values to "imageUrls"
        for(int i = 0; i < article.getImageUrls().size(); i++){
            this.imageUrls.add(i, urlWeitblick + article.getImageUrls().get(i));
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news_detail, container, false);
        this.pagerAdaptor = (ViewPager)root.findViewById(R.id.view_pager);

        ImageView imageView = root.findViewById(R.id.detail_image);

        this.pagerAdaptor.setAdapter(new ImageSliderAdapter(getFragmentManager(), getActivity(), imageUrls));

        /*final ImageView imageView = root.findViewById(R.id.detail_image);

        Picasso.get().load(imageUrls.get(2)).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);


        final TextView locationTextView = root.findViewById(R.id.detail_location);
        locationTextView.setText(this.location);

*/

        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        textTextView.setText(this.text);
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        dateTextView.setText(this.date);

        return root;
    }

}
