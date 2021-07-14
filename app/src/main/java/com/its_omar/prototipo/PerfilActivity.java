package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityPerfilBinding;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private String usuario;
    private String usu;
    SharedPreferencesApp sharedPreferencesApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferencesApp = SharedPreferencesApp.getInstance(this);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        usu = extras.getString("usu");

        binding.tvMiPerfil.setText(usuario);

        binding.btnCerrarSesion.setOnClickListener(view -> {
            WebService api = ServiceRetrofit.getInstance().getSevices();
            api.logoutApp(usu).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.body().isOk()) {
                        sharedPreferencesApp.borrarPreferences();
                        finish();
                        finishAffinity();
                    } else {
                        Snackbar.make(getApplicationContext(),binding.getRoot(), "Ha sucedido un error", Snackbar.LENGTH_SHORT)
                                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.e(Constantes.TAG_ERROR_LOGOUT, "ERROR -> ", t);
                }
            });
        });
    }
}