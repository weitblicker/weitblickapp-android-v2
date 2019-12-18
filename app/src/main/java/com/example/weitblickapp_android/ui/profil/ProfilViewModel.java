package com.example.weitblickapp_android.ui.profil;

import androidx.lifecycle.ViewModel;

public class ProfilViewModel extends ViewModel {

    private int id;
    private String email;
    private String passwort;
    private String donation;
    private String km;

    public ProfilViewModel() {
    }

    public ProfilViewModel(String email, String passwort, String donation, String km){
        this.email=email;
        this.passwort=passwort;
        this.donation=donation;
        this.km=km;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getDonation() {
        return donation;
    }

    public void setDonation(String donation) {
        this.donation = donation;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }
}
