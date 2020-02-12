package com.example.weitblickapp_android.ui.stats;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsViewModel extends ViewModel {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");


    private int tourId;
    private int projectId;
    private double distance;
    private double donation;
    private int durationMinutes;
    private int durationSeconds;
    private int durationHours;
    private Date date;

    public StatsViewModel(){

    }

    public StatsViewModel(int projectId, int tourId, double distance, double donation, int duration, String date) {
        this.projectId = projectId;
        this.tourId = tourId;
        this.distance = distance;
        this.donation = donation;

        Log.e("DURATION SECONDS", duration+"!");

        this.durationSeconds = (duration%60);
        int durationHoursTemp = (duration/60);
        int durationMinutesTemp = (durationHoursTemp % 60);
        this.durationMinutes = durationMinutesTemp;
        this.durationHours = duration/3600;

        try {
            this.date = formatterRead.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getSecondsAsString(){
        if(this.durationSeconds < 10){
            return "0"+ Integer.toString(this.durationSeconds);
        }else{
            return Integer.toString(this.durationSeconds);
        }
    }

    public String getMinutesAsString(){
        if(this.durationMinutes < 10){
            return "0"+ Integer.toString(this.durationMinutes);
        }else{
            return Integer.toString(this.durationMinutes);
        }
    }

    public String getHoursAsString(){
        if(this.durationHours < 10){
            return "0"+ Integer.toString(this.durationHours);
        }else{
            return Integer.toString(this.durationHours);
        }
    }



    public String getDurationAsString(){
        return getHoursAsString() + ":" + getMinutesAsString() + ":" + getSecondsAsString() + " h";
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDonation() {
        return donation;
    }

    public void setDonationMinutes(double donation) {
        this.donation = donation;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDuration(int duration) {
        this.durationMinutes = duration;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getDate() {
        return formatterWrite.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }
}