package com.its_omar.prototipo.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.its_omar.prototipo.databinding.FragmentFirmaUsuBinding;


public class FirmaUsuFragment extends Fragment {

    private FragmentFirmaUsuBinding firmaBinding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firmaBinding = FragmentFirmaUsuBinding.inflate(inflater, container, false);

        firmaBinding.srView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {


                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setColor(Color.CYAN);

/*
                canvas.drawRect(10, 70, ancho-10, 20, paint);*/

                Canvas canvas = surfaceHolder.lockCanvas();
                int ancho = canvas.getWidth();
                canvas.drawRect(10, 70, ancho-10, 20, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);



            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });

        // Inflate the layout for this fragment
        return firmaBinding.getRoot();
    }

/*
    class Vista extends View{

        public Vista(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.CYAN);

            int ancho = canvas.getWidth();

            canvas.drawRect(10, 70, ancho-10, 20, paint);
        }
    }*/


}