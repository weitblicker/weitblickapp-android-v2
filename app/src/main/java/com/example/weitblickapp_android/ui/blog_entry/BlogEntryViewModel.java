package com.example.weitblickapp_android.ui.blog_entry;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BlogEntryViewModel extends ViewModel {

    int id;
    String title;
    String text;
    int image_id;
    Date created_at;
    Date updated_at;
    int location_id;

    private MutableLiveData<String> mText;

    public BlogEntryViewModel(int id, String title, String text, int image_id, Date created_at, Date updated_at, int location_id) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image_id = image_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.location_id = location_id;
    }

    public BlogEntryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is blog_entry fragment");
    }

    public LiveData<String> getText() {
        return mText;
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

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

}