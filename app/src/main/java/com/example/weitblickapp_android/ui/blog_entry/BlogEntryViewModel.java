package com.example.weitblickapp_android.ui.blog_entry;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;

public class BlogEntryViewModel extends ViewModel {

    final private static SimpleDateFormat formatterRead = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private int id;
    private String title;
    private String text;
    private  int image_id;
    String  published;
    String  updated_at;
    private int location_id;


    public BlogEntryViewModel(int id, String title, String text, String published) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.published = published;

    }

    public BlogEntryViewModel(int id, String title, String text, int image_id, String created_at, String updated_at, int location_id) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image_id = image_id;
        this.published = created_at;
        this.updated_at = updated_at;
        this.location_id = location_id;
    }

    public BlogEntryViewModel() {
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

    public String getText(){return this.text;}

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }


    public String getCreated_at() {
        return published;
    }

    public void setCreated_at(String datetime) {

            this.published = datetime;

    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    @Override
    public String toString() {
        return "BlogEntryViewModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image_id=" + image_id +
                ", created_at=" + published +
                ", updated_at=" + updated_at +
                ", location_id=" + location_id +
                '}';
    }
}