package com.its_omar.prototipo.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;

public class CapturarUbicacion implements LocationListener {




    public static final String LOG_TAG = "CapturarUbicacions";
    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 100;
    private int statusProvider;

    private Context context;
    private LocationManager locationManager;
    @Nullable
    private PlatformLocationListener platformLocationListener;

    public interface PlatformLocationListener {
        void onLocationUpdate(Location location, int status);
    }

    public  CapturarUbicacion(Context ctx) {
        this.context = ctx;
    }


    /**
     * Escucha las actualizaciones de la ubicacion del dispositivo
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (platformLocationListener != null) {
            int statusProviderAnPosition = statusProvider;
            platformLocationListener.onLocationUpdate(location, statusProviderAnPosition);
            Log.d("geo", location.getLatitude() + " " + location.getLongitude());

            //Cuando detecte la primer coordenada, se detiene el evento.
            stopLocating();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d(LOG_TAG, "PlatformPositioningProvider status: AVAILABLE");
                statusProvider = LocationProvider.AVAILABLE;
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d(LOG_TAG, "PlatformPositioningProvider status: OUT_OF_SERVICE");
                statusProvider = LocationProvider.OUT_OF_SERVICE;
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d(LOG_TAG, "PlatformPositioningProvider status: TEMPORARILY_UNAVAILABLE");
                statusProvider = LocationProvider.TEMPORARILY_UNAVAILABLE;
                break;
            default:
                statusProvider = 4;
                Log.d(LOG_TAG, "PlatformPositioningProvider status: UNKNOWN");
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    /**
     * Inicia a localizar la posicion del usuario
     * @param locationCallback
     */
    public void starLocating(PlatformLocationListener locationCallback){
        if (this.platformLocationListener != null){
            stopLocating();
            //throw new RuntimeException("Please stop locating before starting again.");
        }

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Positioning permissions denied.");
            return;
        }

        this.platformLocationListener = locationCallback;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS, 1, this);
        } else {
            Log.d(LOG_TAG, "Positioning not possible.");
            statusProvider = 9;
            stopLocating();
        }
    }

    /**
     * Deteniene la obteption de la ubicacion
     */
    public void stopLocating() {
        if(locationManager == null) {
            return;
        }

        locationManager.removeUpdates(this);
        platformLocationListener = null;
    }
}
