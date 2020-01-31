package com.example.weitblickapp_android.ui.partner;

import androidx.lifecycle.ViewModel;

public class ProjectPartnerViewModel extends ViewModel {

    String description;
    String name;
    String weblink;
    String logo;
    int id;

    public ProjectPartnerViewModel(){};

    public ProjectPartnerViewModel(String name, String dec, String weblink, String logo){
        this.description = dec;
        this.name = name;
        this.weblink = weblink;
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
