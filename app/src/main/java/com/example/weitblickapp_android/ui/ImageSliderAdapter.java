package com.example.weitblickapp_android.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weitblickapp_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {
    private ArrayList<String> images = new ArrayList<>();
    private Context context;

    public ImageSliderAdapter(FragmentManager fm, ArrayList<String> images) {
        this.images = images;
    }

    public ImageSliderAdapter(FragmentManager fm, Context context, ArrayList<String> images) {
        super();
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    //Setup Slider items and put them into ViewPager "container"
    @NonNull
    @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.image_slider_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.image_pager_item_image);


            Log.e("!!!!!!!!!!!!!!!!!", images.get(0));



        Picasso.get()
                .load(images.get(position))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2)
                .into(imageView);

        ((ViewPager) container).addView(itemView);

        return itemView;
    }


}
