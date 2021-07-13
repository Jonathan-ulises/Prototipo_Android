package com.its_omar.prototipo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.Commons;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityMainBinding;
import com.its_omar.prototipo.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.its_omar.prototipo.controller.Commons.showAlertError;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        verificarConexionServidor(this);

        

    }

    /**
     * Verifica si la conexion con el servidor es buena
     */
    protected void verificarConexionServidor(Context ctx){
        WebService geoSerice = ServiceRetrofit.getInstance().getSevices();

        geoSerice.verificarConexion().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

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
            public void onFailure(Call<Result> call, Throwable t) {
                //showAlertError(R.string.alert_verificacion_conx_title_error,R.string.alert_verificacion_conx_mesage_error, context );
                Commons.showAlertError(
                        R.string.alert_verificacion_conx_title_error,
                        R.string.alert_verificacion_conx_mesage_error,
                        R.string.alert_verificacion_positive_btn,
                        context,
                        (dialogInterface, i) -> {
                            verificarConexionServidor(context);
                        });
            }
        });
    }


}