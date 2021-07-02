package com.its_omar.prototipo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.here.android.mpa.cluster.ClusterLayer;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.IconCategory;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MapClientesActivity extends AppCompatActivity {


    //Mapa enbebido
    private Map mapClientes = null;

    private PositioningManager posManager;

    //Map Fragment
    private AndroidXMapFragment mapFragment = null;

    boolean paused = false;

    boolean markadorGenerado = false;



    private ClusterLayer cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_clientes);


        initMapa();



        //mapBinding.mapCl.
    }

    /**
     * Inicializa el mapa
     */
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

    /**
     * Obtiene la posiion del visitador
     */
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
                if(!markadorGenerado) {
                    mapClientes.setCenter(geoPosition.getCoordinate(), Map.Animation.NONE);
                    mapClientes.setZoomLevel((mapClientes.getMaxZoomLevel() + mapClientes.getMinZoomLevel()) / 1.5);
                    mapClientes.addMapObject(agregarMarcador(geoPosition.getCoordinate().getLatitude(), geoPosition.getCoordinate().getLongitude()));
                    markadorGenerado = true;
                    posManager.stop();
                    posManager.removeListener(positionListener);
                }
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

    /**
     * genera marcadores para e mapa
     * @param lat Latitude
     * @param lon Longitud
     * @return Marcador del mapa {@link MapMarker}
     */
    private MapMarker agregarMarcador(double lat, double lon) {
        MapMarker markerUser = new MapMarker();
        Image imgMarkerUser = new Image();
        IconCategory ic = IconCategory.ALL;
        try {
            imgMarkerUser.setImageResource(R.drawable.user_marker);
        } catch (IOException e) {
            e.printStackTrace();
        }

        markerUser.setIcon(imgMarkerUser);
        markerUser.setCoordinate(new GeoCoordinate(lat, lon));

        return markerUser;
    }
}