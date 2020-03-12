package com.example.weitblickapp_android.ui.more.credits;

import androidx.lifecycle.ViewModel;

public class MemberViewModel extends ViewModel {

    String name;
    String role;
    String image;
    String email;
    String text;

    public MemberViewModel(){

    }

    public MemberViewModel(String name, String email, String text, String role, String image){
        this.name = name;
        this.role = role;
        this.email = email;
        this.image = image;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
