package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.its_omar.prototipo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(loginBinding.getRoot());

        asignarTipografia(face);

        loginBinding.btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClientesActivity.class);
            startActivity(intent);
        });
    }

    private void asignarTipografia(Typeface face){
        loginBinding.tvTitle.setTypeface(face);
    }
}