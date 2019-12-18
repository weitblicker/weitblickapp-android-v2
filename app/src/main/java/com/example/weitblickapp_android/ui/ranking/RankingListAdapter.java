package com.example.weitblickapp_android.ui.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RankingListAdapter extends ArrayAdapter<RankingViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<RankingViewModel> rankings;
    private FragmentManager fragManager;
    static private boolean km_donation = true;

    public RankingListAdapter(Context mContext, ArrayList<RankingViewModel> mDataSource, FragmentManager fragManager, boolean km_donation) {
        super(mContext, R.layout.fragment_ranking_list, mDataSource);
        this.mContext = mContext;
        this.rankings = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
        this.km_donation = km_donation;
    }

    @Override
    public int getCount() {
        return rankings.size();
    }

    @Override
    public RankingViewModel getItem(int position) {
        return rankings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_ranking_list, null);

        TextView ranks = (TextView) view.findViewById(R.id.rank);
        TextView value = (TextView) view.findViewById(R.id.value);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageProfil);
        TextView username = (TextView) view.findViewById(R.id.username);

        final RankingViewModel ranking = (RankingViewModel) getItem(position);

        int rank = (position + 1);
        String r = Integer.toString(rank);
        ranks.setText(r);

        if(this.km_donation == false){
            String val = String.format("%.2f", ranking.getCycledKm()).concat(" km");
            value.setText(val);
        }else{
            String val = String.format("%.2f", ranking.getCycledDonation()).concat(" €");
            value.setText(val);
        }

        username.setText(ranking.getUsername());

        Picasso.get().load(ranking.getProfileImageUrl()).transform(new RoundedCornersTransform()).fit().centerCrop().
                placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(imageView);

        return view;
    }
}
