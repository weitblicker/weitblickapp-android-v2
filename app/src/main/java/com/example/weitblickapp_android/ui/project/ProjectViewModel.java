package com.example.weitblickapp_android.ui.project;

import androidx.lifecycle.ViewModel;

import com.example.weitblickapp_android.ui.blog_entry.BlogEntryViewModel;
import com.example.weitblickapp_android.ui.cycle.CycleViewModel;
import com.example.weitblickapp_android.ui.news.NewsViewModel;
import com.example.weitblickapp_android.ui.partner.ProjectPartnerViewModel;
import com.example.weitblickapp_android.ui.sponsor.SponsorViewModel;

import java.util.ArrayList;

public class ProjectViewModel extends ViewModel {

    private int id;
    private String name;
    private ArrayList<String> hosts;
    private String description;
    private int locationId;
    private ArrayList <String> imageUrls;
    private ArrayList <ProjectPartnerViewModel> partner_ids;
    private ArrayList <NewsViewModel> new_ids;
    private ArrayList <BlogEntryViewModel> blog_ids;
    private ArrayList <SponsorViewModel> sponsor_ids;
    private float Lat;
    private float Lng;
    private String locationName;
    private String address;
    private CycleViewModel cycle;
    float donationGoal;
    float currentAmount;
    String goalDescription;

    public ProjectViewModel(int projectId, String title, String text, float lat, float lng, String address, String name, CycleViewModel cycle, ArrayList<String>imageUrls, ArrayList <ProjectPartnerViewModel> partner_ids, ArrayList <NewsViewModel> news_id, ArrayList <BlogEntryViewModel> blog_id, ArrayList <SponsorViewModel> sponsor_id, float currentAmount, float donationGoal, String goalDescription, ArrayList<String> allHosts) {
        this.id = projectId;
        this.name = title;
        this.description = text;
        this.Lng = lng;
        this.Lat = lat;
        this.locationName = name;
        this.cycle = cycle;
        this.address = address;
        this.imageUrls = imageUrls;
        this.partner_ids = partner_ids;
        this.new_ids = news_id;
        this.blog_ids = blog_id;
        this.donationGoal = donationGoal;
        this.currentAmount = currentAmount;
        this.sponsor_ids = sponsor_id;
        this.goalDescription = goalDescription;
        this.hosts = allHosts;
    }

    public ProjectViewModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public ProjectViewModel(String title){
        this.name = title;
    }

    public ProjectViewModel(){

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public ArrayList<ProjectPartnerViewModel> getPartner_ids() {
        return partner_ids;
    }

    public ArrayList <String> getImageUrls() { return imageUrls; }

    public void setImageUrls(ArrayList <String> imageUrl) {
        this.imageUrls = imageUrls;
    }

    public void setPartner_ids(ArrayList<ProjectPartnerViewModel> partner_ids) {
        this.partner_ids = partner_ids;
    }

    public ArrayList<NewsViewModel> getNew_ids() {
        return new_ids;
    }

    public void setNew_ids(ArrayList<NewsViewModel> new_ids) {
        this.new_ids = new_ids;
    }

    public ArrayList<BlogEntryViewModel> getBlog_ids() {
        return blog_ids;
    }

    public void setBlog_ids(ArrayList<BlogEntryViewModel> blog_ids) {
        this.blog_ids = blog_ids;
    }

    public CycleViewModel getCycle() {
        return cycle;
    }

    public void setCycle(CycleViewModel cycle) {
        this.cycle = cycle;
    }

    public float getDonationGoal() {
        return donationGoal;
    }

    public void setDonationGoal(float donationGoal) {
        this.donationGoal = donationGoal;
    }

    public float getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(float currentAmount) {
        this.currentAmount = currentAmount;
    }

    public ArrayList<SponsorViewModel> getSponsor_ids() {
        return sponsor_ids;
    }

    public void setSponsor_ids(ArrayList<SponsorViewModel> sponsor_ids) {
        this.sponsor_ids = sponsor_ids;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
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