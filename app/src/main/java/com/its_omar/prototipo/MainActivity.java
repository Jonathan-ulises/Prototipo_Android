package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityMainBinding;
import com.its_omar.prototipo.model.Veri_Con;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        verificarConexionServidor(this);

    }

    /**
     * Verifica si la conexion con el servidor es buena
     */
    private void verificarConexionServidor(Context ctx){
        WebService geoSerice = ServiceRetrofit.getInstance().getSevices();

        geoSerice.verificarConexion().enqueue(new Callback<Veri_Con>() {
            @Override
            public void onResponse(Call<Veri_Con> call, Response<Veri_Con> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Error de respuesta", Toast.LENGTH_SHORT).show();
                } else {
                    if(response.body().isOk()){
                        SharedPreferencesApp sharedPreferencesApp = SharedPreferencesApp.getInstance(ctx);

                        if(sharedPreferencesApp.getSesionSharedPreference()){
                            Intent intent = new Intent(getApplication(), ClientesActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Intent intent = new Intent(getApplication(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Veri_Con> call, Throwable t) {
                Toast.makeText(getApplication(), "Error -> " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}