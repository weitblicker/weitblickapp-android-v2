package com.example.weitblickapp_android.ui.location;

import androidx.lifecycle.ViewModel;

public class SponsorModel extends ViewModel {

    int sponsorId;
    String sponsorName;
    String sponsorImageUrl;

    public SponsorModel(int sponsorId, String sponsorName, String sponsorImageUrl) {
        this.sponsorId = sponsorId;
        this.sponsorName = sponsorName;
        this.sponsorImageUrl = sponsorImageUrl;
    }

    public SponsorModel() {
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorImageUrl() {
        return sponsorImageUrl;
    }

    public void setSponsorImageUrl(String sponsorImageUrl) {
        this.sponsorImageUrl = sponsorImageUrl;
    }
}