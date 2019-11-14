package com.example.weitblickapp_android.ui.blog_entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

public class BlogEntryListAdapter extends ArrayAdapter<BlogEntryViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<BlogEntryViewModel> blogEntries;
    private FragmentManager fragManager;

    public BlogEntryListAdapter(Context mContext, ArrayList<BlogEntryViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_blog_list, mDataSource);
        this.mContext = mContext;
        this.blogEntries = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }
    @Override
    public int getCount() {
        return blogEntries.size();
    }

    @Override
    public BlogEntryViewModel getItem(int position) {
        return blogEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(view == null) {
            view = mInflater.inflate(R.layout.fragment_blog_list, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            //TextView textView_location = (TextView) view.findViewById(R.id.location);
           // TextView textView_shorttext = (TextView) view.findViewById(R.id.shorttext);
            TextView textView_date = (TextView) view.findViewById(R.id.date);

            imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);

            final BlogEntryViewModel blog = (BlogEntryViewModel) getItem(position);

            textView_title.setText(blog.getTitle());
            //   textView_location.setText(article.get);
           // textView_shorttext.setText(blog.getText());

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = fragManager.beginTransaction();
                    ft.replace(R.id.fragment_container, new BlogDetailFragment(blog));
                    ft.commit();
                }
            });

            ImageButton detail = (ImageButton) view.findViewById(R.id.blog_more_btn);
            detail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = fragManager.beginTransaction();
                    ft.replace(R.id.fragment_container, new BlogDetailFragment(blog));
                    ft.commit();
                }
            });
        }
        return view;
    }
}

