package com.example.weitblickapp_android.ui.stats;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsViewModel extends ViewModel {

    private int tourId;
    private int projectId;
    private double distance;
    private double donation;
    private int duration;
    private Date date;

    public StatsViewModel(){

    }

    public StatsViewModel(int projectId, int tourId, double distance, double donation, int duration, String date) {
        this.projectId = projectId;
        this.tourId = tourId;
        this.distance = distance;
        this.donation = donation;
        this.duration = (duration/60);

        try {
            this.date = formatterRead.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDonation() {
        return donation;
    }

    public void setDonation(double donation) {
        this.donation = donation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return formatterWrite.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}