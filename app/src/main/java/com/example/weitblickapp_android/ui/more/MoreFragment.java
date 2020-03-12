package com.example.weitblickapp_android.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.more.credits.CreditsFragment;
import com.example.weitblickapp_android.ui.more.agb.AgbFragment;
import com.example.weitblickapp_android.ui.more.contact.ContactFragment;
import com.example.weitblickapp_android.ui.more.faq.FaqListFragment;

public class MoreFragment extends Fragment {

    private MoreViewModel moreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moreViewModel =
                ViewModelProviders.of(this).get(MoreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_more, container, false);

        ImageView faq = (ImageView) root.findViewById(R.id.faq);
        ImageView blog = (ImageView) root.findViewById(R.id.news);
        ImageView contact = (ImageView) root.findViewById(R.id.contact);
        ImageView credits = (ImageView) root.findViewById(R.id.credits);

        TextView faqText = (TextView) root.findViewById(R.id.faqtext);
        TextView agbText = (TextView) root.findViewById(R.id.agbtext);
        TextView contactText = (TextView) root.findViewById(R.id.contactext);
        TextView creditsText = (TextView) root.findViewById(R.id.creditsText);

        //set onClickListener to FAQListView
        faqText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FaqListFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        faq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FaqListFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //set onClickListener CreditsFragmentView
        creditsText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new CreditsFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        credits.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new CreditsFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //set onClickListener AGBView
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AgbFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        agbText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AgbFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //set onClickListener ContactView
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new ContactFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        contactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new ContactFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return root;
    }

}
