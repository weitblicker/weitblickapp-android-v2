package com.example.weitblickapp_android.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;

    String[] title = {"Wöchentliche Veranstaltung", "Spendensammel Aktion", "Kleidertausch"};
    String[] location = {"Osnabrück", "Münster", "Osnabrück"};
    String[] date = {"06.11.2019" , "17.04.2018", "25.08.2009"};
    String[] text = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        eventViewModel =
                ViewModelProviders.of(this).get(EventViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        ListView listview = (ListView)root.findViewById(R.id.listView);

        EventFragment.CustomAdapter customAdapter = new EventFragment.CustomAdapter();
        listview.setAdapter(customAdapter);

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
        public View getView(final int position, View view, ViewGroup parent) {

            if(view == null){
                view = getLayoutInflater().inflate(R.layout.fragment_event_list,null);

                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                TextView textView_title = (TextView)view.findViewById(R.id.title);
                TextView textView_location = (TextView)view.findViewById(R.id.location);
                TextView textView_date = (TextView)view.findViewById(R.id.date);

                imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
                textView_title.setText(title[position]);
                textView_location.setText(location[position]);
                textView_date.setText(date[position]);
            }

            ImageButton detail = (ImageButton) view.findViewById(R.id.event_more_btn);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new EventDetailFragment(location[position], title[position], date[position], text[position]));
                    ft.commit();
                }
            });


            return view;
        }
    }
}