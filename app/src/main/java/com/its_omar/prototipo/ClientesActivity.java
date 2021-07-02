package com.its_omar.prototipo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityClientesBinding;
import com.its_omar.prototipo.fragments.adapters.ClientesVisitaAdapter;
import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Empleado;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.Usuario;
import com.its_omar.prototipo.model.resultClienteService.ClientesJSONResult;
import com.its_omar.prototipo.model.resultClienteService.Resultado;
import com.nokia.maps.restrouting.GeoCoordinate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.its_omar.prototipo.model.Constantes.ID_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.NOMBRE_CLIENTE_EXTRA_KEY;
import static com.its_omar.prototipo.model.Constantes.PERMISOS;
import static com.its_omar.prototipo.model.Constantes.generarNombreCompleto;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding clientesBinding;
    private Context ctx;
    ClientesVisitaAdapter adapter;
    SharedPreferencesApp sharedPreferencesApp;
    Usuario usu;

    //Lista de coordanadas de los clientes
    ArrayList<Cliente_por_visitar> userInMap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientesBinding = ActivityClientesBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(clientesBinding.getRoot());

        validarPermisos(this);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.ctx = this;

        setSupportActionBar(clientesBinding.toolbar);

        //asignarTipografia(face);

        //SharePreferences -> idEmpleado Logeado
        sharedPreferencesApp = SharedPreferencesApp.getInstance(this);
        usu = sharedPreferencesApp.getUsuarioLogeado();

        //RecycleView -> configuracion
        adapter = new ClientesVisitaAdapter();
        clientesBinding.rclClienteVisita.setLayoutManager(new LinearLayoutManager(this));
        clientesBinding.rclClienteVisita.setAdapter(adapter);

        //ConsultaServidor
        consultarListaCliente(usu.getId_empleado());

        //Evento click del item de la lista
        adapter.setOnItemClickListener(cliente -> {
            //Toast.makeText(this, cliente.getNombre(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, VerificacionVisitaActivity.class);
            String nombre = generarNombreCompleto(cliente);
            intent.putExtra(NOMBRE_CLIENTE_EXTRA_KEY, nombre);
            intent.putExtra(ID_CLIENTE, cliente.getIdCliente());
            startActivity(intent);
        });


        clientesBinding.swipeRfshList.setOnRefreshListener(() -> {
            try {
                consultarListaCliente(usu.getId_empleado());
                Toast.makeText(this, "Refrescado", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("ERRORCL", "error de consulta", e);
            }
        });

        clientesBinding.btnCargarMaoa.setOnClickListener(view -> {
            //MAPA
            Intent intent = new Intent(this, MapClientesActivity.class);
            intent.putParcelableArrayListExtra()
            startActivity(intent);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                if (usu != null) {
                    WebService api = ServiceRetrofit.getInstance().getSevices();
                    api.logoutApp(usu.getNombreUsuario()).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if(response.body().isOk()){
                                //ConsultasComunes.registrarAccionBitacora("Login", "Cerrar Sesion", usu.getId_empleado());
                                sharedPreferencesApp.borrarPreferences();
                                finish();
                            } else {
                                Snackbar.make(clientesBinding.getRoot(), "Ha sucedido un error", Snackbar.LENGTH_SHORT)
                                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.e(Constantes.TAG_ERROR_LOGOUT, "ERROR -> ", t);
                        }
                    });
                }
                //Logout
                break;
            case R.id.action_about:
                //about
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Consume el servicio para obtener la lista de clientes
     * @param idEmpleado id del empleado Logeado
     */
    private void consultarListaCliente(int idEmpleado){

        WebService api = ServiceRetrofit.getInstance().getSevices();
        api.getClientesEmpleado(new Empleado(idEmpleado)).enqueue(new Callback<ClientesJSONResult>() {
            @Override
            public void onResponse(Call<ClientesJSONResult> call, Response<ClientesJSONResult> response) {

                if(response.body().isOk()){
                    if(response.body() != null){
                        if(response.body().getResultado() != null){
                            adapter.submitList(jsonResponse(response.body()));
                            adapter.notifyDataSetChanged();
                            clientesBinding.swipeRfshList.setRefreshing(false);
                        } else {
                            Toast.makeText(getApplication(), "Sin Cliente Asignados", Toast.LENGTH_SHORT).show();
                            clientesBinding.swipeRfshList.setRefreshing(false);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ClientesJSONResult> call, Throwable t) {
                Snackbar.make(clientesBinding.getRoot(), "Ha sucedido un error", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Procesa la respuesta del servidor, esto para respuesta de consulta de clientes por visitar
     * @param res respuesta del servicio {@link ClientesJSONResult}
     * @return Lista de clientes por visitar {@link List<Cliente_por_visitar>}
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


            //AGREGACION DE LAS COORDENADAS DE LOS CLIENTES A LA LISTA
            userInMap.add(cl);

            /*Como no recibe estos datos del servidor, se pone "sin .."
            para que no de errores el DIFF CALL*/
            cl.setCasa("sin casa");
            cl.setFirma("sin firma");
            clList.add(cl);
        }

        return clList;
    }


    private boolean validarPermisos(Context ctx) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if((ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            return true;
        } else {

            requestPermissions(PERMISOS, 100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)){
                    //Se muestra mensaje al usuario de por que deberia aceptar el permiso
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Acceder a la ubicacion del telefono XD");
                    builder.setMessage("Debes aceptar este permiso para poder usar Trovami weon qliao");
                    builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Array de string con los permisos que pediremos al usuario
                            final String[] permissions = new String[]{ACCESS_FINE_LOCATION};

                            //Muestra un dialogo para pedir los permisos al usuario
                            requestPermissions(permissions, 100);
                        }
                    });
                    builder.show();
                }
            }
        }
    }

}