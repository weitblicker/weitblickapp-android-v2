package com.example.weitblickapp_android.ui.sponsor;

import androidx.lifecycle.ViewModel;

public class SponsorViewModel extends ViewModel {

    String description;
    String name;
    String weblink;
    String logo;
    String rateProKm;
    String goal;
    int id;

    public SponsorViewModel(){};

    public SponsorViewModel(String name, String dec, String weblink, String logo, String rateProKm, String goal){

        this.description = dec;
        this.name = name;
        this.weblink = weblink;
        this.rateProKm = rateProKm;
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRateProKm() {
        return rateProKm;
    }

    public void setRateProKm(String rateProKm) {
        this.rateProKm = rateProKm;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
