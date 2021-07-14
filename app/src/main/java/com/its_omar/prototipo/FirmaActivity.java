package com.its_omar.prototipo;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityFirmaBinding;
import com.its_omar.prototipo.fragments.VerificacionDatosFragment;
import com.its_omar.prototipo.model.Constantes;

import java.io.File;

import static com.its_omar.prototipo.model.Constantes.FIRMA_CLIENTE_KEY;

public class FirmaActivity extends AppCompatActivity {

    private ActivityFirmaBinding binding;

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
                    editor.putString(FIRMA_CLIENTE_KEY, f);
                    editor.apply();
                }

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