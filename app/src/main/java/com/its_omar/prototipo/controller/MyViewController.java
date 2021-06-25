package com.its_omar.prototipo.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewController extends View {

    float x = 0;
    float y = 0;
    String accion = "accion";
    Path path = new Path();

    public MyViewController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        @SuppressLint("DrawAllocation") Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.MAGENTA);
        p.setStrokeWidth(10);
        //canvas.drawLine(20, 0, 20, getHeight(), p);

        if(accion.equals("down")) {
            path.moveTo(x, y);
        } else if(accion.equals("move")){
            path.lineTo(x,y);
        }

        canvas.drawPath(path, p);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            accion = "down";
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            accion = "move";
        }

        invalidate();

        return true;
    }
}
