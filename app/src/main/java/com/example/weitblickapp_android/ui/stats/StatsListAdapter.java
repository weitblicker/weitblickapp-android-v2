package com.example.weitblickapp_android.ui.stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;

import com.example.weitblickapp_android.R;

import com.example.weitblickapp_android.ui.news.NewsDetailFragment;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.squareup.picasso.Picasso;

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



        final StatsViewModel article = (StatsViewModel) getItem(position);

        textView_donation.setText("5,40 €");
        textView_distance.setText("25,34 km");
        textView_date.setText("24.12.2019");
        textView_duration.setText("52 min");
        return view;
    }
}