package com.its_omar.prototipo.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Usuario;
import com.its_omar.prototipo.model.bodyJSONCliente.BodyJSONCliente;

import java.util.HashMap;
import java.util.Map;

import static com.its_omar.prototipo.model.Constantes.DATO_COMPLETO;
import static com.its_omar.prototipo.model.Constantes.DATO_IMCOMPLETO;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_VERIFICACION;
import static com.its_omar.prototipo.model.Constantes.FIRMA_CLIENTE_KEY;
import static com.its_omar.prototipo.model.Constantes.FLAG_FIRMA_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.FLAG_FOTO_CASA;
import static com.its_omar.prototipo.model.Constantes.FLAG_LAT_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.FLAG_LONG_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.FLAG_VALIDACION;
import static com.its_omar.prototipo.model.Constantes.FOTO_CASA_KEY;
import static com.its_omar.prototipo.model.Constantes.ID_EMPLEADO_LOGEADO;
import static com.its_omar.prototipo.model.Constantes.LATITUDE_UBI_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.LONGITUD_UBI_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.NOMBRE_USUARIO_LOGEADO;
import static com.its_omar.prototipo.model.Constantes.NO_REALIZADO;
import static com.its_omar.prototipo.model.Constantes.NO_VALIDADO;
import static com.its_omar.prototipo.model.Constantes.PREFERENCES_LOGIN;
import static com.its_omar.prototipo.model.Constantes.PREFERENCES_USUARIO_DATOS_VERIFICACION;
import static com.its_omar.prototipo.model.Constantes.SESION_ESTATUS;
import static com.its_omar.prototipo.model.Constantes.TAG_INFO_DATOS_VERIFICACION;
import static com.its_omar.prototipo.model.Constantes.VALIDADO;

public class SharedPreferencesApp {

    private static SharedPreferencesApp instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesVerificacion;
    private Cliente_por_visitar cl;

    private SharedPreferencesApp(Context ctx){
        //referencias del login
        sharedPreferences = ctx.getSharedPreferences(PREFERENCES_LOGIN, Context.MODE_PRIVATE);

        //Referencias de los datos verificados
        sharedPreferencesVerificacion = ctx.getSharedPreferences(PREFERENCES_USUARIO_DATOS_VERIFICACION, Context.MODE_PRIVATE);
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
        editor.putBoolean(SESION_ESTATUS, true);
        editor.putString(NOMBRE_USUARIO_LOGEADO, nombreusu);
        editor.putInt(ID_EMPLEADO_LOGEADO, id_empleado);
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
        return sharedPreferences.getBoolean(SESION_ESTATUS, false);
    }

    /**
     * Obtiene el usuario que inicio la sesion
     * @return Usuario logeado {@link Usuario}
     */
    public Usuario getUsuarioLogeado() {
        Usuario u = new Usuario();
        u.setNombreUsuario(sharedPreferences.getString(NOMBRE_USUARIO_LOGEADO, null));
        u.setId_empleado(sharedPreferences.getInt(ID_EMPLEADO_LOGEADO, 0));

        return u;
    }

    /**
     * Obtiene un cliente con los datos que han sido llenados.
     * @return Cliente de la visita {@link Cliente_por_visitar}
     */
    public Cliente_por_visitar getDatosClieteEnVerificacion(){
        cl = new Cliente_por_visitar();
        cl.setCasa(sharedPreferencesVerificacion.getString(FOTO_CASA_KEY, "not"));
        cl.setFirma(sharedPreferencesVerificacion.getString(FIRMA_CLIENTE_KEY, "not"));

        if(cl.getCasa().equals("not") || cl.getCasa().isEmpty()){
            cl = null;
            Log.i(TAG_INFO_DATOS_VERIFICACION, "casa_nullo");
        } else if(cl.getFirma().equals("not") || cl.getFirma().isEmpty()){
            cl = null;
            Log.i(TAG_INFO_DATOS_VERIFICACION, "firma_nullo");
        }

        return cl;
    }

