package com.its_omar.prototipo.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyViewController extends View {
    public MyViewController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        @SuppressLint("DrawAllocation") Paint p = new Paint();
        p.setColor(Color.MAGENTA);
        p.setStrokeWidth(10);
        canvas.drawLine(20, 0, 20, getHeight(), p);

    }
}
