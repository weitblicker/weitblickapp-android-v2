package com.example.weitblickapp_android.ui.project;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProjectFragment extends Fragment {

    //private ProjectViewModel projectViewModel;
    ArrayList<ProjectViewModel> projects = new ArrayList<ProjectViewModel>();

    String[] title = {"Save the turtles", "Kinder in Not", "Baue eine Schule"};
    String[] location = {"Sydney", "Namibia", "Deutschland"};
    String[] shorttext = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProjects();
    }


/*
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadProjects();
    }
    */


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

       View root = inflater.inflate(R.layout.fragment_project, container, false);

        ListView listview = (ListView)root.findViewById(R.id.listView);

        CustomAdapter customAdapter = new CustomAdapter();
        listview.setAdapter(customAdapter);
        return root;
    }


    private void loadProjects(){
        // Talk to Rest API
        String URL = "https://new.weitblicker.org/rest/projects/?limit=3";

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
                        Integer projectId = responseObject.getInt("id");
                        String projectName = responseObject.getString("name");
                        String projectDescription = responseObject.getString("description");
                        projectDescription.trim();
                        Integer locationId = responseObject.getInt("location");

                        ProjectViewModel temp = new ProjectViewModel(projectId, projectName, projectDescription, locationId);
                        projects.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(ProjectViewModel project:projects){
                    Log.e("Projekt",project.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display Error Message
                Log.e("Rest Response", error.toString());
            }
        }){
            //Override header-Information to set Credentials
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
        public View getView(final int position, View view, ViewGroup parent) {

            if(view == null){
                view = getLayoutInflater().inflate(R.layout.fragment_project_list,null);

                ImageView imageView = (ImageView)view.findViewById(R.id.image);
                TextView textView_title = (TextView)view.findViewById(R.id.title);
                TextView textView_location = (TextView)view.findViewById(R.id.location);
                TextView textView_shorttext = (TextView)view.findViewById(R.id.shorttext);

                imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);
                textView_title.setText(title[position]);
                textView_location.setText(location[position]);
                textView_shorttext.setText(shorttext[position]);

            }

            Button detail = (Button) view.findViewById(R.id.project_more_btn);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new ProjectDetailFragment(location[position], title[position], shorttext[position]));
                    ft.commit();
                }
            });
            return view;
        }
    }
}