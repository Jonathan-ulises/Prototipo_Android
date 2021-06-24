package com.its_omar.prototipo.controller;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.model.Bitacora;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultasComunes {

    public static void registrarAccionBitacora(String modulo, String accion, int id_empleado) {
        WebService api = ServiceRetrofit.getInstance().getSevices();

        Bitacora registroBit = new Bitacora(modulo, accion, id_empleado);

        api.registrarBitacora(registroBit).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body().isOk()){
                    Log.i(Constantes.TAG_BITACORA_LOG, response.body().getMensaje());
                } else {
                    Log.i(Constantes.TAG_BITACORA_LOG, response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i(Constantes.TAG_BITACORA_LOG, t.getMessage());
            }
        });
    }


}
