package com.example.weitblickapp_android.ui.stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

public class StatsListAdapter extends ArrayAdapter<StatsViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<StatsViewModel> stats;
    private FragmentManager fragManager;

    public StatsListAdapter(Context mContext, ArrayList<StatsViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_stats_list, mDataSource);
        this.mContext = mContext;
        this.stats = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }
    @Override
    public int getCount() {
        return stats.size();
    }

    @Override
    public StatsViewModel getItem(int position) {
        return stats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_stats_list, null);

        TextView textView_duration = (TextView) view.findViewById(R.id.duration);
        TextView textView_distance = (TextView) view.findViewById(R.id.distance);
        TextView textView_date = (TextView) view.findViewById(R.id.date);
        TextView textView_donation = (TextView) view.findViewById(R.id.donation);



        final StatsViewModel tour = (StatsViewModel) getItem(position);


        String donation = String.format("%.2f", tour.getDonation()).concat(" €");
        String distance = String.format("%.2f", tour.getDistance()).concat(" km");

        textView_donation.setText(donation);
        textView_distance.setText(distance);
        textView_date.setText(tour.getDate());
        textView_duration.setText(Integer.toString(tour.getDuration()).concat(" min"));
        return view;
    }
}