package com.example.weitblickapp_android.ui.project;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ProjectViewModel extends ViewModel {

    private int id;
    private String name;
    private ArrayList<String> hosts;
    private String description;
    private int locationId;
    private ArrayList <String> imageUrls;
    private ArrayList <Integer> partner_ids;

    public ProjectViewModel(Integer projectId, String projectName, String projectDescription, Integer locationId, ArrayList <String> imageUrl) {
        this.id = projectId;
        this.name = projectName;
        this.description = projectDescription;
        this.locationId = locationId;
        this.imageUrls = imageUrl;
    }

    public ProjectViewModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public ArrayList <String> getImageUrls() { return imageUrls; }

    public void setImageUrls(ArrayList <String> imageUrl) {
        this.imageUrls = imageUrls;
    }

    public void setPartner_ids(ArrayList<Integer> partner_ids) {
        this.partner_ids = partner_ids;
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