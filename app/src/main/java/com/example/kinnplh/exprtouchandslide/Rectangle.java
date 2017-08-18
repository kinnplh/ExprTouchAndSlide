package com.example.kinnplh.exprtouchandslide;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import static java.lang.Math.abs;

/**
 * Created by kinnplh on 2016/12/21.
 */

class Rectangle extends Shape{

    float width;
    float height;
    float rotateAngel;
    Rectangle(float x, float y, float w, float h) {
        super(x, y);
        width = w;
        height = h;
        rotateAngel = 0;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.rotate(- rotateAngel, centerX, centerY);
        canvas.drawRect(centerX - width / 2, centerY - height / 2,
                centerX + width / 2, centerY + height / 2, paint);
        canvas.rotate(rotateAngel, centerX, centerY);
    }

    @Override
    public boolean inside(float x, float y) {
        return width > 2 * abs(x - centerX) && height > 2 * abs(y - centerY);
    }

    @Override
    public void zoom(double ratio){
        width *= ratio;
        height *= ratio;
    }

    public void rotate(float _a){
        rotateAngel += _a;
    }
}