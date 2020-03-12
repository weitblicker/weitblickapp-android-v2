package com.example.weitblickapp_android.ui.event;

public class EventLocation {

    private String name;
    private String address;
    private String description;
    private double lat;
    private double lng;


    public EventLocation(String name, String address, double lat, double lng, String description) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() { return lat; }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EventLocation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
