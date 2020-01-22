package com.example.weitblickapp_android.ui.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<EventViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<EventViewModel> events;
    private FragmentManager fragManager;

    public EventListAdapter(Context mContext, ArrayList<EventViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_event_short, mDataSource);
        this.mContext = mContext;
        this.events = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public EventViewModel getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        String weitblickUrl = "https://weitblicker.org";

        view = mInflater.inflate(R.layout.fragment_event_list,null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView_title = (TextView)view.findViewById(R.id.title);
        TextView textView_location = (TextView)view.findViewById(R.id.location);
        TextView textView_date = (TextView)view.findViewById(R.id.date);

        final EventViewModel event = (EventViewModel) getItem(position);


        if(event.getImageUrls().size()>0) {
            weitblickUrl = weitblickUrl.concat(event.getImageUrls().get(0));
        }

        Picasso.get().load(weitblickUrl).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);

        textView_title.setText(event.getTitle());
        textView_location.setText(event.getLocation().getAddress());
        textView_date.setText(event.getEventStartDate());


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragManager.beginTransaction();
                ft.replace(R.id.fragment_container, new EventDetailFragment(event));
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        return view;
    }
}

