package com.its_omar.prototipo.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.its_omar.prototipo.model.Constantes;

public class SharedPreferencesApp {

    private static SharedPreferencesApp instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferencesApp(Context ctx){
        sharedPreferences = ctx.getSharedPreferences(Constantes.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesApp getInstance(Context context){
        if(SharedPreferencesApp.instance == null){
            SharedPreferencesApp.instance = new SharedPreferencesApp(context);
        }

        return instance;
    }


    /**
     * Guarda el estatus de la sesion
     */
    public void saveSharePreferencesLogin(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constantes.SESION_ESTATUS, true);
        editor.apply();
    }


    /**
     * Obtiene el estatus del logeo del usuario
     * @return estatus de la sesion
     */
    public boolean getSesionSharedPreference(){
        return sharedPreferences.getBoolean(Constantes.SESION_ESTATUS, false);
    }
}
