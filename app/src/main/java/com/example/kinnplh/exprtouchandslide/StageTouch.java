package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

import static android.graphics.Color.WHITE;

/**
 * Created by kinnplh on 2016/12/21.
 */

public class StageTouch extends MainOperationView {
    final static int targetX = 3;
    final static int targetY = 3;
    final static int repeatTimes = 1;
    final static int halfWidth = 200;
    Shape touchTarget;
    private int crtX;
    private int crtY;
    private boolean lastTouchInside;
    private int [][] testTimeCounter;
    public StageTouch(Context context, @Nullable String info){
        super(context);
        setBackgroundColor(WHITE);
        testTimeCounter = new int[targetX][targetY];
        for(int i = 0; i < targetX; ++ i)
            for(int j = 0; j < targetY; ++ j)
                testTimeCounter[i][j] = 0;
        if(info == null)
            InfoWhenStart = "在接下来的实验中，你需要以给定的握姿，点击屏幕上出现的正方形。一共需要点击45次。";
        else
            InfoWhenStart = info;
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
                if (testTimeCounter[crtX][crtY] < repeatTimes)
                    index -= 1;
                if (index == 0)
                    break;
            }
            if(index == 0)
                break;
        }
        if(testTimeCounter[crtX][crtY] >= repeatTimes){
            Log.e("Error", "Error in generate next in stage touch");
        }
        testTimeCounter[crtX][crtY] += 1;
        crtX += 1;
        crtY += 1;
        FloatPoint tem = getGridPoint(crtX, crtY, targetX, targetY);
        touchTarget = new Rectangle(tem.x, tem.y, halfWidth, halfWidth);
        return true;
    }


    @Override
    public void onDraw(Canvas canvas){
        if(crtStageFinished)
            return;
        touchTarget.draw(canvas, p);
    }

    @Override
    public boolean eventHandler(MotionEvent e){
        Log.d("prjDebug", "TouchStageEvent");
        //only support single touch point
        if(crtStageFinished)
            return false;
        if(e.getActionMasked() == MotionEvent.ACTION_DOWN || e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            if(touchTarget.inside(e.getX(), e.getY()))
                lastTouchInside = true;
        }
        if(e.getActionMasked() == MotionEvent.ACTION_POINTER_UP || e.getActionMasked() == MotionEvent.ACTION_UP){
            if(lastTouchInside && touchTarget.inside(e.getX(), e.getY())){
                Log.d("prjDebug", "TouchStageEventUP");
                next();
                invalidate();
            }
            lastTouchInside = false;
        }
        return false;
    }


    private int getToDoPosNum(){
        int res = 0;
        for(int i = 0; i < targetX; ++ i)
            for(int j = 0;j < targetY; ++ j)
                if(testTimeCounter[i][j] < repeatTimes)
                    res += 1;
        return res;
    }

}
