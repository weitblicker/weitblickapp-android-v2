package com.example.weitblickapp_android.ui.ranking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RankingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hier werden bald Rankings stehen!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}