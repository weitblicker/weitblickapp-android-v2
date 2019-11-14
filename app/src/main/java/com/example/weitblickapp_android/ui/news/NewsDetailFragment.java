package com.example.weitblickapp_android.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

public class NewsDetailFragment extends Fragment {

    String location;
    String title;
    String text;
    String date;
    String imageUrl;

    public NewsDetailFragment() {
    }
    public NewsDetailFragment(NewsViewModel article){
        this.title = article.getTitle();
        this.text = article.getText();
        this.imageUrl = article.getImageUrl();
    }

    NewsDetailFragment(String location, String title, String text, String date){
        this.location=location;
        this.title=title;
        this.text=text;
        this.date=date;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        String weitblickUrl = "https://new.weitblicker.org";

        View root = inflater.inflate(R.layout.fragment_news_detail, container, false);

        final ImageView imageView = root.findViewById(R.id.detail_image);

        weitblickUrl = weitblickUrl.concat(imageUrl);

        Picasso.with(getContext()).load(weitblickUrl).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);
        
        final TextView locationTextView = root.findViewById(R.id.detail_location);
        locationTextView.setText(this.location);
        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        textTextView.setText(this.text);
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        dateTextView.setText(this.date);

        return root;
    }

}
