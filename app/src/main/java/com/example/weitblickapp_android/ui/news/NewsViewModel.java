package com.example.weitblickapp_android.ui.news;

import com.example.weitblickapp_android.ui.location.LocationViewModel;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private int id;
    private String title;
    private String text;
    private int image_id;
    private Date created_at;
    private Date updated_at;
    private LocationViewModel location;

    public NewsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fragment_news fragment");
    }

    public NewsViewModel(int id, String title, String text, int image_id) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image_id = image_id;
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

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public LocationViewModel getLocation() {
        return location;
    }

    public void setLocation(LocationViewModel location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "NewsViewModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image_id=" + image_id +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    public LiveData<String> getText() {
        return mText;
    }

}