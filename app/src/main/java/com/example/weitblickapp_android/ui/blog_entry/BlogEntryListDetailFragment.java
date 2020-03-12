package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//GET all BlogEntries as a List from a ProjectDetailPage

public class BlogEntryListDetailFragment extends ListFragment{

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<BlogEntryViewModel> blogEntries = new ArrayList<BlogEntryViewModel>();
    private BlogEntryListAdapter adapter;

    private String lastItemDate;
    private String lastItemDateCheck = "";
    int limitLoadedBlogs = 20;
    private String url = "https://weitblicker.org/rest/blog?limit=5";


    public BlogEntryListDetailFragment(ArrayList<BlogEntryViewModel> arrBlog){
        this.blogEntries = arrBlog;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Blog");

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ImageButton back = (ImageButton) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        adapter = new BlogEntryListAdapter(getActivity(), blogEntries, getFragmentManager());
        this.setListAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        // Button detail = (Button) v.findViewById(R.id.news_more_btn);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentTransaction replace = ft.replace(R.id.fragment_container, new BlogDetailFragment(blogEntries.get(position)));
                ft.commit();
            }
        });
    }
}
