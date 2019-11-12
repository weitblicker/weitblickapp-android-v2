package com.example.weitblickapp_android.ui.news;

import android.location.Location;

import java.util.Date;

import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    private int id;
    private String title;
    private String text;
    private int image_id;
    private String date;
    private Date updated_at;
    private Location location;

    public NewsViewModel() {

    }

    public NewsViewModel(int id, String title, String text, int image_id) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image_id = image_id;
    }

    public NewsViewModel(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {return text; }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {this.date = date; }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "NewsViewModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image_id=" + image_id +
                ", date=" + date +
                ", updated_at=" + updated_at +
                '}';
    }



}