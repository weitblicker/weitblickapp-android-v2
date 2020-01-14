package com.example.weitblickapp_android.ui.event;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.event.EventViewModel;
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

        String weitblickUrl = "https://new.weitblicker.org";

        view = mInflater.inflate(R.layout.fragment_event_short, null);

        TextView textView_title = (TextView) view.findViewById(R.id.title);
        TextView textView_date = (TextView) view.findViewById(R.id.date);
        TextView text = (TextView) view.findViewById(R.id.description);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView location = (TextView) view.findViewById(R.id.location);

        final EventViewModel event = (EventViewModel) getItem(position);

        //Set title for BlogEntries
        textView_title.setText(event.getName());
        text.setText(event.getText());
        time.setText(event.getTime());
        location.setText(event.getLocation());
        textView_date.setText(event.getDate());

        return view;
    }
}

