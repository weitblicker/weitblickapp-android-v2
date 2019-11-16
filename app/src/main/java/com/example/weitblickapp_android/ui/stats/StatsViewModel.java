package com.example.weitblickapp_android.ui.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hier werden bald deine Statistiken stehen!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}