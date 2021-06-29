package com.its_omar.prototipo.controller;

import android.content.Context;
import android.provider.Settings;

public class CapturarUbicacion {

    public static boolean checkIfLocationOpened(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }
}
