package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

/**
 * Created by kinnplh on 2016/12/22.
 */

public class StageTangentialForce extends MainOperationView {
    final static int LongTouchTimeMs = 1000;
    final static int targetX = 3;
    final static int targetY = 3;
    final static int repeatTimes = 1;
    final static int halfWidth = 200;
    final static int forceDirectionNum = 4;
    final static int assistantLineLength = 200;
    final static int LEFT = 0;
    final static int RIGHT = 1;
    final static int UP = 2;
    final static int DOWN = 3;



    Shape touchTarget;
    Shape assistantLine;
    private int crtX;
    private int crtY;
    private int crtForceDirection;
    private boolean lastTouchInside;
    private int [][][] testTimeCounter;
    private boolean longTouched;
    private Timer t;
    public StageTangentialForce(Context context, @Nullable String info){
        super(context);
        crtX = 1;
        crtY = 1;

        setBackgroundColor(WHITE);
        testTimeCounter = new int[targetX][targetY][forceDirectionNum];
        for(int i = 0; i < targetX; ++ i)
            for(int j = 0; j < targetY; ++ j)
                for(int k = 0; k < forceDirectionNum; ++ k)
                testTimeCounter[i][j][k] = 0;
        if(info == null)
            InfoWhenStart = "在接下来的实验中，你需要以给定的握姿，长按击屏幕上出现的正方形，直到正方形变红。一共需要长按45次。";
        else
            InfoWhenStart = info;
        longTouched = false;
        next();
    }

    @Override
    boolean next() {
        int remainPos = getToDoPosNum();
        if(remainPos == 0) {
            crtStageFinished = true;
            return false;
        }

        Random random = new Random();
        int index = random.nextInt(remainPos) + 1;

        for(crtX = 0; crtX < targetX; ++ crtX) {
            for (crtY = 0; crtY < targetY; ++crtY) {
                for (crtForceDirection = 0; crtForceDirection < forceDirectionNum; ++crtForceDirection) {
                    if (testTimeCounter[crtX][crtY][crtForceDirection] < repeatTimes)
                        index -= 1;
                    if (index == 0)
                        break;
                }
                if (index == 0)
                    break;
            }
            if(index == 0)
                break;
        }
        if(testTimeCounter[crtX][crtY][crtForceDirection] >= repeatTimes){
            Log.e("Error", "Error in generate next in stage touch");
        }
        testTimeCounter[crtX][crtY][crtForceDirection] += 1;
        crtX += 1;
        crtY += 1;
        FloatPoint tem = getGridPoint(crtX, crtY, targetX, targetY);
        touchTarget = new Rectangle(tem.x, tem.y, halfWidth, halfWidth);
        assistantLine = generateAssistantLineByPosAndDirection(crtForceDirection);

        return true;
    }


    @Override
    public void onDraw(Canvas canvas){
        if(crtStageFinished)
            return;
        if(longTouched)
            p.setColor(RED);
        else
            p.setColor(BLACK);
        touchTarget.draw(canvas, p);
        assistantLine.draw(canvas, p);
    }

    @Override
    public boolean eventHandler(MotionEvent e){
        Log.d("prjDebug", "TouchStageEvent");
        //only support single touch point
        if(crtStageFinished)
            return false;
        if(e.getActionMasked() == MotionEvent.ACTION_DOWN || e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            if(touchTarget.inside(e.getX(), e.getY())){
                lastTouchInside = true;
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d("In handler", "be transparent");
                        longTouched = true;
                        invalidate();
                        super.handleMessage(msg);
                    }
                };

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                };

                t = new Timer();
                t.schedule(task, LongTouchTimeMs);
            }
        }


        if(e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if(!lastTouchInside)
                return false;
            if(touchTarget.inside(e.getX(), e.getY()))
                return false;
            if(t != null) {
                t.cancel();
                t = null;
            }
        }

        if(e.getActionMasked() == MotionEvent.ACTION_POINTER_UP || e.getActionMasked() == MotionEvent.ACTION_UP){
            if(!lastTouchInside)
                return false;
            if(t != null)
                t.cancel();
            if(longTouched){
                next();
                invalidate();
                longTouched = false;
                t = null;
            }

            lastTouchInside = false;
        }
        return false;
    }

    private int getToDoPosNum(){
        int res = 0;
        for(int i = 0; i < targetX; ++ i)
            for(int j = 0;j < targetY; ++ j)
                for(int k = 0; k < forceDirectionNum; ++ k)
                    if(testTimeCounter[i][j][k] < repeatTimes)
                        res += 1;
        return res;
    }

    private Shape generateAssistantLineByPosAndDirection(int d){
        switch (d){
            case LEFT:
                return new Line(touchTarget.centerX, touchTarget.centerY,
                        touchTarget.centerX - assistantLineLength, touchTarget.centerY);
            case RIGHT:
                return new Line(touchTarget.centerX, touchTarget.centerY,
                        touchTarget.centerX + assistantLineLength, touchTarget.centerY);
            case UP:
                return new Line(touchTarget.centerX, touchTarget.centerY,
                        touchTarget.centerX, touchTarget.centerY - assistantLineLength);
            case DOWN:
                return new Line(touchTarget.centerX, touchTarget.centerY,
                        touchTarget.centerX, touchTarget.centerY + assistantLineLength);
        }
        return null;
    }
}
