package com.its_omar.prototipo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityClientesBinding;
import com.its_omar.prototipo.model.Bitacora;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding clientesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientesBinding = ActivityClientesBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(clientesBinding.getRoot());


        setSupportActionBar(clientesBinding.toolbar);

        asignarTipografia(face);

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
                //Logout
                break;
            case R.id.action_about:
                //about
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}