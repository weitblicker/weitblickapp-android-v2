package com.example.weitblickapp_android.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;

import java.util.ArrayList;


public class EndFragment extends Fragment {

    ArrayList<SponsorModel> sponsors = new ArrayList<SponsorModel>();

    String distanceTotal;
    String eurosTotal;
    String projectName;

    private ProjectViewModel project;

     EndFragment(Tour currentTour, ProjectViewModel project){
        this.project = project;
        this.distanceTotal = String.format("%.2f", currentTour.getDistanceTotal()).concat(" km");
        this.eurosTotal = String.format("%.2f", currentTour.getEurosTotal()).concat(" €");
        this.projectName = project.getName();
     }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_end, container, false);

        TextView distanceText = root.findViewById(R.id.distance);
        distanceText.setText(distanceTotal);

        TextView eurosText = root.findViewById(R.id.donation);
        eurosText.setText(eurosTotal);

        TextView projectNameText = root.findViewById(R.id.project);
        projectNameText.setText(projectName);


        ListView listview = (ListView) root.findViewById(R.id.list);

        EndFragment.CustomAdapter customAdapter = new EndFragment.CustomAdapter();
        listview.setAdapter(customAdapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
       // loadEnd();
        setStaticSponsor();
    }

    public void setStaticSponsor(){
       // EndViewModel temp = new EndViewModel(1,"Weihnaachtsmann & CO. KG", "Baue eine Schule in Afrika");
       // end.add(temp);
    }


    class CustomAdapter extends BaseAdapter {

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

            if(view == null){
                view = getLayoutInflater().inflate(R.layout.fragment_end_list,null);

                TextView textView_sponsor = (TextView)view.findViewById(R.id.sponsor);
            }
            return view;
        }
    }
}