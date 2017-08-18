package com.example.kinnplh.exprtouchandslide;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by kinnplh on 2016/12/21.
 */

public class Circle extends Shape {
    private float radius;

    public Circle(float x, float y, float r){
        super(x, y);
        radius = r;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    @Override
    public boolean inside(float x, float y) {
        return radius * radius > Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
    }
}
