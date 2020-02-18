package com.example.weitblickapp_android.ui.project.milenstone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.weitblickapp_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.ListFragment;

public class MilenstoneDetailListFragment extends ListFragment {
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<MilenstoneViewModel> mileList = new ArrayList<MilenstoneViewModel>();
    private MilenstoneListAdapter adapter;
    private String lastItemDate;
    private String lastItemDateCheck = "";
    private String url = "https://weitblicker.org/rest/news?limit=5";

    private SharedPreferences cachedNews;
    private String PREF_NAME = "NewsList";

    public MilenstoneDetailListFragment(ArrayList<MilenstoneViewModel> arrMile) {
        this.mileList = arrMile;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_green));

        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        adapter = new MilenstoneListAdapter(getActivity(), mileList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }
}
