package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by kinnplh on 2016/12/22.
 */

public class StageMultiTouchSlide extends MainOperationView {

    Shape crtShape;
    Shape assistLine;
    static final int totalPosNum = 4;
    static final int repeatTimes = 1;
    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;
    static final int DOWN = 3;
    static final int halfWidth = 100;

    private int [] countDoneTime;
    private int crtPos;
    private int fingerNum;
    boolean slideSuccess;
    public StageMultiTouchSlide(Context context, int fingerNum, @Nullable String info){
        super(context);
        countDoneTime = new int[totalPosNum];
        for(int i = 0; i < totalPosNum; ++ i)
            countDoneTime[i] = 0;
        if(info == null)
            InfoWhenStart = "在接下来的实验中，你需要将方块，沿着粗线的方向快速地滑动，不必准确控制滑动的最终位置";
        else
            InfoWhenStart = info;
        this.fingerNum = fingerNum;
        slideSuccess = false;
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
        if(e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if(e.getPointerCount() == fingerNum)
                slideSuccess = true;
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_UP){
            if(slideSuccess){
                next();
                invalidate();
                slideSuccess = false;
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
        }
        return null;
    }

    Shape getCrtShapeByPos(int p){
        switch (p){
            case LEFT:
                return new Circle(0, screenHeight / 2, halfWidth);
            case RIGHT:
                return new Circle(screenWidth, screenHeight / 2, halfWidth);
            case UP:
                return new Circle(screenWidth / 2, 0, halfWidth);
            case DOWN:
                return new Circle(screenWidth / 2, screenHeight, halfWidth);
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
