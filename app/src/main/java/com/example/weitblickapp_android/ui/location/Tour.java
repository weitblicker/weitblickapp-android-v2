package com.example.weitblickapp_android.ui.location;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class Tour {

  private Date startTime;
  private Date endTime;
  private String userToken;
  private double eurosTotal;
  private double distanceTotal;
  private ArrayList<Location> locations;
  private int projectId;

  public Tour(int projectId){
    this.projectId = projectId;
    this.locations = new ArrayList<Location>();
  }

  public Tour(Date startTime, Date endTime, String userToken, double eurosTotal, double distanceTotal) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.userToken = userToken;
    this.eurosTotal = eurosTotal;
    this.distanceTotal = distanceTotal;
    this.locations = new ArrayList<Location>();
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getUserToken() {
    return userToken;
  }

  public void setUserToken(String userToken) {
    this.userToken = userToken;
  }

  public double getEurosTotal() {
    return eurosTotal;
  }

  public void setEurosTotal(double eurosTotal) {
    this.eurosTotal = eurosTotal;
  }

  public double getDistanceTotal() {
    return distanceTotal;
  }

  public void setDistanceTotal(double distanceTotal) {
    this.distanceTotal = distanceTotal;
  }

  public ArrayList<Location> getLocations() {
    return locations;
  }

  public void setLocations(ArrayList<Location> locations) {
    this.locations = locations;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
}
