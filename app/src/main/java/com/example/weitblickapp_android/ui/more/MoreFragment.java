package com.example.weitblickapp_android.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weitblickapp_android.R;
import com.example.weitblickapp_android.ui.blog_entry.BlogEntryListFragment;
import com.example.weitblickapp_android.ui.contact.ContactFragment;
import com.example.weitblickapp_android.ui.event.EventDetailFragment;
import com.example.weitblickapp_android.ui.faq.FaqListFragment;
import com.example.weitblickapp_android.ui.news.NewsDetailFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MoreFragment extends Fragment {

    private MoreViewModel moreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moreViewModel =
                ViewModelProviders.of(this).get(MoreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_more, container, false);

        ImageButton faqButton = (ImageButton) root.findViewById(R.id.faqButton);
        ImageButton blogButton = (ImageButton) root.findViewById(R.id.blogButton);
        ImageButton contactButton = (ImageButton) root.findViewById(R.id.contactButton);

        faqButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FaqListFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        blogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new BlogEntryListFragment());
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

        return root;
    }

}
