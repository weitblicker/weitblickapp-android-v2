package com.example.weitblickapp_android.ui.location;

import android.location.Address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    int id;
    String name;
    String country;
    float lat;
    float lng;
    Address adress;

    public LocationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is location fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}