package com.example.weitblickapp_android.ui.faq;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FaqListFragment extends Fragment {

    private FaqViewModel faqViewModel;
    ArrayList<FaqViewModel> faq = new ArrayList<FaqViewModel>();

    String[] question = {"Was ist Weitblick?" , "Wie verläuft eine Spende genau ab?", "Wie werden meine Routen gespeichert?"};
    String[] answer = {"bugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuvbugeiodslkvjbdrs ghelbiusfldkvn dbiuv", "hsvjhb srrgvjsbvoc wowvuw wrgber", "h viwhf wiveriv irfgvwrfi wfwo fowe fwo ow fhf"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("FAQ");

        faqViewModel =
                ViewModelProviders.of(this).get(FaqViewModel.class);
        View root = inflater.inflate(R.layout.fragment_faq, container, false);
        ImageButton back = (ImageButton) root.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ListView listview = (ListView)root.findViewById(R.id.listView);

        FaqListFragment.CustomAdapter customAdapter = new FaqListFragment.CustomAdapter();
        listview.setAdapter(customAdapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFaq();
    }


    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        loadFaq();
    }

    public void loadFaq(){
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

                        FaqViewModel temp = new FaqViewModel(eventId, question, answer);
                        faq.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                for(FaqViewModel faq:faq){
                    Log.e("Faq",faq.toString());
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
            return question.length;
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
                view = getLayoutInflater().inflate(R.layout.fragment_faq_list,null);

                TextView textView_question = (TextView)view.findViewById(R.id.question);
                TextView textView_answer = (TextView)view.findViewById(R.id.answer);

                textView_answer.setText(answer[position]);
                textView_question.setText(question[position]);
            }
            return view;
        }
    }
}