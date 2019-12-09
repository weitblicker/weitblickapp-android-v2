package com.example.weitblickapp_android.ui.location;

import java.util.Date;

public class Tour {

  private Date startTime;
  private Date endTime;
  private String userToken;
  private double eurosTotal;
  private double distanceTotal;
  private int projectId;

  public Tour(int projectId){
    this.projectId = projectId;

  }

  public Tour(Date startTime, Date endTime, String userToken, double eurosTotal, double distanceTotal) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.userToken = userToken;
    this.eurosTotal = eurosTotal;
    this.distanceTotal = distanceTotal;
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
}
