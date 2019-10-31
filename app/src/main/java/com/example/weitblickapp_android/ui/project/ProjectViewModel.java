package com.example.weitblickapp_android.ui.project;

import com.example.weitblickapp_android.ui.location.LocationViewModel;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProjectViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    int id;
    String name;
    ArrayList<String> hosts;
    String description;
    LocationViewModel location;
    ArrayList <Integer> partner_ids;


    public ProjectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is project fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}