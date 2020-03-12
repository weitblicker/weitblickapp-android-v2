package com.example.weitblickapp_android.ui.event;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.news.NewsListAdapter;
import com.example.weitblickapp_android.ui.news.NewsViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.fragment.app.ListFragment;

//get a DetailPage of all EventEntries of ProjectDetailPage

public class EventListDetailFragment extends ListFragment {
    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<EventViewModel> eventList = new ArrayList<EventViewModel>();
    private EventListAdapter adapter;
    private String lastItemDate;
    private String lastItemDateCheck = "";
    private String url = "https://weitblicker.org/rest/news?limit=5";
    private SharedPreferences cachedNews;
    private String PREF_NAME = "NewsList";

    public EventListDetailFragment(ArrayList<EventViewModel> arrEvent) {
        this.eventList = arrEvent;
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

        adapter = new EventListAdapter(getActivity(), eventList, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }
}