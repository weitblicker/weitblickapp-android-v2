package com.example.weitblickapp_android.ui.project.partner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.ListFragment;
import com.example.weitblickapp_android.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProjectPartnerListFragment extends ListFragment{
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<ProjectPartnerViewModel> partnerList = new ArrayList<ProjectPartnerViewModel>();
    private ProjectPartnerAdapter adapter;
    private String lastItemDate;
    private String lastItemDateCheck = "";
    private String url = "https://weitblicker.org/rest/news?limit=5";

    private SharedPreferences cachedNews;
    private String PREF_NAME = "NewsList";

    public ProjectPartnerListFragment(ArrayList<ProjectPartnerViewModel> arrPartner){
        this.partnerList = arrPartner;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        adapter = new ProjectPartnerAdapter(getActivity(), partnerList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    public ArrayList <String> getImageUrls(String text){
        //Find image-tag markdowns and extract
        ArrayList <String> imageUrls = new ArrayList<>();
        Matcher m = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\\"")
                .matcher(text);
        while (m.find()) {
            imageUrls.add(m.group(2));
        }
        return imageUrls;
    }

}

