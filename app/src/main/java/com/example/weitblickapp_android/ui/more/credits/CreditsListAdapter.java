package com.example.weitblickapp_android.ui.more.credits;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.faq.FagDetailFragment;
import com.example.weitblickapp_android.ui.faq.FaqViewModel;
import com.example.weitblickapp_android.ui.news.NewsDetailFragment;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class CreditsListAdapter extends ArrayAdapter<MemberViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MemberViewModel> member;
    private FragmentManager fragManager;

    public CreditsListAdapter(Context mContext, ArrayList<MemberViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_members, mDataSource);
        this.mContext = mContext;
        this.member = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }
    @Override
    public int getCount() {
        return member.size();
    }

    @Override
    public MemberViewModel getItem(int position) {
        return member.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_members, null);

        String weitblickUrl = "https://new.weitblicker.org";

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView email = (TextView) view.findViewById(R.id.email);
        TextView role = (TextView) view.findViewById(R.id.role);
        ImageView image = (ImageView) view.findViewById(R.id.profil);

        final MemberViewModel member = (MemberViewModel) getItem(position);

        try {
            weitblickUrl = weitblickUrl.concat(member.getImage());
        }catch(IndexOutOfBoundsException e){
            Log.e("Info", "no pictures for this Member");
        }
        Picasso.get().load(weitblickUrl).fit().centerCrop().
                placeholder(R.drawable.ic_wbcd_logo_standard_svg2)
                .error(R.drawable.ic_wbcd_logo_standard_svg2).into(image);

        name.setText(member.getName());
        email.setText(member.getEmail());
        text.setText(member.getText());
        role.setText(member.getRole());

        return view;
    }
}
