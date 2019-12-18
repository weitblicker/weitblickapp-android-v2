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
    private float Lat;
    private float Lng;
    private String locationName;
    private float current_amount;
    private float cycle_donation;
    private boolean finished;
    private int cycle_id;
    private String address;
    private float goal_amount;

    public ProjectViewModel(int projectId,String title,String text, float lat,float lng,String address, String name, float current_amount,float cycle_donation, boolean finished, int cycle_id, float goal_amount, ArrayList<String>imageUrls) {
        this.id = projectId;
        this.name = title;
        this.description = text;
        this.Lng = lng;
        this.Lat = lat;
        this.locationName = name;
        this.current_amount = current_amount;
        this.cycle_donation = cycle_donation;
        this.cycle_id = cycle_id;
        this.finished = finished;
        this.address = address;
        this.goal_amount = goal_amount;
        this.imageUrls = imageUrls;
    }
    public ProjectViewModel(int projectId,String title,String text, float lat,float lng,String address, String name, float current_amount,float cycle_donation, boolean finished, int cycle_id, float goal_amount) {
        this.id = projectId;
        this.name = title;
        this.description = text;
        this.Lng = lng;
        this.Lat = lat;
        this.locationName = name;
        this.current_amount = current_amount;
        this.cycle_donation = cycle_donation;
        this.cycle_id = cycle_id;
        this.finished = finished;
        this.address = address;
        this.goal_amount = goal_amount;
    }

    public ProjectViewModel(){

    }

    public float getGoal_amount() {
        return goal_amount;
    }

    public void setGoal_amount(float goal_amount) {
        this.goal_amount = goal_amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ProjectViewModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public float getCurrent_amount() {
        return current_amount;
    }

    public void setCurrent_amount(float current_amount) {
        this.current_amount = current_amount;
    }

    public float getCycle_donation() {
        return cycle_donation;
    }

    public void setCycle_donation(float cycle_donation) {
        this.cycle_donation = cycle_donation;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(int cycle_id) {
        this.cycle_id = cycle_id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public float getLat() {
        return Lat;
    }

    public void setLat(float lat) {
        this.Lat = lat;
    }

    public float getLng() {
        return Lng;
    }

    public void setLng(float lng) {
        this.Lng = lng;
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