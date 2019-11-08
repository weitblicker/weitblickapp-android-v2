package com.example.weitblickapp_android.ui.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
<<<<<<< HEAD:app/src/main/java/com/example/weitblickapp_android/ui/event/EventFragment.java
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.project.ProjectFragment;
=======
>>>>>>> REST:app/src/main/java/com/example/weitblickapp_android/ui/maps/MapsFragment.java

public class MapsFragment extends Fragment {

    private MapsViewModel mapsViewModel;

    String[] title = {"Wöchentliche Veranstaltung", "Spendensammel Aktion", "Kleidertausch"};
    String[] location = {"Osnabrück", "Münster", "Osnabrück"};
    String[] date = {"06.11.2019" , "17.04.2018", "25.08.2009"};


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        mapsViewModel =
                ViewModelProviders.of(this).get(MapsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event, container, false);
<<<<<<< HEAD:app/src/main/java/com/example/weitblickapp_android/ui/event/EventFragment.java
        ListView listview = (ListView)root.findViewById(R.id.listView);

        EventFragment.CustomAdapter customaAdapter = new EventFragment.CustomAdapter();
        listview.setAdapter(customaAdapter);

=======
        final TextView textView = root.findViewById(R.id.text_slideshow);
        mapsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
>>>>>>> REST:app/src/main/java/com/example/weitblickapp_android/ui/maps/MapsFragment.java
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

            view = getLayoutInflater().inflate(R.layout.fragment_event_list,null);

            TextView textView_title = (TextView)view.findViewById(R.id.title);
            TextView textView_location = (TextView)view.findViewById(R.id.location);
            TextView textView_date = (TextView)view.findViewById(R.id.date);

            textView_title.setText(title[position]);
            textView_location.setText(location[position]);
            textView_date.setText(date[position]);

            return view;
        }
    }
}