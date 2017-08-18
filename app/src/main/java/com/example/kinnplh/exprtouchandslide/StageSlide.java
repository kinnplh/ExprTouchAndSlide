package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by kinnplh on 2016/12/21.
 */

public class StageSlide extends MainOperationView {

    Shape crtShape;
    Shape assistLine;
    static final int totalPosNum = 8;
    static final int repeatTimes = 1;
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;
    static final int DOWN = 3;
    static final int MIDDLE_UP = 4;
    static final int MIDDLE_DOWN = 5;
    static final int MIDDLE_LEFT = 6;
    static final int MIDDLE_RIGHT = 7;
    static final int halfWidth = 200;

    private int [] countDoneTime;
    private int crtPos;
    private float lastTouchX;
    private float lastTouchY;


    public StageSlide(Context context,@Nullable String info){
        super(context);
        countDoneTime = new int[totalPosNum];
        for(int i = 0; i < totalPosNum; ++ i)
            countDoneTime[i] = 0;
        lastTouchX = lastTouchY = -1;
        if(info == null)
            InfoWhenStart = "在接下来的实验中，你需要将方块，沿着粗线的方向快速地滑动，不必准确控制滑动的最终位置";
        else
            InfoWhenStart = info;
        next();
    }

    public void onDraw(Canvas canvas){
        if(crtStageFinished)
            return;
        crtShape.draw(canvas, p);
        assistLine.draw(canvas, p);
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
        crtShape = getCrtShapeByPos(crtPos);
        assistLine = getAssistLineByPos(crtPos);
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
                next();
                invalidate();
                lastTouchY = lastTouchX = -1;
            }
        }
        return false;
    }

    Shape getAssistLineByPos(int p){
        switch (p){
            case LEFT:
                return new Line(screenWidth / 4, screenHeight / 2, screenWidth / 2, screenHeight / 2);
            case RIGHT:
                return new Line(screenWidth / 2, screenHeight / 2, screenWidth / 4 * 3, screenHeight / 2);
            case UP:
                return new Line(screenWidth / 2, screenHeight / 2, screenWidth / 2, screenHeight / 4);
            case DOWN:
                return new Line(screenWidth / 2, screenHeight / 2, screenWidth / 2, screenHeight / 4 * 3);
            case MIDDLE_DOWN:
                return new Line(screenWidth / 2, screenHeight / 4 * 3, screenWidth / 2, screenHeight);
            case MIDDLE_LEFT:
                return new Line(screenWidth / 4, screenHeight / 2, 0, screenHeight / 2);
            case MIDDLE_RIGHT:
                return new Line(screenWidth / 4 * 3, screenHeight / 2, screenWidth, screenHeight / 2);
            case MIDDLE_UP:
                return new Line(screenWidth / 2, screenHeight / 4, screenWidth / 2, 0);
        }
        return null;
    }

    Shape getCrtShapeByPos(int p){
        switch (p){
            case LEFT:
                return new Rectangle(0, screenHeight / 2, halfWidth, halfWidth);
            case RIGHT:
                return new Rectangle(screenWidth, screenHeight / 2, halfWidth, halfWidth);
            case UP:
                return new Rectangle(screenWidth / 2, 0, halfWidth, halfWidth);
            case DOWN:
                return new Rectangle(screenWidth / 2, screenHeight, halfWidth, halfWidth);
            case MIDDLE_DOWN:
            case MIDDLE_LEFT:
            case MIDDLE_RIGHT:
            case MIDDLE_UP:
                return new Rectangle(screenWidth / 2, screenHeight / 2, halfWidth, halfWidth);
        }

        return null;
    }

    private int getToDoPosNum(){
        int res = 0;
        for(int i = 0; i < totalPosNum; ++ i)
            if(countDoneTime[i] < repeatTimes)
                res += 1;
        return res;
    }

}
