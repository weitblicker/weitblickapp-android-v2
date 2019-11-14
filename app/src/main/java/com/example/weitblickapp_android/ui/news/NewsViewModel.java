package com.example.weitblickapp_android.ui.news;

import android.location.Location;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsViewModel extends ViewModel {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private int id;
    private String title;
    private String text;
    private String teaser;
    private int image_id;
    private String date;
    private Date updated_at;
    private Location location;
    private String imageUrl;

    public NewsViewModel() {

    }

    public NewsViewModel(int id, String title, String text, String teaser, String imageUrl) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.teaser = teaser;
        this.imageUrl = imageUrl;
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

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

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