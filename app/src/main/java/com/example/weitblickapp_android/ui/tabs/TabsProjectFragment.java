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
import com.example.weitblickapp_android.ui.blog_entry.RegisterFragment;
import com.example.weitblickapp_android.ui.project.ProjectListFragment;
import com.google.android.material.tabs.TabLayout;

public class TabsProjectFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tabs, container, false);

        tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) root.findViewById(R.id.pager);

        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.AddFragment(new ProjectListFragment(), "Projekte");
        adapter.AddFragment(new RegisterFragment(), "Blog");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}