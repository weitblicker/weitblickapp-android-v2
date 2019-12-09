package com.example.weitblickapp_android.ui.location;

import androidx.lifecycle.ViewModel;

public class EndViewModel extends ViewModel {

    int id;
    String sponsor;
    String project;

    EndViewModel(int id, String sponsor, String project){
        this.id = id;
        this.sponsor = sponsor;
        this.project = project;

    }
    EndViewModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
