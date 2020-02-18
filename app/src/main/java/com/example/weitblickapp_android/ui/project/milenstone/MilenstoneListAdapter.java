package com.example.weitblickapp_android.ui.project.milenstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;

public class MilenstoneListAdapter extends ArrayAdapter<MilenstoneViewModel> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MilenstoneViewModel> milenstone;
    private FragmentManager fragManager;
    String PREF_NAME = "DefaultSponsor";

    public MilenstoneListAdapter(Context mContext, ArrayList<MilenstoneViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_milenstone, mDataSource);
        this.mContext = mContext;
        this.milenstone = mDataSource;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }


    @Override
    public int getCount() {
        return milenstone.size();
    }

    @Override
    public MilenstoneViewModel getItem(int position) {
        return milenstone.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_milenstone, null);

        TextView description = (TextView) view.findViewById(R.id.description);
        TextView title = (TextView) view.findViewById(R.id.titel);
        TextView date = (TextView) view.findViewById(R.id.date);
        ImageView finished = (ImageView) view.findViewById(R.id.logo);

        final MilenstoneViewModel mile = (MilenstoneViewModel) getItem(position);

        if(mile.reached){
            finished.setImageResource(R.drawable.icon_milestone_true);
        }else{
            finished.setImageResource(R.drawable.icon_milestone_false);
        }

        description.setText(mile.getDescription());
        title.setText(mile.getTitle());
        date.setText(mile.getDate());

        return view;
    }
}

