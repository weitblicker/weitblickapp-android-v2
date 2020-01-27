package com.example.weitblickapp_android.ui.sponsor;

import androidx.lifecycle.ViewModel;

public class SponsorViewModel extends ViewModel {

    String description;
    String name;
    String weblink;
    String donation;
    int id;

    public SponsorViewModel(){};

    public SponsorViewModel(String name, String dec, String weblink, String donation){
        this.description = dec;
        this.name = name;
        this.weblink = weblink;
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

    public String getDonation() {
        return donation;
    }

    public void setDonation(String donation) {
        this.donation = donation;
    }
}
