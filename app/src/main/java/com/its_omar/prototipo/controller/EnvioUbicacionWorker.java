package com.its_omar.prototipo.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import static android.content.Context.LOCATION_SERVICE;
//import static com.its_omar.prototipo.model.Constantes.TAG_INFO_ENVIO_UBICACION;

public class EnvioUbicacionWorker extends AsyncTask<Context, Void, Void> implements LocationListener {

    public static final String LOG_TAG = "CapturarUbicacions";
    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 100;
    private LocationManager locationManager;
    private SharedPreferencesApp sharedPreferencesApp;
    Socket socket;
    double lat, lon;
    int idEmpleado;

    JsonObject obj;


    /**
     * Escucha las actualizaciones de la ubicacion del dispositivo
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.lat = location.getLatitude();
        this.lon = location.getLongitude();

        this.obj = new JsonObject();
        this.obj.addProperty("latitude", lat);
        this.obj.addProperty("longitud", lon);
        this.obj.addProperty("idEmpleado", idEmpleado);
    }

    /**
     * Inicia a localizar la posicion del usuario
     */
    public void starLocating(Context ctx){
        sharedPreferencesApp = SharedPreferencesApp.getInstance(ctx);
        this.idEmpleado = sharedPreferencesApp.getUsuarioLogeado().getId_empleado();

        if (ActivityCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ctx,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Positioning permissions denied.");
            return;
        }
        locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS, 1, this);
        } else {
            Log.d(LOG_TAG, "Positioning not possible.");
            stopLocating();
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        try {
            starLocating(contexts[0]);
            InetAddress ipServidor =InetAddress.getByName("http://72.167.220.178/Prototipo/enviarCoordenadas");
            this.socket = new Socket(ipServidor, 34123);

            PrintStream salida = new PrintStream(socket.getOutputStream());
            salida.print(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deteniene la obteption de la ubicacion
     */
    public void stopLocating() {
        if (locationManager == null) {
            return;
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(this);
    }
}
