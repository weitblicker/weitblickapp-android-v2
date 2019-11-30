package com.example.weitblickapp_android.ui.location;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.MainActivity;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.faq.FaqListFragment;
import com.example.weitblickapp_android.ui.faq.FaqViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EndFragment extends Fragment {

    private EndViewModel endViewModel;
    ArrayList<EndViewModel> end = new ArrayList<EndViewModel>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_end, container, false);
        ListView listview = (ListView) root.findViewById(R.id.list);

        EndFragment.CustomAdapter customAdapter = new EndFragment.CustomAdapter();
        listview.setAdapter(customAdapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadEnd();
    }


    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadEnd();
    }

    public void loadEnd(){
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
                        String question = responseObject.getString("question");
                        String answer = responseObject.getString("answer");

                        EndViewModel temp = new EndViewModel(1,"Weihnaachtsmann & CO. KG", "Baue eine Schule in Afrika");
                        end.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
            return 1;
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
                view = getLayoutInflater().inflate(R.layout.fragment_end_list,null);

                TextView textView_sponsor = (TextView)view.findViewById(R.id.sponsor);

                textView_sponsor.setText("Weihnachtsmann & CO. KG");
            }
            return view;
        }
    }
}