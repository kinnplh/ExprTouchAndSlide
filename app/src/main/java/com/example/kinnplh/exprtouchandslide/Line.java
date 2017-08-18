package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by kinnplh on 2016/12/21.
 */

public class Line extends Shape {
    private float x1, y1, x2, y2;

    Line(float _x1, float _y1, float _x2, float _y2){
        super((_x1 + _x2) / 2, (_y1 + _y2) / 2);
        x1 = _x1;
        x2 = _x2;
        y1 = _y1;
        y2 = _y2;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float before = paint.getStrokeWidth();
        paint.setStrokeWidth(20);
        canvas.drawLine(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(before);
    }

    @Override
    public boolean inside(float x, float y) {
        // in fact, a line have No width
        return false;
    }
}
