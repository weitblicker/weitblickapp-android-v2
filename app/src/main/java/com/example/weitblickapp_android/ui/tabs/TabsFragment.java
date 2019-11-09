package com.example.weitblickapp_android.ui.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.ViewPageAdapter;
import com.example.weitblickapp_android.ui.event.EventFragment;
import com.example.weitblickapp_android.ui.news.NewsFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class TabsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tabs, container, false);

        tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) root.findViewById(R.id.pager);

        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.AddFragment(new NewsFragment(), "Neuheiten");
        adapter.AddFragment(new EventFragment(), "Veranstaltungen");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}