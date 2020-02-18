package com.example.weitblickapp_android.ui.project.partner;

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

public class ProjectPartnerAdapter extends ArrayAdapter<ProjectPartnerViewModel> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProjectPartnerViewModel> partner;
    private FragmentManager fragManager;
    String PREF_NAME = "DefaultSponsor";

    public ProjectPartnerAdapter(Context mContext, ArrayList<ProjectPartnerViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_projectpartner, mDataSource);
        this.mContext = mContext;
        this.partner = mDataSource;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }

    @Override
    public int getCount() {
        return partner.size();
    }

    @Override
    public ProjectPartnerViewModel getItem(int position) {
        return partner.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_projectpartner, null);

        TextView description = (TextView) view.findViewById(R.id.description);
        TextView weblink = (TextView) view.findViewById(R.id.weblink);
        TextView name = (TextView) view.findViewById(R.id.name);
        ImageView logo = (ImageView) view.findViewById(R.id.logo);

        final ProjectPartnerViewModel partner = (ProjectPartnerViewModel) getItem(position);

        description.setText(partner.getDescription());
        name.setText(partner.name);
        weblink.setText(partner.getWeblink());

        String weitblickUrl = "https://weitblicker.org";

        weitblickUrl = weitblickUrl.concat(partner.getLogo());

            Picasso.get()
                    .load(weitblickUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(logo);

        return view;
    }

}

