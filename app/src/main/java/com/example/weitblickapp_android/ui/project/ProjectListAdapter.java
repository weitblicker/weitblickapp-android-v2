package com.example.weitblickapp_android.ui.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.location.MapOverviewFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectListAdapter extends ArrayAdapter<ProjectViewModel> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ProjectViewModel> projects;
    private FragmentManager fragManager;
    String PREF_NAME = "DefaultProject";
    private OnItemClicked onItemClickedListener;
    int selectedPosition = -1;


    //checks which listItem is selected
    public void select(int position){
        selectedPosition = position;
        notifyDataSetChanged();
    }


    public ProjectListAdapter(Context mContext, ArrayList<ProjectViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_project_list, mDataSource);
        this.mContext = mContext;
        this.projects = mDataSource;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }

    public interface OnItemClicked{
        public void onClick(int position);
    }

    public void setOnItemClickedListener(OnItemClicked listener){
        onItemClickedListener = listener;
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

            String weitblickUrl = "https://weitblicker.org";

             view = mInflater.inflate(R.layout.fragment_project_list, null);

        //sets the selected Position in Focus
        if(selectedPosition == position){
            view.setSelected(true);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.wb_green));
        }

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            TextView textView_address = (TextView) view.findViewById(R.id.location);
            ImageButton maps = (ImageButton) view.findViewById(R.id.project_maps_btn);
            TextView partner = (TextView) view.findViewById(R.id.partner);


            //TextView textView_date = (TextView) view.findViewById(R.id.date);

        //set onClickListener to set the listItem in Focus
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickedListener != null){
                    onItemClickedListener.onClick(position);
                }
            }
        });

        final ProjectViewModel project = (ProjectViewModel) getItem(position);


        //makes one String out of a hostArray
        StringBuilder b = new StringBuilder();
        for(String s : project.getHosts()){
            b.append(s);
            b.append(" ");
        }
        //sets Character UpperCase
        StringBuilder B = new StringBuilder();
        for ( int i = 0; i < b.length(); i++ ) {
            char c = b.charAt( i );
            if(Character.isLowerCase(c)){
                B.append(Character.toUpperCase(c));
            }else{
                B.append(c);
            }
        }
            //set CycleIcon if Cycle is available
            if(project.getCycle() == null){

                maps.setVisibility(View.GONE);
            }else{
                //if project is selected sets as defaultProject
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
        //set Image or DefaultImage
        if(project.getImageUrls().size()>0) {
            weitblickUrl = weitblickUrl.concat(project.getImageUrls().get(0));

            Picasso.get().load(weitblickUrl).fit().centerCrop().placeholder(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);
        }else{
            Picasso.get().load(R.drawable.project_default).fit().centerCrop().into(imageView);
        }
            //set data
            textView_title.setText(project.getName());
            textView_address.setText(project.getAddress());

            partner.setText(B.toString());

            //textView_title.setText(project.g);

            //Set Button-Listener and redirect to Details-Page
            ImageButton detail = (ImageButton) view.findViewById(R.id.project_more_btn);
            detail.setOnClickListener(new View.OnClickListener() {
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

