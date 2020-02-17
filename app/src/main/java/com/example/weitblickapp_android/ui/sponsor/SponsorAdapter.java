package com.example.weitblickapp_android.ui.sponsor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

public class SponsorAdapter extends ArrayAdapter<SponsorViewModel> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SponsorViewModel> sponsor;
    private FragmentManager fragManager;
    String PREF_NAME = "DefaultSponsor";

    public SponsorAdapter(Context mContext, ArrayList<SponsorViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_sponsor, mDataSource);
        this.mContext = mContext;
        this.sponsor = mDataSource;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }


    @Override
    public int getCount() {
        return sponsor.size();
    }

    @Override
    public SponsorViewModel getItem(int position) {
        return sponsor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_sponsor, null);

        TextView description = (TextView) view.findViewById(R.id.description);
        TextView weblink = (TextView) view.findViewById(R.id.weblink);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView donation = (TextView) view.findViewById(R.id.donation);
        ImageView logo = (ImageView) view.findViewById(R.id.logo);

        final SponsorViewModel sponsor = (SponsorViewModel) getItem(position);


        //logo.setImageResource(sponsor.getLogo());
        description.setText(sponsor.getDescription());
        name.setText(sponsor.name);
        weblink.setText(sponsor.getWeblink());
        donation.setText("Dieser Spender spendet pro Kilometer " + sponsor.getRateProKm() + " €");

        return view;
    }
}

