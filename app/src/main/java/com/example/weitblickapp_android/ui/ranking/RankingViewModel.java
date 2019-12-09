package com.example.weitblickapp_android.ui.ranking;

import androidx.lifecycle.ViewModel;

public class RankingViewModel extends ViewModel {

    private String profileImageUrl;
    private String username;
    private double cycledKm;
    private double cycledDonation;


    public RankingViewModel() {

    }

    public RankingViewModel(String profileImageUrl, String username, double cycledKm, double cycledDonation) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.cycledKm = cycledKm;
        this.cycledDonation = cycledDonation;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getCycledKm() {
        return cycledKm;
    }

    public void setCycledKm(double cycledKm) {
        this.cycledKm = cycledKm;
    }

    public double getCycledDonation() {
        return cycledDonation;
    }

    public void setCycledDonation(double cycledDonation) {
        this.cycledDonation = cycledDonation;
    }
}