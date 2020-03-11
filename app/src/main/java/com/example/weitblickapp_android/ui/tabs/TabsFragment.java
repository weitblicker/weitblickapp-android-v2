package com.example.weitblickapp_android.ui.tabs;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ViewPageAdapter;
import com.example.weitblickapp_android.ui.event.EventListFragment;
import com.example.weitblickapp_android.ui.news.NewsListFragment;
import com.google.android.material.tabs.TabLayout;

public class TabsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tabs, container, false);

        //setUp tabLayout and ViewPager
        tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) root.findViewById(R.id.pager);

        //add Fragments to adapter
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.AddFragment(new NewsListFragment(), "Neuigkeiten");
        adapter.AddFragment(new EventListFragment(), "Veranstaltungen");

        //view viewPager and tabLayout
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}