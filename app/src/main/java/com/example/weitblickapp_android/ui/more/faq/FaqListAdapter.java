package com.example.weitblickapp_android.ui.more.faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.weitblickapp_android.R;

import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FaqListAdapter extends ArrayAdapter<FaqViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FaqViewModel> faq;
    private FragmentManager fragManager;
    static ArrayList<String> titel = new ArrayList<>();

    public FaqListAdapter(Context mContext, ArrayList<FaqViewModel> mDataSource, FragmentManager fragManager) {
        super(mContext, R.layout.fragment_faq_list, mDataSource);
        this.mContext = mContext;
        this.faq = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragManager = fragManager;
    }
    @Override
    public int getCount() {
        return faq.size();
    }

    @Override
    public FaqViewModel getItem(int position) {
        return faq.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.fragment_faq_list, null);

        TextView textView_question = (TextView) view.findViewById(R.id.question);
        TextView title = (TextView) view.findViewById(R.id.titel);
        ImageView point = (ImageView) view.findViewById(R.id.point);

        final FaqViewModel faq = (FaqViewModel) getItem(position);

        if(faq.answer != null && faq.question != null){
                title.setVisibility(View.GONE);
                textView_question.setText(faq.getQuestion());

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = fragManager.beginTransaction();
                        ft.replace(R.id.fragment_container, new FagDetailFragment(faq));
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
        }else{
            textView_question.setVisibility(View.GONE);
            point.setVisibility(View.GONE);
            title.setText(faq.getTitel());
        }
        return view;
    }
}
