package com.its_omar.prototipo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.its_omar.prototipo.fragments.VerificarEstatusFragment;

import static com.its_omar.prototipo.model.Constantes.ID_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.NOMBRE_CLIENTE_EXTRA_KEY;

public class VerificacionVisitaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_visita);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        String nom = extras.getString(NOMBRE_CLIENTE_EXTRA_KEY);
        int id = extras.getInt(ID_CLIENTE);
        try {

            VerificarEstatusFragment fragment = VerificarEstatusFragment.newInstance(nom, id);
            getSupportFragmentManager().beginTransaction().add(R.id.container_verificacion , fragment).commit();
        } catch (Exception e){
            Log.d("frag", "error", e);
        }


    }
}