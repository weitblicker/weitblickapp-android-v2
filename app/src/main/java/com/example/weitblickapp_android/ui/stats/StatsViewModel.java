package com.example.weitblickapp_android.ui.stats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatsViewModel extends ViewModel {

    private String distance;
    private String donation;
    private String duration;
    private Date date;

    public StatsViewModel(){

    }

    public StatsViewModel(String distance, String donation, String duration, String date) {
        this.distance = distance;
        this.donation = donation;
        this.duration = duration;

        try {
            this.date = formatterRead.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDonation() {
        return donation;
    }

    public void setDonation(String donation) {
        this.donation = donation;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return formatterWrite.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}