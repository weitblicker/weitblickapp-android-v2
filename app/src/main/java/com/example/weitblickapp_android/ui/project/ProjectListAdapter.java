package com.example.weitblickapp_android.ui.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

public class ProjectListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProjectViewModel> projects;
    private FragmentManager fragManager;

    public ProjectListAdapter(Context mContext, ArrayList<ProjectViewModel> mDataSource) {
        this.mContext = mContext;
        this.projects = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(view == null) {
            // view = getLayoutInflater().inflate(R.layout.fragment_news_list, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            TextView textView_location = (TextView) view.findViewById(R.id.location);
            TextView textView_shorttext = (TextView) view.findViewById(R.id.shorttext);
            TextView textView_date = (TextView) view.findViewById(R.id.date);

            imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
            textView_title.setText("");
            textView_location.setText("");
            textView_shorttext.setText("");
            textView_date.setText("");
        }

       // Button detail = (Button) view.findViewById(R.id.news_more_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragManager.beginTransaction();
                ft.replace(R.id.fragment_container, new ProjectDetailFragment());
                ft.commit();
            }
        });

        return view;
    }
}

