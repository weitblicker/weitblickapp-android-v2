package com.example.weitblickapp_android.ui.event;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventViewModel extends ViewModel {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");


    int id;
    String title;
    int location_id;
    Date eventDateStart;
    Date eventDateEnd;
    String time;
    String text;


    public EventViewModel(int id, String title) {
        this.id = id;
        this.title = title;
    }


    public EventViewModel(int id, String title, String description, String startDate, String endDate, int location_id) {
        this.id = id;
        this.title = title;
        this.text = description;
        this.time = time;
        this.location_id = location_id;
        try {
            this.eventDateStart = formatterRead.parse(startDate);
            this.eventDateEnd = formatterRead.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public int getLocation() {
        return location_id;
    }

    public void setLocation(int location) {
        this.location_id = location;
    }

    public String getEventStartDate() {
        return formatterWrite.format(eventDateStart);
    }

    public void setEventStartDate(Date date) { this.eventDateStart = date; }

    public String getEventEndDate() {
        return formatterWrite.format(eventDateStart);
    }

    public void setEventEndDate(Date date) {
        this.eventDateEnd = date;
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

    @Override
    public String toString() {
        return "EventViewModel{" +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location_id + '\'' +
                ", eventStartDate=" + eventDateStart +
                ", eventEndDate='" + eventDateEnd + '\'' +
                ", time='" + time + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}