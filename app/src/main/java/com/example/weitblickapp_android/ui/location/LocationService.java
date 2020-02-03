package com.example.weitblickapp_android.ui.location;

import android.location.Location;
import android.util.Log;

import mad.location.manager.lib.Interfaces.LocationServiceInterface;
import mad.location.manager.lib.Interfaces.LocationServiceStatusInterface;
import mad.location.manager.lib.Services.KalmanLocationService;
import mad.location.manager.lib.Services.ServicesHelper;

public class LocationService extends KalmanLocationService implements LocationServiceInterface, LocationServiceStatusInterface  {


    public LocationService(){
        ServicesHelper.addLocationServiceInterface(this);
    }

    @Override
    public void locationChanged(Location location) {
        Log.e("KAAAAAAAALMAN!!!!", location.getLatitude() +"");
    }


    @Override
    public void serviceStatusChanged(KalmanLocationService.ServiceStatus serviceStatus) {

    }

    @Override
    public void GPSStatusChanged(int i) {

    }

    @Override
    public void GPSEnabledChanged(boolean b) {

    }

    @Override
    public void lastLocationAccuracyChanged(float v) {

    }
}