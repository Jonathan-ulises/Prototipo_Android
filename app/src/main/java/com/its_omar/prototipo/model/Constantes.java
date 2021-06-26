package com.its_omar.prototipo.model;

import android.graphics.Bitmap;
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
    public static final String TAG_CAPTURAR_UBICACION = "evento_cap";
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
}
