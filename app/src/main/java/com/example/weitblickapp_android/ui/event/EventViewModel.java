package com.example.weitblickapp_android.ui.event;

import android.location.Location;
import java.util.Date;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    int id;
    String name;
    String location;
    Date eventDate;
    String date;
    String time;
    String text;

    public EventViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is maps fragment");
    }

    public EventViewModel(int id, String title) {
        this.id = id;
        this.name = title;
    }


    public EventViewModel(int id, String title, String description, String time, String date, String location) {
        this.id = id;
        this.name = title;
        this.text = description;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public MutableLiveData<String> getmText() {
        return mText;
    }

    public void setmText(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}