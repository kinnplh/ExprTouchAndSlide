package com.example.kinnplh.exprtouchandslide;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by kinnplh on 2016/12/21.
 */

abstract public class Shape {
    public float centerX;
    public float centerY;

    public Shape(float _x, float _y){
        centerX = _x;
        centerY = _y;
    }

    abstract public void draw(Canvas canvas, Paint paint);
    abstract public boolean inside(float x, float y);
    public void zoom(double ratio){}


    public void move(float xOffset, float yOffset){
        centerX += xOffset;
        centerY += yOffset;
    }

}