    /**
     * Crear banderas por cada dato del formulario de la visita, validando si una accion
     * fue realizada. Si falta un dato, la bandera sera un 0, si esta completo el datos, sera 1
     * @return Banderas de los datos completados {@link Map<>}
     */
    public Map<String, Integer> getFlagValidacion(){
        Map<String, Integer> datosAgregados = new HashMap<String, Integer>();

        String foto_v = sharedPreferencesVerificacion.getString(FOTO_CASA_KEY, "not");
        String firma_v = sharedPreferencesVerificacion.getString(FIRMA_CLIENTE_KEY, "not");
        double lon = Double.parseDouble(sharedPreferencesVerificacion.getString(LONGITUD_UBI_CLIENTE, "0"));
        double lat = Double.parseDouble(sharedPreferencesVerificacion.getString(LATITUDE_UBI_CLIENTE, "0"));

        int isValidate = sharedPreferencesVerificacion.getInt(ESTATUS_VERIFICACION, NO_REALIZADO);

        //foto
        if (!foto_v.equals("not")){
            datosAgregados.put(FLAG_FOTO_CASA, DATO_COMPLETO);
        } else {
            datosAgregados.put(FLAG_FOTO_CASA, DATO_IMCOMPLETO);
        }

        //casa
        if (!firma_v.equals("not")){
            datosAgregados.put(FLAG_FIRMA_CLIENTE, DATO_COMPLETO);
        } else {
            datosAgregados.put(FLAG_FIRMA_CLIENTE, DATO_IMCOMPLETO);
        }

        //ubicacion
        //LONG
        if(lon > 0 || lon < 0){
            datosAgregados.put(FLAG_LONG_CLIENTE, DATO_COMPLETO);
        } else {
            datosAgregados.put(FLAG_LONG_CLIENTE, DATO_IMCOMPLETO);
        }

        //LAT
        if (lat > 0 || lat < 0) {
            datosAgregados.put(FLAG_LAT_CLIENTE, DATO_COMPLETO);
        } else {
            datosAgregados.put(FLAG_LAT_CLIENTE, DATO_IMCOMPLETO);
        }

        //Verificacion
        if (isValidate == NO_REALIZADO){
            datosAgregados.put(FLAG_VALIDACION, NO_REALIZADO);
        } else {
            if (isValidate == VALIDADO) {
                datosAgregados.put(FLAG_VALIDACION, VALIDADO);
            } else {
                datosAgregados.put(FLAG_VALIDACION, NO_VALIDADO);
            }

        }

        return datosAgregados;
    }

    public BodyJSONCliente generateBodyRequestVisita(int idCliente){
        String foto_v = sharedPreferencesVerificacion.getString(FOTO_CASA_KEY, "not");
        String firma_v = sharedPreferencesVerificacion.getString(FIRMA_CLIENTE_KEY, "not");
        double lon = Double.parseDouble(sharedPreferencesVerificacion.getString(LONGITUD_UBI_CLIENTE, "0"));
        double lat = Double.parseDouble(sharedPreferencesVerificacion.getString(LATITUDE_UBI_CLIENTE, "0"));
        int isValidate = sharedPreferencesVerificacion.getInt(ESTATUS_VERIFICACION, NO_REALIZADO);

        BodyJSONCliente body = new BodyJSONCliente();
        body.setIdCliente(idCliente);
        body.setFirma(firma_v);
        body.setFotoCasa(foto_v);
        body.setIdEstatusVisita(isValidate);
        body.setLongitudReal(lon);
        body.setLatitudReal(lat);
        body.setComentario("Todo esta correcto");

        return body;

    }

    /**
     * Bora las sharePreferences de login
     */
    public void borrarPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREFERENCES_LOGIN);
        editor.clear();
        editor.apply();
    }


    /**
     * Borra las preferencias de la verificacion de datos
     * @param editor Editor de las preferencias de verificacion de datos
     */
    public void borrarPreferencesDatos(SharedPreferences.Editor editor) {
        editor = sharedPreferencesVerificacion.edit();
        editor.remove(PREFERENCES_USUARIO_DATOS_VERIFICACION);
        editor.clear();
        editor.apply();
        int s =  sharedPreferencesVerificacion.getAll().size();
    }
}
