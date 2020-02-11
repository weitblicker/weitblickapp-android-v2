package com.example.weitblickapp_android.ui.event;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class EventViewModel extends ViewModel {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final private static SimpleDateFormat formatterWrite = new SimpleDateFormat("dd.MM.yyyy");


    private int id;
    private String title;
    private EventLocation location;
    private Date eventDateStart;
    private Date eventDateEnd;
    private String time;
    private String text;
    private String hostName;
    private ArrayList<String> imageUrls;


    public EventViewModel(int id, String title) {
        this.id = id;
        this.title = title;
    }


    public EventViewModel(int id, String title, String description, String startDate, String endDate, String hostName, EventLocation location, ArrayList<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.text = description;
        this.hostName = hostName;
        this.time = time;
        this.location = location;
        try {
            this.eventDateStart = formatterRead.parse(startDate);
            this.eventDateEnd = formatterRead.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.imageUrls = imageUrls;
    }

    public EventLocation getLocation() { return location; }

    public void setLocation(EventLocation location) { this.location = location; }

    public String getHostName() { return hostName; }

    public void setHostName(String hostName) { this.hostName = hostName; }

    public ArrayList<String> getImageUrls() { return imageUrls; }

    public void setImageUrls(ArrayList<String> imageUrls) { this.imageUrls = imageUrls; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String formatToTimeRange() {

        LocalDate eventDate =  eventDateStart.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate inTwoDays = today.plusDays(2);
        LocalDate inThreeDays = today.plusDays(3);


        if(eventDate.equals(yesterday)){
            return "Gestern";
        }
        if(eventDate.equals(today)){
            return "Heute";
        }
        if(eventDate.equals(tomorrow)){
            return "Morgen";
        }
        if(eventDate.equals(inTwoDays)){
            return "In 2 Tagen";
        }
        if(eventDate.equals(inThreeDays)){
            return "In 3 Tagen";
        }
        return getEventStartDate();
    }



    @Override
    public String toString() {
        return "EventViewModel{" +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", eventStartDate=" + eventDateStart +
                ", eventEndDate='" + eventDateEnd + '\'' +
                ", time='" + time + '\'' +
                ", text='" + text + '\'' +
                ", images='" + imageUrls + '\'' +
                '}';
    }
}