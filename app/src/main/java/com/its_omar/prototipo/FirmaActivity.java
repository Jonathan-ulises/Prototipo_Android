package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.material.button.MaterialButton;
import com.its_omar.prototipo.controller.MyViewController;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityFirmaBinding;
import com.its_omar.prototipo.fragments.VerificacionDatosFragment;
import com.its_omar.prototipo.model.Constantes;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FirmaActivity extends AppCompatActivity {

    private ActivityFirmaBinding binding;
    //private SharedPreferences.Editor editor;

    private static String fileName;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myFirmas");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirmaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initEditorVerificacion();

        binding.btnReiniciarFirma.setOnClickListener(v -> binding.signatureView.clearCanvas());

        binding.btnAceptarFirma.setOnClickListener(v -> {
            if(!binding.signatureView.isBitmapEmpty()){

                SharedPreferences.Editor editor = initEditorVerificacion();

                Bitmap firma = binding.signatureView.getSignatureBitmap();

                String f = Constantes.bitmapToBase64(firma);

                if(editor != null){
                    editor.putString(Constantes.FIRMA_CLIENTE_KEY, f);
                    editor.apply();
                }

                System.out.println("golo");

                finish();
            }
        });



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    /**
     * Inicializa el Editor del SharedPreferences para escribir datos
     */
    private SharedPreferences.Editor initEditorVerificacion(){
        VerificacionDatosFragment fr = new VerificacionDatosFragment();

        SharedPreferencesApp sp = SharedPreferencesApp.getInstance(this);
        return sp.getEditorForDatosVerificacion();
    }
}