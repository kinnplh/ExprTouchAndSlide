package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by kinnplh on 2016/12/21.
 */

public class StageDrag extends MainOperationView {
    Shape crtShape;
    Shape targetPos;
    static final int totalPosNum = 4;
    static final int repeatTimes = 1;
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;
    static final int DOWN = 3;
    static final float RADIUS = 75;
    static final int targetDis = 200;
    static final int halfWidth = 200;

    private int [] countDoneTime;
    private int crtPos;
    private float lastTouchX;
    private float lastTouchY;


    public StageDrag(Context context,@Nullable String info){
        super(context);
        crtShape = new Rectangle(screenWidth / 2, screenHeight / 2, halfWidth, halfWidth);
        countDoneTime = new int[totalPosNum];
        for(int i = 0; i < totalPosNum; ++ i)
            countDoneTime[i] = 0;
        next();
        lastTouchX = lastTouchY = -1;
        if(info == null)
            InfoWhenStart = "在接下来的实验中，你需要将屏幕中间的正方形沿直线拖动到圆圈所在的位置（以手指进入圆圈为准）。共需拖拽20次";
        else
            InfoWhenStart = info;
    }

    public void onDraw(Canvas canvas){
        if(crtStageFinished)
            return;
        crtShape.draw(canvas, p);
        targetPos.draw(canvas, p);
    }


    @Override
    boolean next() {
        if(crtStageFinished)
            return false;
        int remainPos = getToDoPosNum();
        if(remainPos == 0){
            crtStageFinished = true;
            return false;
        }

        Random random = new Random();
        int index = random.nextInt(remainPos) + 1;
        for(crtPos = 0; crtPos < totalPosNum; ++ crtPos) {
            if (countDoneTime[crtPos] < repeatTimes)
                index --;
            if(index == 0)
                break;
        }

        countDoneTime[crtPos] += 1;
        crtShape = new Rectangle(screenWidth / 2, screenHeight / 2, halfWidth, halfWidth);
        targetPos = getTargetShapeByPos(crtPos);
        return true;

    }

    @Override
    boolean eventHandler(MotionEvent e) {
        // only support single touch point!
        if(e.getActionMasked() == MotionEvent.ACTION_DOWN || e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
            if(crtShape.inside(e.getX(), e.getY())){
                lastTouchX = e.getX();
                lastTouchY = e.getY();
            }
            else {
                lastTouchX = lastTouchY = -1;
            }
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if(lastTouchX >= 0 && lastTouchY >= 0){
                crtShape.move(e.getX() - lastTouchX, e.getY() - lastTouchY);
                lastTouchX = e.getX();
                lastTouchY = e.getY();
                invalidate();
            }
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_POINTER_UP || e.getActionMasked() == MotionEvent.ACTION_UP){
            if(lastTouchY >= 0 && lastTouchX >= 0){
                if(targetPos.inside(e.getX(), e.getY())) {
                    next();
                    invalidate();
                }
                lastTouchY = lastTouchX = -1;
            }
        }
        return false;
    }

    Shape getTargetShapeByPos(int p){
        Shape res = null;
        switch (p){
            case LEFT:
                return new Circle(screenWidth / 2 - targetDis, screenHeight / 2, RADIUS);
            case RIGHT:
                return new Circle(screenWidth / 2 + targetDis, screenHeight / 2, RADIUS);
            case UP:
                return new Circle(screenWidth / 2, screenHeight / 2 - targetDis, RADIUS);
            case DOWN:
                return new Circle(screenWidth / 2, screenHeight / 2 + targetDis, RADIUS);
        }

        return res;
    }

    private int getToDoPosNum(){
        int res = 0;
        for(int i = 0; i < totalPosNum; ++ i)
            if(countDoneTime[i] < repeatTimes)
                res += 1;
        return res;
    }


}
