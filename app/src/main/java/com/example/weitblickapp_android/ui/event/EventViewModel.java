package com.example.weitblickapp_android.ui.event;

import com.example.weitblickapp_android.ui.location.LocationViewModel;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    int id;
    String name;
    LocationViewModel location;
    Date eventDate;



    public EventViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is event fragment");
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

    public LocationViewModel getLocation() {
        return location;
    }

    public void setLocation(LocationViewModel location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public LiveData<String> getText() {
        return mText;
    }
}