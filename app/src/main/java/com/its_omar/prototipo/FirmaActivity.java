package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.material.button.MaterialButton;
import com.its_omar.prototipo.controller.MyViewController;
import com.its_omar.prototipo.databinding.ActivityFirmaBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FirmaActivity extends AppCompatActivity {

    private ActivityFirmaBinding binding;

    private static String fileName;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myFirmas");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirmaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnReiniciarFirma.setOnClickListener(v -> binding.signatureView.clearCanvas());

        binding.btnAceptarFirma.setOnClickListener(v -> {
            if(!binding.signatureView.isBitmapEmpty()){
                Bitmap firma = binding.signatureView.getSignatureBitmap();

                String f = bitmapToBase64(firma);
                System.out.println("golo");
            }
        });



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}