package com.example.weitblickapp_android.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryFragment;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;

    String[] title = {"Das habt ihr erreicht", "Helft den Kindern", "Neue Schule gebaut"};
    String[] location = {"Sydney", "Namibia", "Deutschland"};
    String[] shorttext = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};
    String[] date = {"06.11.2019" , "17.04.2018", "25.08.2009"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        ListView listview = (ListView)root.findViewById(R.id.listView);

        NewsFragment.CustomAdapter customaAdapter = new NewsFragment.CustomAdapter();
        listview.setAdapter(customaAdapter);
        return root;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return title.length;
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
        public View getView(int position, View view, ViewGroup parent) {

            view = getLayoutInflater().inflate(R.layout.fragment_news_list,null);

            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            TextView textView_title = (TextView)view.findViewById(R.id.title);
            TextView textView_location = (TextView)view.findViewById(R.id.location);
            TextView textView_shorttext = (TextView)view.findViewById(R.id.shorttext);
            TextView textView_date = (TextView)view.findViewById(R.id.date);

            imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
            textView_title.setText(title[position]);
            textView_location.setText(location[position]);
            textView_shorttext.setText(shorttext[position]);
            textView_date.setText(date[position]);

            return view;
        }
    }
}