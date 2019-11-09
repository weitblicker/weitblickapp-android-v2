package com.example.weitblickapp_android.ui.blog_entry;

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

import java.util.Date;

public class BlogEntryFragment extends Fragment {

    private BlogEntryViewModel blogEntryViewModel;

    String[] title = {"Save the turtles", "Kinder in Not", "Baue eine Schule"};
    String[] location = {"Sydney", "Namibia", "Deutschland"};
    String[] shorttext = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};
    String[] date = {"06.11.2019" , "17.04.2018", "25.08.2009"};

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        blogEntryViewModel =
                ViewModelProviders.of(this).get(BlogEntryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blog, container, false);
        ListView listview = (ListView)root.findViewById(R.id.listView);

        CustomAdapter customaAdapter = new CustomAdapter();
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

            view = getLayoutInflater().inflate(R.layout.fragment_blog_list,null);

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