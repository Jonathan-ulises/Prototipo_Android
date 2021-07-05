package com.its_omar.prototipo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.fragments.adapters.ClientesVisitaAdapter;
import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Empleado;
import com.its_omar.prototipo.model.resultClienteService.ClientesJSONResult;
import com.its_omar.prototipo.model.resultClienteService.Resultado;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.its_omar.prototipo.model.Constantes.INTENT_ID_EMPLEADO;


public class MapClientesActivity extends AppCompatActivity {

    private LottieAnimationView progressView;
    private View backgroudProgress;

    //Mapa enbebido
    private Map mapClientes = null;
    private PositioningManager posManager;

    //Map Fragment
    private AndroidXMapFragment mapFragment = null;
    boolean paused = false;
    boolean markadorGenerado = false;
    protected static List<Cliente_por_visitar> listClientes = new ArrayList<>();
    private boolean isUsuarioPosicionado = false;
    private GeoCoordinate userCoord;

    int idEmpleado;

    //Bottom Sheet
    private BottomSheetBehavior mBottomSheetBehavior;
    private boolean mIsEpanded;
    private ImageButton btnExpand;
    private ImageButton btnRefresh;
    private RecyclerView rclClienteInMap;
    ClientesVisitaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_clientes);

        //View viewBotoom = View.inflate(this, R.layout.layout_bottom_sheet_clientes, null);
        View ad = findViewById(R.id.includeBottom);

        btnExpand = ad.findViewById(R.id.btnResize);
        btnRefresh = ad.findViewById(R.id.btnRefresh);
        adapter = new ClientesVisitaAdapter();
        rclClienteInMap = ad.findViewById(R.id.rclClienteVisita);
        rclClienteInMap.setLayoutManager(new LinearLayoutManager(this));
        rclClienteInMap.setAdapter(adapter);


        mBottomSheetBehavior = BottomSheetBehavior.from(ad);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mIsEpanded = true;
                        btnExpand.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_expand_more));
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mIsEpanded = false;
                        btnExpand.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_expand_less));
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        progressView = findViewById(R.id.prBarMap);
        backgroudProgress = findViewById(R.id.bacgrPrMap);

        View view = getCurrentFocus();

        Bundle extras = getIntent().getExtras();
        idEmpleado = extras.getInt(INTENT_ID_EMPLEADO);

        initMapa();

        btnExpand.setOnClickListener(v -> {
            if(mIsEpanded){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        btnRefresh.setOnClickListener(view1 -> initMapa());
    }

    /**
     * Inicializa el mapa
     */
    private void initMapa() {
        backgroudProgress.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.VISIBLE);
        progressView.playAnimation();

        mapFragment = (AndroidXMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCl);

        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps");

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if(error ==  OnEngineInitListener.Error.NONE) {
                    mapClientes = mapFragment.getMap();

                    //mapClientes.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0), Map.Animation.NONE);

                    mapClientes.setZoomLevel((mapClientes.getMaxZoomLevel() + mapClientes.getMinZoomLevel()) / 2);

                    if(!isUsuarioPosicionado) {
                        posicionarVisitador();
                        isUsuarioPosicionado = true;
                    }

                    mapClientes.setCenter(userCoord, Map.Animation.NONE);
                    cargarClientes();

                    backgroudProgress.setVisibility(View.GONE);
                    progressView.pauseAnimation();
                    progressView.setVisibility(View.GONE);
                //posicionarClientes();

                } else {
                    Toast.makeText(getApplicationContext(), "ERROR DE MAPA " + error.getDetails(), Toast.LENGTH_LONG).show();
                }
            }
        });
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
                    userCoord = geoPosition.getCoordinate();
                    mapClientes.setCenter(geoPosition.getCoordinate(), Map.Animation.NONE);
                    mapClientes.setZoomLevel((mapClientes.getMaxZoomLevel() + mapClientes.getMinZoomLevel()) / 1.5);
                    mapClientes.addMapObject(agregarMarcador(geoPosition.getCoordinate().getLatitude(), geoPosition.getCoordinate().getLongitude(), false));
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
    private MapMarker agregarMarcador(double lat, double lon, boolean isCliente) {
        MapMarker markerUser = new MapMarker();
        Image imgMarkerUser = new Image();

        try {
            if(!isCliente) {
                imgMarkerUser.setImageResource(R.drawable.user_marker);
            } else {
                imgMarkerUser.setImageResource(R.drawable.cliente_marker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        markerUser.setIcon(imgMarkerUser);
        markerUser.setCoordinate(new GeoCoordinate(lat, lon));
        markerUser.

        return markerUser;
    }

    /**
     * Carga los clientes en el mapa y en la lista
     */
    private void cargarClientes(){
        WebService api = ServiceRetrofit.getInstance().getSevices();
        api.getClientesEmpleado(new Empleado(idEmpleado)).enqueue(new Callback<ClientesJSONResult>() {
            @Override
            public void onResponse(Call<ClientesJSONResult> call, Response<ClientesJSONResult> response) {

                if(response.body().isOk()){
                    if(response.body() != null){
                        if(response.body().getResultado() != null){

                            //listClientes = jsonResponse(response.body());
                            mapClientes.addMapObjects(agregarMarcadoresClientes(jsonResponse(response.body())));
                            adapter.submitList(jsonResponse(response.body()));
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getApplication(), "Sin Cliente Asignados", Toast.LENGTH_SHORT).show();
                           /* clientesBinding.swipeRfshList.setRefreshing(false);*/
                        }
                    }
                }

            }

            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onFailure(Call<ClientesJSONResult> call, Throwable t) {
                Snackbar.make(getCurrentFocus(), "Ha sucedido un error", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                t.printStackTrace();
            }
        });


    }

    /**
     * Procesa la respuesta del servidor, esto para respuesta de consulta de clientes por visitar
     * @param res respuesta del servicio {@link ClientesJSONResult}
     * @return Lista de clientes por visitar {@link List <Cliente_por_visitar>}
     */
    private List<Cliente_por_visitar> jsonResponse(ClientesJSONResult res){
        List<Cliente_por_visitar> clList = new ArrayList<>();

        for (Resultado r:res.getResultado()) {
            Cliente_por_visitar cl = new Cliente_por_visitar();
            cl.setIdCliente(r.getJson().getId_Cliente());
            cl.setNombre(r.getJson().getNombre());
            cl.setaPaterno(r.getJson().getaPaterno());
            cl.setaMaterno(r.getJson().getaMaterno());
            cl.setIne(r.getJson().getFotoINE());
            cl.setLat(r.getJson().getLati());
            cl.setLon(r.getJson().getLongt());


            /*Como no recibe estos datos del servidor, se pone "sin .."
            para que no de errores el DIFF CALL*/
            cl.setCasa("sin casa");
            cl.setFirma("sin firma");
            clList.add(cl);
        }

        return clList;
    }

    /**
     * Genrera lista de marcadores de clientes para mostrar en el mapa
     * @param lsCl Lista de clientes por visitar {@link List<Cliente_por_visitar>}
     * @return Lista de opjetos de mapa {@link List<MapObject>}
     */
    private List<MapObject> agregarMarcadoresClientes(List<Cliente_por_visitar> lsCl) {
        List<MapObject> listaMarcadores = new ArrayList<>();
        for(Cliente_por_visitar c : lsCl) {
            listaMarcadores.add(agregarMarcador(c.getLat(), c.getLon(), true));
        }

        return listaMarcadores;
    }
}