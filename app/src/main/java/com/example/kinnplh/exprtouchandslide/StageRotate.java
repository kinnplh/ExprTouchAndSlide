package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by kinnplh on 2016/12/22.
 */

public class StageRotate extends MainOperationView {

    final static int repeatTimes = 5;
    final static int halfWidth = 600;

    private int countTimes;
    private boolean inRotateProcess;
    Shape rotateObj;
    private float initAngle;
    StageRotate(Context context, String info){
        super(context);
        inRotateProcess = false;
        initAngle = 0;
        InfoWhenStart = info;
        next();
    }

    @Override
    boolean next() {
        if(crtStageFinished)
            return false;
        if(countTimes >= repeatTimes){
            crtStageFinished = true;
            return false;
        }
        rotateObj = new Rectangle(screenWidth / 2, screenHeight / 2, halfWidth, halfWidth);
        countTimes += 1;
        return false;
    }

    @Override
    boolean eventHandler(MotionEvent e) {
        if(e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN || e.getActionMasked() == MotionEvent.ACTION_DOWN){
            if(e.getPointerCount() != 2) {
                inRotateProcess = false;
                return false;
            }
            if(rotateObj.inside(e.getX(0), e.getY(0)) && rotateObj.inside(e.getX(1), e.getY(1))) {
                inRotateProcess = true;
                float xOffset = e.getX(0) - e.getX(1);
                float yOffset = e.getY(0) - e.getY(1);
                initAngle = (float) Math.toDegrees(Math.atan(xOffset / yOffset));
            }
            else
                inRotateProcess = false;
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if(inRotateProcess){
                if(e.getPointerCount() != 2)
                    return false;
                float xOffset = e.getX(0) - e.getX(1);
                float yOffset = e.getY(0) - e.getY(1);
                float crtAngle = (float) Math.toDegrees(Math.atan(xOffset / yOffset));
                ((Rectangle)rotateObj).rotate(crtAngle - initAngle);
                initAngle = crtAngle;
                invalidate();
            }
            else
                inRotateProcess = false;
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_UP){
            if(inRotateProcess){
                next();
                invalidate();
            }
            inRotateProcess = false;
        }

        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        rotateObj.draw(canvas, p);
    }
}
