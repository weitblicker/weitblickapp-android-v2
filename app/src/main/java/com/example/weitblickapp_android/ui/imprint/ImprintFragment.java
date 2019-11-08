package com.example.weitblickapp_android.ui.imprint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weitblickapp_android.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ImprintFragment extends Fragment {

    private ImprintViewModel imprintViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        imprintViewModel =
                ViewModelProviders.of(this).get(ImprintViewModel.class);
        View root = inflater.inflate(R.layout.fragment_imprint, container, false);
        final TextView textView = root.findViewById(R.id.text_imprint);
        imprintViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
