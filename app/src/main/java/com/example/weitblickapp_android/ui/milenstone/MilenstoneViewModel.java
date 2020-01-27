package com.example.weitblickapp_android.ui.milenstone;

import androidx.lifecycle.ViewModel;

public class MilenstoneViewModel extends ViewModel {

    String date;
    String title;
    String description;

    public MilenstoneViewModel(){};

    public MilenstoneViewModel(String title, String date, String description){
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
