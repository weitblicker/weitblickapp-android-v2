package com.example.weitblickapp_android.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsShortAdapter extends ArrayAdapter<NewsViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NewsViewModel> news;
    private FragmentManager fragManager;

    public NewsShortAdapter(Context mContext, ArrayList<NewsViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_news_list, mDataSource);
        this.mContext = mContext;
        this.news = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }
    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public NewsViewModel getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        String weitblickUrl = "https://new.weitblicker.org";

        view = mInflater.inflate(R.layout.fragment_news_blog, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView_title = (TextView) view.findViewById(R.id.titel);
        TextView textView_teaser = (TextView) view.findViewById(R.id.teaser);
        TextView textView_date = (TextView) view.findViewById(R.id.date);

        TextView partner = (TextView) view.findViewById(R.id.partner);


        final NewsViewModel article = (NewsViewModel) getItem(position);

        StringBuilder b = new StringBuilder();
        for(String s : article.getHosts()){
            b.append(s);
            b.append(" ");
        }
        partner.setText(b.toString());

        if(article.getImageUrls().size()>0) {
            weitblickUrl = weitblickUrl.concat(article.getImageUrls().get(0));
        }

        Picasso.get().load(weitblickUrl).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);


        textView_title.setText(article.getTitle());
        textView_teaser.setText(article.getTeaser());
        textView_date.setText(article.getDate());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragManager.beginTransaction();
                ft.replace(R.id.fragment_container, new NewsDetailFragment(article));
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }
}