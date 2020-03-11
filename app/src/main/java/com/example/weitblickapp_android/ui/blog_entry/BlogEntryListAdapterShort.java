package com.example.weitblickapp_android.ui.blog_entry;

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
import com.example.weitblickapp_android.ui.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogEntryListAdapterShort extends ArrayAdapter<BlogEntryViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<BlogEntryViewModel> blogEntries;
    private FragmentManager fragManager;

    public BlogEntryListAdapterShort(Context mContext, ArrayList<BlogEntryViewModel> mDataSource, FragmentManager fragManager) {
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

        String weitblickUrl = "https://weitblicker.org";

        view = mInflater.inflate(R.layout.fragment_news_blog, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView_title = (TextView) view.findViewById(R.id.titel);
        TextView textView_date = (TextView) view.findViewById(R.id.date);
        TextView teaser = (TextView) view.findViewById(R.id.teaser);
        TextView partner = (TextView) view.findViewById(R.id.partner);

        final BlogEntryViewModel blog = (BlogEntryViewModel) getItem(position);

        //Set picture for BlogEntries using Picasso-Lib
        if(blog.getImageUrls().size()>0) {
            weitblickUrl = weitblickUrl.concat(blog.getImageUrls().get(0));
        }

        Picasso.get().load(weitblickUrl).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(imageView);

        //Set title for BlogEntries
        textView_title.setText(blog.getTitle());
        teaser.setText(blog.getTeaser());
        textView_date.setText(blog.getPublished());

        //Makes one String out of the Partner Array
        StringBuilder b = new StringBuilder();
        for(String s : blog.getHosts()){
            b.append(s);
            b.append(" ");
        }

        //Makes Character UpperCase
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
        ImageView authorProfile = (ImageView) view.findViewById(R.id.imageProfil);
        TextView name = (TextView) view.findViewById(R.id.authorname);

        //LOAD blogPictures if null set defaultPicture
        if(blog.getImage().contains("null")){
            Picasso.get().load(R.mipmap.ic_launcher_foreground).fit().centerCrop()
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(authorProfile);
        }else {
            weitblickUrl = "https://weitblicker.org";
            String url = weitblickUrl.concat(blog.getImage());
            Picasso.get().load(url).transform(new CircleTransform()).fit().centerCrop().
                    placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                    .error(R.drawable.ic_wbcd_logo_standard_svg2).into(authorProfile);
        }
        name.setText(blog.getName());

        // onClick Listener for whole view-element -->redirect to DetailsPage
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragManager.beginTransaction();
                ft.replace(R.id.fragment_container, new BlogDetailFragment(blog));
                ft.addToBackStack(null);
                ft.commit();
            }
            // onClick Listener for Button-element -->redirect to DetailsPage
        });

        return view;
    }
}

