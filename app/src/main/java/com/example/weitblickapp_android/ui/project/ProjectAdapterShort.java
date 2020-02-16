package com.example.weitblickapp_android.ui.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.location.MapOverviewFragment;
import com.example.weitblickapp_android.ui.news.NewsDetailFragment;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProjectAdapterShort extends ArrayAdapter<ProjectViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProjectViewModel> projects;
    private FragmentManager fragManager;
    String PREF_NAME = "DefaultProject";

    public ProjectAdapterShort(Context mContext, ArrayList<ProjectViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_project_news_blog, mDataSource);
        this.mContext = mContext;
        this.projects = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
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

        String weitblickUrl = "https://new.weitblicker.org";

        view = mInflater.inflate(R.layout.fragment_project_news_blog, null);


        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView_title = (TextView) view.findViewById(R.id.title);
        TextView textView_address = (TextView) view.findViewById(R.id.location);
        ImageButton maps = (ImageButton) view.findViewById(R.id.project_maps_btn);
        TextView partner = (TextView) view.findViewById(R.id.partner);


        final ProjectViewModel project = (ProjectViewModel) getItem(position);


        StringBuilder b = new StringBuilder();
        for(String s : project.getHosts()){
            b.append(s);
            b.append(" ");
        }
        StringBuilder B = new StringBuilder();
        for ( int i = 0; i < b.length(); i++ ) {
            char c = b.charAt( i );
            if(Character.isLowerCase(c)){
                B.append(Character.toUpperCase(c));
            }else{
                B.append(c);
            }
        }

        if(project.getCycle() == null){

            maps.setVisibility(View.GONE);
        }else{
            maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("projectid", project.getId());
                    editor.putString("projectname", project.getName());
                    editor.putFloat("lat", (long) project.getLat());
                    editor.putFloat("lng", (long) project.getLng());
                    editor.putString("hosts", B.toString());
                    editor.putString("location", project.getAddress());
                    editor.commit();
                    FragmentTransaction ft = fragManager.beginTransaction();
                    ft.replace(R.id.fragment_container, new MapOverviewFragment());
                    ft.commit();
                }
            });
        }
        try {
            weitblickUrl = weitblickUrl.concat(project.getImageUrls().get(0));
        }catch(IndexOutOfBoundsException e){
            Log.e("Info", "no pictures for this BlogEntry");
        }

        Picasso.get()
                .load(weitblickUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);

        textView_title.setText(project.getName());
        textView_address.setText(project.getAddress());

        partner.setText(B.toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragManager.beginTransaction();
                ft.replace(R.id.fragment_container, new ProjectDetailFragment(project));
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }
}
