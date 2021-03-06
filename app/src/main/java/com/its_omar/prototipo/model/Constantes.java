package com.its_omar.prototipo.model;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class Constantes {

    public static final int REQUEST_CODE_CAMERA_INTENT = 1000;

    //Preferences name
    public static final String PREFERENCES_LOGIN = "preferences_login";
    public static final String PREFERENCES_USUARIO_DATOS_VERIFICACION = "datos_verificacion";

    //Keys editor
    public static final String SESION_ESTATUS = "login_ok";
    public static final String NOMBRE_USUARIO_LOGEADO = "usu_logeado";
    public static final String ID_EMPLEADO_LOGEADO = "id_empleado_logeado";
    public static final String NOMBRE_CLIENTE_EXTRA_KEY = "cliente_por_visitar";
    public static final String ID_CLIENTE = "clente_seleccionado";
    public static final String FOTO_CASA_KEY = "fotografia_casa";
    public static final String FIRMA_CLIENTE_KEY = "firma_cliente";
    public static final String LONGITUD_UBI_CLIENTE = "long_cliente";
    public static final String LATITUDE_UBI_CLIENTE = "lat_cliente";
    public static final String ESTATUS_VERIFICACION = "verificacion_credito";


    //TAGS para Log
    public static final String TAG_BITACORA_LOG = "reg_bita";
    public static final String TAG_ERROR_LOGOUT = "login_out";
    public static final String TAG_ERROR_ARGUMENTS_FRAGMENT = "arg_fragment";
    public static final String TAG_ERROR_INTENT_EXTRAS = "intent_extras";
    public static final String TAG_ERROR_FOTO_NULL = "foto_capturada";
    public static final String TAG_INFO_DATOS_VERIFICACION = "faltan_datos";


    //FLAGS para verificar los datos enviados.
    public static final String FLAG_FOTO_CASA = "foto_completo";
    public static final String FLAG_FIRMA_CLIENTE = "firma_completo";
    public static final String FLAG_LONG_CLIENTE = "uLongitud_completo";
    public static final String FLAG_LAT_CLIENTE = "uLongitud_completo";
    public static final int DATO_COMPLETO = 1;
    public static final int DATO_IMCOMPLETO = 0;
    public static final String FLAG_VALIDACION = "validacion_completo";
    public static final int VALIDADO = 1;
    public static final int NO_VALIDADO = 0;
    public static final int NO_REALIZADO = 2;


    public static final int ESTATUS_POR_VISITAR = 1;
    public static final int ESTATUS_VISITADO = 2;
    public static final int ESTATUS_NO_ENCONTRADO = 4;
    public static final int ESTATUS_VISITA_RECHAZADA = 5;
    public static final int ESTATUS_VISITA_ABANDONADA = 6;
    public static final int ESTATUS_VALIDADO = 7;
    public static final int ESTATUS_NO_VALIDADO = 8;

    public static final String INTENT_ID_EMPLEADO = "cliente_Coord";


    public static final String[] PERMISOS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /**
     * Genera el nombre completo del cliente
     * @param cl Objeto del cliente {@link Cliente_por_visitar}
     * @return Nombre completo {@link String}
     */
    public static String generarNombreCompleto(@NonNull Cliente_por_visitar cl){
        return cl.getNombre() + " " + cl.getaPaterno() + " " + cl.getaMaterno();
    }

    /**
     * Convierte Bitmap a formato Base64
     * @param bitmap Foto formato Bitmap {@link Bitmap}
     * @return Base64 {@link String}
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Revisa que se tiene el servicio de GPS encendido
     * @param context Contexto de ejecucion
     * @return Si esta conectado
     */
    public static boolean checkIfLocationOpened(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }
}
