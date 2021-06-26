package com.its_omar.prototipo.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Usuario;

public class SharedPreferencesApp {

    private static SharedPreferencesApp instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesVerificacion;
    private Cliente_por_visitar cl;

    private SharedPreferencesApp(Context ctx){
        //referencias del login
        sharedPreferences = ctx.getSharedPreferences(Constantes.PREFERENCES_LOGIN, Context.MODE_PRIVATE);

        //Referencias de los datos verificados
        sharedPreferencesVerificacion = ctx.getSharedPreferences(Constantes.PREFERENCES_USUARIO_DATOS_VERIFICACION, Context.MODE_PRIVATE);
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
     * Obtiene el Editor del SharePreferences para la escribir datos de la verificacion de los datos
     * del cliente
     * @return Editor {@link SharedPreferences.Editor}
     */
    public SharedPreferences.Editor getEditorForDatosVerificacion(){
        return sharedPreferencesVerificacion.edit();
    }

    /**
     * Obtiene el estatus del logeo del usuario
     * @return estatus de la sesion
     */
    public boolean getSesionSharedPreference(){
        return sharedPreferences.getBoolean(Constantes.SESION_ESTATUS, false);
    }

    /**
     * Obtiene el usuario que inicio la sesion
     * @return Usuario logeado {@link Usuario}
     */
    public Usuario getUsuarioLogeado() {
        Usuario u = new Usuario();
        u.setNombreUsuario(sharedPreferences.getString(Constantes.NOMBRE_USUARIO_LOGEADO, null));
        u.setId_empleado(sharedPreferences.getInt(Constantes.ID_EMPLEADO_LOGEADO, 0));

        return u;
    }

    /**
     * Obtiene un cliente con los datos que han sido llenados.
     * @return Cliente de la visita {@link Cliente_por_visitar}
     */
    public Cliente_por_visitar getDatosClieteEnVerificacion(){
        cl = new Cliente_por_visitar();
        cl.setCasa(sharedPreferencesVerificacion.getString(Constantes.FOTO_CASA_KEY, "not"));
        cl.setFirma(sharedPreferencesVerificacion.getString(Constantes.FIRMA_CLIENTE_KEY, "not"));

        if(cl.getCasa().equals("not") || cl.getCasa().isEmpty()){
            cl = null;
            Log.i(Constantes.TAG_INFO_DATOS_VERIFICACION, "casa_nullo");
        } else if(cl.getFirma().equals("not") || cl.getFirma().isEmpty()){
            cl = null;
            Log.i(Constantes.TAG_INFO_DATOS_VERIFICACION, "firma_nullo");
        }

        return cl;
    }



    public void borrarPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constantes.PREFERENCES_LOGIN);
        editor.clear();
        editor.apply();
    }
}
