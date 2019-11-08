package com.example.weitblickapp_android.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.weitblickapp_android.R;

public class ProjectFragment extends Fragment {

    private ProjectViewModel projectViewModel;

    String[] title = {"Save the turtles", "Kinder in Not", "Baue eine Schule"};
    String[] location = {"Sydney", "Namibia", "Deutschland"};
    String[] shorttext = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        projectViewModel =
                ViewModelProviders.of(this).get(ProjectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_project, container, false);

        ListView listview = (ListView)root.findViewById(R.id.listView);


        CustomAdapter customaAdapter = new CustomAdapter();
        listview.setAdapter(customaAdapter);

        return root;
    }

    class CustomAdapter extends BaseAdapter{

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

            view = getLayoutInflater().inflate(R.layout.fragment_project_list,null);

            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            TextView textView_title = (TextView)view.findViewById(R.id.title);
            TextView textView_location = (TextView)view.findViewById(R.id.location);
            TextView textView_shorttext = (TextView)view.findViewById(R.id.shorttext);

            imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
            textView_title.setText(title[position]);
            textView_location.setText(location[position]);
            textView_shorttext.setText(shorttext[position]);

            return view;
        }
    }
}