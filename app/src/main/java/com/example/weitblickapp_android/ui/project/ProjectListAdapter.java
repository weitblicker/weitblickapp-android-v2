package com.example.weitblickapp_android.ui.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

public class ProjectListAdapter extends ArrayAdapter<ProjectViewModel> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProjectViewModel> projects;

    public ProjectListAdapter(Context mContext, ArrayList<ProjectViewModel> mDataSource) {
        super(mContext, R.layout.fragment_project_list, mDataSource);
        this.mContext = mContext;
        this.projects = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public ProjectViewModel getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(view == null) {
             view = mInflater.inflate(R.layout.fragment_project_list, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            TextView textView_location = (TextView) view.findViewById(R.id.location);
            TextView textView_shorttext = (TextView) view.findViewById(R.id.shorttext);
            TextView textView_date = (TextView) view.findViewById(R.id.date);

            imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);

            final ProjectViewModel project = (ProjectViewModel) getItem(position);

            textView_title.setText(project.getName());
           // textView_location.setText(project.getDescription());
            textView_shorttext.setText(project.getDescription());
           // textView_date.setText();
        }
        return view;
    }
}

