package com.its_omar.prototipo.controller;

import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.its_omar.prototipo.R;

public class Commons {

    /**
     * Muestra alerta de error
     * @param titleIDResource IDResource Titulo de la alerta
     * @param messageIDResource IDResource Mensaje de la alerta
     * @param context Contexto de ejecucion
     */
    public static void showAlertError(int titleIDResource, int messageIDResource, Context context) {
        new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_Dialog)
                .setTitle(titleIDResource)
                .setIcon(R.drawable.ic_error)
                .setMessage(messageIDResource)
                .show();
    }

    /**
     * Muestra alerta de error
     * @param title Titulo de la alerta
     * @param message Mensaje de la alerta
     * @param context Contexto de ejecucion
     */
    public static void showAlertError(String title, String message, Context context) {
        new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_Dialog)
                .setTitle(title)
                .setIcon(R.drawable.ic_error)
                .setMessage(message)
                .show();
    }

    /**
     * Muestra alerta de error con boton positivo,
     * @param titleIDResource titleIDResource IDResource Titulo de la alerta
     * @param messageIDResource messageIDResource IDResource Mensaje de la alerta
     * @param textPositiveBtnID textPositiveBtn texto del boton positivo
     * @param context Contexto de ejecucion
     * @param onClickPositiver Evento onClicl del positive botton.
     */
    public static void showAlertError(int titleIDResource, int messageIDResource, int textPositiveBtnID, Context context, DialogInterface.OnClickListener onClickPositiver){
        new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_Dialog)
                .setTitle(titleIDResource)
                .setIcon(R.drawable.ic_error)
                .setMessage(messageIDResource)
                .setPositiveButton(textPositiveBtnID, onClickPositiver)
                .show();
    }


}
