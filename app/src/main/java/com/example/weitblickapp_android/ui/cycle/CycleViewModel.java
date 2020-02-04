package com.example.weitblickapp_android.ui.cycle;

import androidx.lifecycle.ViewModel;

public class CycleViewModel extends ViewModel {

    String currentAmount;
    String cycleDonation;
    int cyclist;
    String km_sum;

    public CycleViewModel(String currentAmount, String cycleDonation, int cyclist, String km_sum){
        this.currentAmount = currentAmount;
        this.cycleDonation = cycleDonation;
        this.cyclist = cyclist;
        this.km_sum = km_sum;
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

    public int getCyclist() {
        return cyclist;
    }

    public void setCyclist(int cyclist) {
        this.cyclist = cyclist;
    }

    public String getKm_sum() {
        return km_sum;
    }

    public void setKm_sum(String km_sum) {
        this.km_sum = km_sum;
    }
}
