package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.its_omar.prototipo.controller.MyViewController;

public class FirmaActivity extends AppCompatActivity {
    MaterialButton btn;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

        btn = findViewById(R.id.btnRehacer);
        v = findViewById(R.id.srView);

        btn.setOnClickListener(view -> {
            MyViewController mv = new MyViewController(this, null);
            mv.clearAnimation();
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}