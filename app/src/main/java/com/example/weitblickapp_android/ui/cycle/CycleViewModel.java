package com.example.weitblickapp_android.ui.cycle;

import androidx.lifecycle.ViewModel;

public class CycleViewModel extends ViewModel {

    float currentAmount;
    float cycleDonation;
    boolean finished;
    int donationID;
    float goalAmount;

    public CycleViewModel(float currentAmount, float cycleDonation, boolean finished, int donationID, float goalAmount){
        this.currentAmount = currentAmount;
        this.cycleDonation = cycleDonation;
        this.finished = finished;
        this.donationID = donationID;
        this.goalAmount = goalAmount;
    }

    public float getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(float currentAmount) {
        this.currentAmount = currentAmount;
    }

    public float getCycleDonation() {
        return cycleDonation;
    }

    public void setCycleDonation(float cycleDonation) {
        this.cycleDonation = cycleDonation;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getDonationID() {
        return donationID;
    }

    public void setDonationID(int donationID) {
        this.donationID = donationID;
    }

    public float getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(float goalAmount) {
        this.goalAmount = goalAmount;
    }
}
