package com.example.weitblickapp_android.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NewsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NewsViewModel> news;
    private FragmentManager fragManager;

    public NewsListAdapter(Context mContext, ArrayList<NewsViewModel> mDataSource) {
        this.mContext = mContext;
        this.news = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
      return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(view == null) {
             view = mInflater.inflate(R.layout.fragment_news_list, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            TextView textView_location = (TextView) view.findViewById(R.id.location);
            TextView textView_shorttext = (TextView) view.findViewById(R.id.shorttext);
            TextView textView_date = (TextView) view.findViewById(R.id.date);

            imageView.setImageResource(R.drawable.ic_wbcd_logo_standard_svg2);

            final NewsViewModel article = (NewsViewModel) getItem(position);

            textView_title.setText(article.getTitle());
         //   textView_location.setText(article.get);
            textView_shorttext.setText(article.getText());
            textView_date.setText(article.getDate());
        }

        Button detail = (Button) view.findViewById(R.id.news_more_btn);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragManager.beginTransaction();
                ft.replace(R.id.fragment_container, new NewsDetailFragment());
                ft.commit();
            }
        });

        return view;
    }
}