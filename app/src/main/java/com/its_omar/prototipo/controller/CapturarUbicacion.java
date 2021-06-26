package com.its_omar.prototipo.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class CapturarUbicacion implements LocationListener {



    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 100;

    private Context ctx;
    private int status;
    private LocationManager locationManager;

    private CapturarUbicacionListener capturarUbicacionListener;

    public interface CapturarUbicacionListener {
        void onLocationUpdate(Location location, int provStatus);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (capturarUbicacionListener != null) {
            capturarUbicacionListener.onLocationUpdate(location, status);
            //STOP
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                status = LocationProvider.AVAILABLE;
                break;
            case LocationProvider.OUT_OF_SERVICE:
                status = LocationProvider.OUT_OF_SERVICE;
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                status = LocationProvider.TEMPORARILY_UNAVAILABLE;
            default:
                status = 4;

        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public void startLocation(CapturarUbicacionListener locationCallback){
        if (this.capturarUbicacionListener != null) {
            stopCapturelocation();
        }

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS, 1, this);
        } else {
            status = 9;
            stopCapturelocation();
        }
    }

    public void stopCapturelocation(){
        if(locationManager == null) {
            return;
        }

        locationManager.removeUpdates(this);
        capturarUbicacionListener = null;
    }
}
