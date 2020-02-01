package com.example.weitblickapp_android.ui.cycle;

import androidx.lifecycle.ViewModel;

public class CycleViewModel extends ViewModel {

    String currentAmount;
    String cycleDonation;
    boolean finished;
    int donationID;
    String goalAmount;

    public CycleViewModel(String currentAmount, String cycleDonation, boolean finished, int donationID, String goalAmount){
        this.currentAmount = currentAmount;
        this.cycleDonation = cycleDonation;
        this.finished = finished;
        this.donationID = donationID;
        this.goalAmount = goalAmount;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCycleDonation() {
        return cycleDonation;
    }

    public void setCycleDonation(String cycleDonation) {
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

    public String getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(String goalAmount) {
        this.goalAmount = goalAmount;
    }
}
