package com.example.weitblickapp_android.ui.event;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;
    ArrayList<EventViewModel> events = new ArrayList<EventViewModel>();


    String[] title = {"Wöchentliche Veranstaltung", "Spendensammel Aktion", "Kleidertausch"};
    String[] location = {"Osnabrück", "Münster", "Osnabrück"};
    String[] date = {"06.11.2019" , "17.04.2018", "25.08.2009"};
    String[] text = {"Heute ist es wieder soweit für unser wöchentliches Weitblick-Treffen! Wir freuen uns hier all die neuen Gesichter begrüßen zu dürfen... ", "Spendensammel-Projekt", "Kleidertausch Veranstaltung"};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadEvents();
    }


    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadEvents();
    }

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

    public void loadEvents(){
        String URL = "https://new.weitblicker.org/rest/events/?limit=3&search=Benin";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Save Data into Model
                String jsonData = response.toString();
                //Parse the JSON response array by iterating over it
                for (int i = 0; i < response.length(); i++) {
                    JSONObject responseObject = null;
                    try {
                        responseObject = response.getJSONObject(i);
                        Integer eventId = responseObject.getInt("id");
                        String title = responseObject.getString("title");



                        EventViewModel temp = new EventViewModel(eventId, title);
                        events.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(EventViewModel event:events){
                    Log.e("Event",event.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Rest Response", error.toString());
            }
        }){
            //Override getHeaders() to set Credentials for REST-Authentication
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "surfer:hangloose";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        requestQueue.add(objectRequest);
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


                view = getLayoutInflater().inflate(R.layout.fragment_event_list,null);

                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                TextView textView_title = (TextView)view.findViewById(R.id.title);
                TextView textView_location = (TextView)view.findViewById(R.id.location);
                TextView textView_date = (TextView)view.findViewById(R.id.date);

                imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
                textView_title.setText(title[position]);
                textView_location.setText(location[position]);
                textView_date.setText(date[position]);



            ImageButton detail = (ImageButton) view.findViewById(R.id.event_more_btn);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new EventDetailFragment(location[position], title[position], date[position], text[position]));
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            detail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new EventDetailFragment(location[position], title[position], date[position], text[position]));
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new EventDetailFragment(location[position], title[position], date[position], text[position]));
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            return view;
        }
    }
}