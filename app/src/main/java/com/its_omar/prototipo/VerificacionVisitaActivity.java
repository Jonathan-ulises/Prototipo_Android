package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import com.its_omar.prototipo.fragments.VerificarEstatusFragment;

public class VerificacionVisitaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_visita);

        try {
            VerificarEstatusFragment fragment = new VerificarEstatusFragment();
            fragment.setArguments(null);
            getSupportFragmentManager().beginTransaction().add(R.id.container_verificacion , fragment).commit();
        } catch (Exception e){
            Log.d("frag", "error", e);
        }


    }
}