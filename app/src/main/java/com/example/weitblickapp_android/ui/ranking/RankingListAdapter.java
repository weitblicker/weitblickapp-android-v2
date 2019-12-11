package com.example.weitblickapp_android.ui.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.news.NewsDetailFragment;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RankingListAdapter extends ArrayAdapter<RankingViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<RankingViewModel> rankings;
    private FragmentManager fragManager;
    private int rank = 0;

    public RankingListAdapter(Context mContext, ArrayList<RankingViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_ranking_list, mDataSource);
        this.mContext = mContext;
        this.rankings = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
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

        rank = rank + 1;
        String r = Integer.toString(rank);
        ranks.setText(r);
        String val = Float.toString(ranking.getKm());
        value.setText(val);
        username.setText(ranking.getUsername());
        imageView.setImageResource(R.drawable.ic_launcher_background);

        return view;
    }
}
