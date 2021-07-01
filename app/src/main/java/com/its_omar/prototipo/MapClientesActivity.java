package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.File;

public class MapClientesActivity extends AppCompatActivity {


    //Mapa enbebido
    private Map mapClientes = null;

    //Map Fragment
    private AndroidXMapFragment mapFragment = null;

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
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR DE MAPA " + error.getDetails(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}