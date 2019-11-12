package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BlogDetailFragment extends Fragment {

    String location;
    String title;
    String text;
    String date;

    public BlogDetailFragment() {
    }

    BlogDetailFragment(String location, String title, String text, String date){
        this.location=location;
        this.title=title;
        this.text=text;
        this.date=date;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_blog_detail, container, false);

        final ImageView imageView = root.findViewById(R.id.detail_image);
        imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
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
