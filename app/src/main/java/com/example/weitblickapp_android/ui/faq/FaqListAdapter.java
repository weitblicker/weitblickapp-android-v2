package com.example.weitblickapp_android.ui.faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.project.ProjectViewModel;

import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;


public class FaqListAdapter extends ArrayAdapter<FaqViewModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FaqViewModel> faq;
    private FragmentManager fragManager;

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
        TextView textView_answer = (TextView) view.findViewById(R.id.answer);

        final FaqViewModel article = (FaqViewModel) getItem(position);

        textView_question.setText(article.getQuestion());
        textView_answer.setText(article.getAnswer());

        return view;
    }
}
