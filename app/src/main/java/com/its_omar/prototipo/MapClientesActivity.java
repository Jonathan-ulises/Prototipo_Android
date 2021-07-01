package com.its_omar.prototipo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.File;
import java.lang.ref.WeakReference;

public class MapClientesActivity extends AppCompatActivity {


    //Mapa enbebido
    private Map mapClientes = null;

    private PositioningManager posManager;

    //Map Fragment
    private AndroidXMapFragment mapFragment = null;

    boolean paused = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_clientes);
        initMapa();

        //mapBinding.mapCl.
    }

    private void initMapa() {
        mapFragment = (AndroidXMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCl);

        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps");

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if(error ==  OnEngineInitListener.Error.NONE) {
                    mapClientes = mapFragment.getMap();

                    mapClientes.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0), Map.Animation.NONE);

                    mapClientes.setZoomLevel((mapClientes.getMaxZoomLevel() + mapClientes.getMinZoomLevel()) / 2);

                    posicionarVisitador();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR DE MAPA " + error.getDetails(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //posicionarVisitador();
    }

    private void posicionarVisitador() {
        try {
            posManager = PositioningManager.getInstance();

            posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);

            posManager.addListener(new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
        } catch (Exception e){
            Log.i("mapa", e.getMessage());
        }
    }

    private final PositioningManager.OnPositionChangedListener positionListener = new PositioningManager.OnPositionChangedListener() {

        @Override
        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, @Nullable  GeoPosition geoPosition, boolean b) {
            if(!paused) {
                mapClientes.setCenter(geoPosition.getCoordinate(), Map.Animation.NONE);
            }
        }

        @Override
        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        paused = false;
        if (posManager != null) {
            posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
        }
    }

    @Override
    protected void onPause() {
        if(posManager != null) {
            posManager.stop();
        }
        super.onPause();

        paused = true;
    }

    @Override
    protected void onDestroy() {
        if(posManager != null) {
            posManager.removeListener(positionListener);
        }

        mapClientes = null;
        super.onDestroy();
    }
}