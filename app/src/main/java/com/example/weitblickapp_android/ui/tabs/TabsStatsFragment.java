package com.example.weitblickapp_android.ui.tabs;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.utils.ViewPageAdapter;
import com.example.weitblickapp_android.ui.ranking.RankingFragment;
import com.example.weitblickapp_android.ui.stats.StatsFragment;
import com.google.android.material.tabs.TabLayout;

public class TabsStatsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tabs, container, false);

        //setup tabLayout and viewPager
        tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) root.findViewById(R.id.pager);

        //add Fragments to adapter
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.AddFragment(new RankingFragment(), "Rangliste");
        adapter.AddFragment(new StatsFragment(), "Deine Strecken");

        //view viewPager and tabLayout
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}