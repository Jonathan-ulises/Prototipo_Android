package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.its_omar.prototipo.databinding.ActivityClientesBinding;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding clientesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientesBinding = ActivityClientesBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(clientesBinding.getRoot());

        asignarTipografia(face);

        clientesBinding.btnCargarMaoa.setOnClickListener(view -> {
            Intent intent = new Intent(this, VerificacionVisitaActivity.class);

            startActivity(intent);

        });
    }

    private void asignarTipografia(Typeface face){
        clientesBinding.tvTituloLista.setTypeface(face);
        clientesBinding.btnCargarMaoa.setTypeface(face);
    }
}