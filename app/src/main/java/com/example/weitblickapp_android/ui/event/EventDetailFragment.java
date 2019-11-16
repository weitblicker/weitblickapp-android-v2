package com.example.weitblickapp_android.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;

public class EventDetailFragment extends Fragment {
    String location;
    String title;
    String date;
    String text;

    EventDetailFragment(String location, String title, String date, String text){
        this.location=location;
        this.title=title;
        this.date=date;
        this.text=text;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_detail, container, false);

        final ImageView imageView = root.findViewById(R.id.detail_image);
        imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
        final TextView locationTextView = root.findViewById(R.id.detail_location);
        locationTextView.setText(this.location);
        final TextView textTextView = root.findViewById(R.id.detail_text);
        textTextView.setText(this.text);
        final TextView titleTextView = root.findViewById(R.id.detail_title);
        titleTextView.setText(this.title);
        final TextView dateTextView = root.findViewById(R.id.detail_date);
        dateTextView.setText(this.date);

        return root;
    }
}
