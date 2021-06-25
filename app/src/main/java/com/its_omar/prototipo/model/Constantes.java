package com.its_omar.prototipo.model;

import androidx.annotation.NonNull;

public class Constantes {

    public static final int REQUEST_CODE_CAMERA_INTENT = 1000;

    public static final String PREFERENCES_NAME = "preferences_login";
    public static final String SESION_ESTATUS = "login_ok";
    public static final String NOMBRE_USUARIO_LOGEADO = "usu_logeado";
    public static final String ID_EMPLEADO_LOGEADO = "id_empleado_logeado";
    public static final String NOMBRE_CLIENTE_EXTRA_KEY = "cliente_por_visitar";

    //TAGS para Log
    public static final String TAG_BITACORA_LOG = "reg_bita";
    public static final String TAG_ERROR_LOGOUT = "login_out";
    public static final String TAG_ERROR_ARGUMENTS_FRAGMENT = "arg_fragment";
    public static final String TAG_ERROR_INTENT_EXTRAS = "intent_extras";
    public static final String TAG_ERROR_FOTO_NULL = "foto_capturada";
    /**
     * Genera el nombre completo del cliente
     * @param cl Objeto del cliente {@link Cliente_por_visitar}
     * @return Nombre completo {@link String}
     */
    public static String generarNombreCompleto(@NonNull Cliente_por_visitar cl){
        return cl.getNombre() + " " + cl.getaPaterno() + " " + cl.getaMaterno();
    }
}
