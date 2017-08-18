package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.example.kinnplh.exprtouchandslide.ControlLayout.SDCardPath;


/**
 * Created by kinnplh on 2016/12/21.
 */

abstract public class MainOperationView extends View {

    public int screenWidth;
    public int screenHeight;
    private float densityDpi;
    public boolean crtStageFinished;
    public String InfoWhenStart;
    Paint p;
    BufferedWriter out;
    public MainOperationView(Context context){
        super(context);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        densityDpi = dm.densityDpi;
        crtStageFinished = false;
        p = new Paint();
        InfoWhenStart = "诶呀呀~ 我怎么会在这里！";

        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SDCardPath, true)));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FloatPoint getGridPoint(int x, int y, int totalX, int totalY){

        float xOffset = screenWidth * (2f * x - 1f) / (2f * totalX);
        float yOffset = screenHeight * (2f * y - 1f) / (2f * totalY);
        return new FloatPoint(xOffset, yOffset);
    }

    abstract boolean next();
    abstract boolean eventHandler(MotionEvent e);
    abstract public void onDraw(Canvas canvas);
    public boolean hasNext(){
        return crtStageFinished;
    }

    public void printToSDCardWithTimestamp(String info){
        try {
            out.write(String.format("%d: %s", System.currentTimeMillis(), info));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FloatPoint{
    float x;
    float y;
    FloatPoint(){}
    FloatPoint(float _x, float _y){
        x = _x;
        y = _y;
    }
}