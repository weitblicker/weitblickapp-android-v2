package com.example.weitblickapp_android.ui.ranking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankingViewModel extends ViewModel {

    private String username;
    private float km;
    private float donation;
    private String profileImage;

    public RankingViewModel(){};

    public RankingViewModel(String username, float km, float donation, String imageProfile){
        this.username = username;
        this.donation = donation;
        this.km = km;
        this.profileImage = imageProfile;
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public float getDonation() {
        return donation;
    }

    public void setDonation(float donation) {
        this.donation = donation;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}