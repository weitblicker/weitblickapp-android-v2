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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.location.MapOverviewFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

public class ProjectCycleListAdapter extends ArrayAdapter<ProjectViewModel> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProjectViewModel> projects;
    private FragmentManager fragManager;
    String PREF_NAME = "DefaultProject";

    public ProjectCycleListAdapter(Context mContext, ArrayList<ProjectViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_project_cycle_list, mDataSource);
        this.mContext = mContext;
        this.projects = mDataSource;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        final ProjectViewModel project = (ProjectViewModel) getItem(position);

        String weitblickUrl = "https://weitblicker.org";

        view = mInflater.inflate(R.layout.fragment_project_cycle_list, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            TextView textView_address = (TextView) view.findViewById(R.id.location);
            TextView partner = (TextView) view.findViewById(R.id.partner);

            if(project.getImageUrls().size() != 0){
                weitblickUrl = weitblickUrl.concat(project.getImageUrls().get(0));

                Picasso.get()
                        .load(weitblickUrl)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                        .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);
            }

            textView_title.setText(project.getName());
            textView_address.setText(project.getAddress());

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
            partner.setText(B.toString());
            //Set Button-Listener and redirect to Details-Page
            ImageButton detail = (ImageButton) view.findViewById(R.id.project_more_btn);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = fragManager.beginTransaction();
                    ft.replace(R.id.fragment_container, new ProjectDetailFragment(project));
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            detail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = fragManager.beginTransaction();
                    ft.replace(R.id.fragment_container, new ProjectDetailFragment(project));
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            ImageButton defProject = (ImageButton) view.findViewById(R.id.project_maps_btn);

            defProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getContext().getApplicationContext().getSharedPreferences(PREF_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("projectid", project.getId());
                    editor.putString("projectname", project.getName());
                    editor.putFloat("lat", project.getLat());
                    editor.putFloat("lng", project.getLng());
                    editor.commit();
                    FragmentTransaction ft = fragManager.beginTransaction();
                    ft.replace(R.id.fragment_container, new MapOverviewFragment());
                    ft.commit();
                }
            });
        return view;
    }

}

