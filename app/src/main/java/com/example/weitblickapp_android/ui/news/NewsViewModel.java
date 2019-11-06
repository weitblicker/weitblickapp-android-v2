package com.example.weitblickapp_android.ui.news;

import com.example.weitblickapp_android.ui.location.LocationViewModel;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    int id;
    String title;
    String text;
    int image_id;
    Date created_at;
    Date updated_at;
    LocationViewModel location;

    public NewsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fragment_news fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}