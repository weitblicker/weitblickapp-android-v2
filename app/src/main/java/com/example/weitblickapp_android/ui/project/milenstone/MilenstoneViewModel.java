package com.example.weitblickapp_android.ui.project.milenstone;

import androidx.lifecycle.ViewModel;

public class MilenstoneViewModel extends ViewModel {

    String date;
    String title;
    String description;
    boolean reached;

    public MilenstoneViewModel(){};

    public MilenstoneViewModel(String title, String date, String description, boolean reached){
        this.title = title;
        this.date = date;
        this.description = description;
        this.reached = reached;
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

    public boolean isReached() {
        return reached;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
