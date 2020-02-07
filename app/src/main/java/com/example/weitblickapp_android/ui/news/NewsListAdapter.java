package com.example.weitblickapp_android.ui.news;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsListAdapter extends ArrayAdapter<NewsViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NewsViewModel> news;
    private FragmentManager fragManager;

    public NewsListAdapter(Context mContext, ArrayList<NewsViewModel> mDataSource, FragmentManager fragManager) {
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

        String weitblickUrl = "https://weitblicker.org";

            view = mInflater.inflate(R.layout.fragment_news_list, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView_title = (TextView) view.findViewById(R.id.title);
            TextView textView_teaser = (TextView) view.findViewById(R.id.teaser);
            TextView textView_date = (TextView) view.findViewById(R.id.date);
            TextView partner = (TextView) view.findViewById(R.id.partner);


            final NewsViewModel article = (NewsViewModel) getItem(position);

            StringBuilder b = new StringBuilder();
            for(String s : article.getHosts()){
                b.append(s);
                b.append(" ");
            }
            StringBuilder B = new StringBuilder();
            for ( int i = 0; i < b.length(); i++ ) {
                char c = b.charAt( i );
                if(Character.isLowerCase(c)){
                    B.append(Character.toUpperCase(c));
                }else{
                    B.append(c);
                }
            }
            partner.setText(B.toString());

            if(article.getImageUrls().size()>0) {
                weitblickUrl = weitblickUrl.concat(article.getImageUrls().get(0));
            }

            Picasso.get().load(weitblickUrl).fit().centerCrop().
                     placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);


            textView_title.setText(article.getTitle());
            textView_teaser.setText(article.getTeaser());
            textView_date.setText(article.getDate());

            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView_teaser.getLayoutParams();
            params.height = getTextViewHeight(textView_teaser);
            textView_teaser.setLayoutParams(params);

            ViewGroup.LayoutParams params2 = (ViewGroup.LayoutParams) textView_title.getLayoutParams();
            params2.height = getTextViewHeight(textView_title);
            textView_title.setLayoutParams(params2);


            //Set View-Listener and redirect to Details-Page onClick

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

    public static int getTextViewHeight(TextView textView) {
        WindowManager wm =
                (WindowManager) textView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }
}