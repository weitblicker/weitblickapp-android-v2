package com.example.weitblickapp_android.ui.project;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProjectViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private int id;
    private String name;
    private ArrayList<String> hosts;
    private String description;
    private int locationId;
    private ArrayList <Integer> partner_ids;


    public ProjectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is project fragment");
    }

    public ProjectViewModel(Integer projectId, String projectName, String projectDescription, Integer locationId) {
        this.id = projectId;
        this.name = projectName;
        this.description = projectDescription;
        this.locationId = locationId;
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

    public int getLocation() {
        return locationId;
    }

    public void setLocation(int locationId) { this.locationId = locationId; }

    public ArrayList<Integer> getPartner_ids() {
        return partner_ids;
    }

    public void setPartner_ids(ArrayList<Integer> partner_ids) {
        this.partner_ids = partner_ids;
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public String toString() {
        return "ProjectViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hosts=" + hosts +
                ", description='" + description + '\'' +
                ", locationId=" + locationId +
                ", partner_ids=" + partner_ids +
                '}';
    }
}