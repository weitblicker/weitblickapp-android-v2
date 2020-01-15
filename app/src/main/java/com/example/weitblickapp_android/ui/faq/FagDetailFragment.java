package com.example.weitblickapp_android.ui.faq;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import org.w3c.dom.Text;

import androidx.fragment.app.Fragment;

public class FagDetailFragment extends Fragment {

    FaqViewModel faq;

    public FagDetailFragment(FaqViewModel faq){
        this.faq = faq;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq_detail, container, false);

        TextView question = (TextView) view.findViewById(R.id.question);
        TextView answer = (TextView) view.findViewById(R.id.answer);
        ImageButton back = (ImageButton) view.findViewById(R.id.back);

        question.setText(this.faq.getQuestion());
        answer.setText(this.faq.getAnswer());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }


}
