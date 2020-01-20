package com.example.weitblickapp_android.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.register.RegisterFragment;
import com.example.weitblickapp_android.ui.contact.ContactFragment;
import com.example.weitblickapp_android.ui.faq.FaqListFragment;

public class MoreFragment extends Fragment {

    private MoreViewModel moreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moreViewModel =
                ViewModelProviders.of(this).get(MoreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_more, container, false);

        ImageButton faqButton = (ImageButton) root.findViewById(R.id.faqButton);
        ImageButton agbButton = (ImageButton) root.findViewById(R.id.agbButton);
        ImageButton contactButton = (ImageButton) root.findViewById(R.id.contactButton);

        ImageView faq = (ImageView) root.findViewById(R.id.faq);
        ImageView blog = (ImageView) root.findViewById(R.id.news);
        ImageView contact = (ImageView) root.findViewById(R.id.contact);

        TextView faqText = (TextView) root.findViewById(R.id.faqtext);
        TextView agbText = (TextView) root.findViewById(R.id.agbtext);
        TextView contactText = (TextView) root.findViewById(R.id.contactext);

        faqButton.setOnClickListener(new View.OnClickListener(){
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
        faqText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FaqListFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        agbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new RegisterFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new RegisterFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        agbText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new RegisterFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new ContactFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
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
