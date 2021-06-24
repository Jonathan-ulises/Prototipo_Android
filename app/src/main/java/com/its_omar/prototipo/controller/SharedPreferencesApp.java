package com.its_omar.prototipo.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Usuario;

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
    public void saveSharePreferencesLogin(String nombreusu, int id_empleado){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constantes.SESION_ESTATUS, true);
        editor.putString(Constantes.NOMBRE_USUARIO_LOGEADO, nombreusu);
        editor.putInt(Constantes.ID_EMPLEADO_LOGEADO, id_empleado);
        editor.apply();
    }


    /**
     * Obtiene el estatus del logeo del usuario
     * @return estatus de la sesion
     */
    public boolean getSesionSharedPreference(){
        return sharedPreferences.getBoolean(Constantes.SESION_ESTATUS, false);
    }

    public Usuario getUsuarioLogeado() {
        Usuario u = new Usuario();
        u.setNombreUsuario(sharedPreferences.getString(Constantes.NOMBRE_USUARIO_LOGEADO, null));
        u.setId_empleado(sharedPreferences.getInt(Constantes.ID_EMPLEADO_LOGEADO, 0));

        return u;
    }
}
