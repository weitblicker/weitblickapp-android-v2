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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getHosts() {
        return hosts;
    }

    public void setHosts(ArrayList<String> hosts) {
        this.hosts = hosts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationViewModel getLocation() {
        return location;
    }

    public void setLocation(LocationViewModel location) {
        this.location = location;
    }

    public ArrayList<Integer> getPartner_ids() {
        return partner_ids;
    }

    public void setPartner_ids(ArrayList<Integer> partner_ids) {
        this.partner_ids = partner_ids;
    }

    public LiveData<String> getText() {
        return mText;
    }
}