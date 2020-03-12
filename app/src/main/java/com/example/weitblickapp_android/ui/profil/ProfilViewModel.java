package com.example.weitblickapp_android.ui.profil;

import androidx.lifecycle.ViewModel;

public class ProfilViewModel extends ViewModel {

    private final String imageBaseUrl = "https://weitblicker.org/";

    private int id;
    private String passwort;
    private String donation;
    private String km;
    private String imageUrl;
    private String userName;


    public ProfilViewModel() {
    }

    public ProfilViewModel(String userName, double donation, double km, String imageUrl){
        this.userName=userName;
        this.donation= String.format("%.2f", donation).concat(" €");
        this.km= String.format("%.2f", km).concat(" km");

        if(imageUrl.equals("null")){
            this.imageUrl = "";
        }else {
            this.imageUrl = imageBaseUrl.concat(imageUrl);
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ProfilViewModel{" +
                "donation='" + donation + '\'' +
                ", km='" + km + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
