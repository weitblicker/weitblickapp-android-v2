package com.example.weitblickapp_android.ui.more.credits;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import io.noties.markwon.Markwon;

public class CreditsFragment extends Fragment {

    private CreditsListAdapter adapter;
    ArrayList<MemberViewModel> allMembers = new ArrayList<MemberViewModel>();
    RequestQueue requestQueue;
    String image;
    String titel;
    String description;
    String weitblickUrl = "https://weitblicker.org/";

    TextView titelView;
    TextView descriptionView;
    ImageView imageView;
    ListView list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCredits();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_credits, container, false);

        titelView = (TextView) view.findViewById(R.id.name);
        descriptionView = (TextView) view.findViewById(R.id.description);
        imageView = (ImageView) view.findViewById(R.id.image);

        list = (ListView) view.findViewById(R.id.members);
        adapter = new CreditsListAdapter(getActivity(), allMembers, getFragmentManager());
        list.setAdapter(adapter);

        ImageButton back = (ImageButton) view.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    private void loadCredits(){

        String URL = "https://weitblicker.org/rest/info/credits/";

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject responseObject) {
                //Parse the JSON response array by iterating over it
                JSONArray membersArrayObject = null;
                JSONObject memberObject = null;
                JSONObject imageObject = null;
                ArrayList<String> imageUrls = new ArrayList<String>();

                try {

                    membersArrayObject = responseObject.getJSONArray("member");

                    String memberName = null;
                    String text = null;
                    String profil = null;
                    String email = null;
                    String role = null;
                    String name = null;
                    String description = null;

                    imageObject = responseObject.getJSONObject("image");

                    String url = imageObject.getString("url");

                    try {
                        weitblickUrl = weitblickUrl.concat(url);
                        Picasso.get().load(weitblickUrl).fit().centerCrop().
                                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);
                    }catch(IndexOutOfBoundsException e){
                        Log.e("Info", "no pictures for this BlogEntry");
                    }

                    name = responseObject.getString("name");

                    description = responseObject.getString("description");

                    final Markwon markwon = Markwon.create(getActivity());

                    markwon.setMarkdown(descriptionView, description);

                    titelView.setText(name);

                    for (int x = 0; x < membersArrayObject.length(); x++) {
                        memberObject = membersArrayObject.getJSONObject(x);
                        memberName = memberObject.getString("name");
                        text = memberObject.getString("text");
                        email = memberObject.getString("email");
                        profil = memberObject.getString("image");
                        role = memberObject.getString("role");
                        allMembers.add(new MemberViewModel(memberName, email, text, role, profil));
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 400 * (listView.getResources().getDisplayMetrics().density);
                item.measure(
                        View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                int height = item.getMeasuredHeight();
                totalItemsHeight += height;
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
