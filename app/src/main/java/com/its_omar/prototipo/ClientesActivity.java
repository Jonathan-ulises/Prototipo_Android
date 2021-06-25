package com.its_omar.prototipo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.ConsultasComunes;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityClientesBinding;
import com.its_omar.prototipo.fragments.adapters.ClientesVisitaAdapter;
import com.its_omar.prototipo.model.Bitacora;
import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Empleado;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.Usuario;
import com.its_omar.prototipo.model.resultClienteService.ClientesJSONResult;
import com.its_omar.prototipo.model.resultClienteService.Resultado;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding clientesBinding;
    private Context ctx;
    ClientesVisitaAdapter adapter;
    SharedPreferencesApp sharedPreferencesApp;
    Usuario usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientesBinding = ActivityClientesBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(clientesBinding.getRoot());

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
            Toast.makeText(this, cliente.getNombre(), Toast.LENGTH_LONG).show();
        });


        clientesBinding.swipeRfshList.setOnRefreshListener(() -> {
            consultarListaCliente(usu.getId_empleado());
            Toast.makeText(this, "Refrescado", Toast.LENGTH_SHORT).show();
        });

        clientesBinding.btnCargarMaoa.setOnClickListener(view -> {
            Intent intent = new Intent(this, VerificacionVisitaActivity.class);

            startActivity(intent);

        });
    }

    private void asignarTipografia(Typeface face){
        clientesBinding.tvTituloLista.setTypeface(face);
        clientesBinding.btnCargarMaoa.setTypeface(face);
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

                //Toast.makeText(this, usu.getNombreUsuario() + " : " + usu.getId_empleado(), Toast.LENGTH_LONG).show();

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
        api.getClientesEmplado(new Empleado(idEmpleado)).enqueue(new Callback<ClientesJSONResult>() {
            @Override
            public void onResponse(Call<ClientesJSONResult> call, Response<ClientesJSONResult> response) {

                if(response.body().isOk()){
                    if(response.body() != null){
                        adapter.submitList(jsonResponse(response.body()));
                        adapter.notifyDataSetChanged();
                        clientesBinding.swipeRfshList.setRefreshing(false);
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

    private List<Cliente_por_visitar> jsonResponse(ClientesJSONResult res){
        List<Cliente_por_visitar> clList = new ArrayList<>();

        for (Resultado r:res.getResultado()) {
            Cliente_por_visitar cl = new Cliente_por_visitar();
            cl.setIdCliente(r.getJson().getId_Cliente());
            cl.setNombre(r.getJson().getNombre());
            cl.setaPaterno(r.getJson().getaPaterno());
            cl.setaMaterno(r.getJson().getaMaterno());
            cl.setIne(r.getJson().getFotoINE());
            clList.add(cl);
        }

        return clList;
    }
}