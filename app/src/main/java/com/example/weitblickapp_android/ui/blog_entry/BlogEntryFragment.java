package com.example.weitblickapp_android.ui.blog_entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.weitblickapp_android.R;

public class BlogEntryFragment extends Fragment {

    private BlogEntryViewModel blogEntryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        blogEntryViewModel =
                ViewModelProviders.of(this).get(BlogEntryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        blogEntryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}