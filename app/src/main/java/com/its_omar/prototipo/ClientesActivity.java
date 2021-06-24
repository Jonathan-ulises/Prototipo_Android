package com.its_omar.prototipo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.ConsultasComunes;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityClientesBinding;
import com.its_omar.prototipo.fragments.adapters.ClientesVisitaAdapter;
import com.its_omar.prototipo.model.Bitacora;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding clientesBinding;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientesBinding = ActivityClientesBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(clientesBinding.getRoot());

        this.ctx = this;

        setSupportActionBar(clientesBinding.toolbar);

        asignarTipografia(face);

        //RecycleView
        ClientesVisitaAdapter adapter = new ClientesVisitaAdapter();
        clientesBinding.rclClienteVisita.setLayoutManager(new LinearLayoutManager(this));


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
        SharedPreferencesApp sharedPreferencesApp = SharedPreferencesApp.getInstance(this);
        Usuario usu;
        switch (item.getItemId()) {
            case R.id.action_logout:
                usu = sharedPreferencesApp.getUsuarioLogeado();

                Toast.makeText(this, usu.getNombreUsuario() + " : " + usu.getId_empleado(), Toast.LENGTH_LONG).show();

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

    private void consultarListaCliente(){


    }


}