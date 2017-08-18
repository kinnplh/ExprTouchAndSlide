package com.example.kinnplh.exprtouchandslide;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;

/**
 * Created by kinnplh on 2016/12/22.
 */

public class StageZoom extends MainOperationView {

    boolean zoomIn;
    Shape zoomObj;
    Shape zoomTarget;
    final static int halfWidth = 600;
    final static int largeHalfWidth = 800;
    final static int smallHalfWidth = 400;
    final static int repeatTimes = 1;

    private int repeatCounter;
    private double initDis;
    private boolean inZoomProcess;
    StageZoom(Context context, boolean zoomIn, @Nullable String info){
        super(context);
        this.zoomIn = zoomIn;
        inZoomProcess = false;
        if(info == null)
            InfoWhenStart = "在接下来的实验中，你需要以规定的手势将屏幕中间的黑色正方形用双指进行缩放，直到其与红色正方形的大小关系发生变化。共需缩放5次。";
        else
            InfoWhenStart = info;
        next();
    }


    @Override
    boolean next() {
        if(crtStageFinished)
            return false;
        if(repeatCounter >= repeatTimes){
            crtStageFinished = true;
            return false;
        }
        createNewTarget();
        zoomObj = new Rectangle(screenWidth / 2, screenHeight / 2, halfWidth, halfWidth);
        return true;
    }

    @Override
    boolean eventHandler(MotionEvent e) {
        if(e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN || e.getActionMasked() == MotionEvent.ACTION_DOWN){
          if(e.getPointerCount() != 2) {
              inZoomProcess = false;
              return false;
          }
          if(zoomObj.inside(e.getX(0), e.getY(0)) && zoomObj.inside(e.getX(1), e.getY(1))) {
              inZoomProcess = true;
              initDis = Math.sqrt((Math.pow(e.getX(0) - e.getX(1), 2) + Math.pow(e.getX(0) - e.getX(1), 2)));
          }
          else
            inZoomProcess = false;
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if(inZoomProcess){
                double crtDis = Math.sqrt((Math.pow(e.getX(0) - e.getX(1), 2) + Math.pow(e.getX(0) - e.getX(1), 2)));
                double ratio = crtDis / initDis;
                ((Rectangle)zoomObj).zoom(ratio);
                invalidate();
                initDis = crtDis;
            }
            else
                inZoomProcess = false;
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_UP){
            inZoomProcess = false;
            if(zoomIn && ((Rectangle) zoomObj).height > ((Rectangle)zoomTarget).height
                    && ((Rectangle) zoomObj).width > ((Rectangle) zoomTarget).width){
                next();
                invalidate();
            }
            else if(!zoomIn && ((Rectangle) zoomObj).height < ((Rectangle)zoomTarget).height
                    && ((Rectangle) zoomObj).width < ((Rectangle) zoomTarget).width){
                next();
                invalidate();
            }
        }
        else if(e.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
            inZoomProcess = false;
        }
        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(crtStageFinished)
            return;
        p.setColor(RED);
        zoomTarget.draw(canvas, p);
        p.setColor(BLACK);
        zoomObj.draw(canvas, p);
    }

    private void createNewTarget(){
        if(zoomIn)
            zoomTarget = new Rectangle(screenWidth / 2, screenHeight / 2, largeHalfWidth, largeHalfWidth);
        else
            zoomTarget = new Rectangle(screenWidth / 2, screenHeight / 2, smallHalfWidth, smallHalfWidth);
        repeatCounter += 1;
    }
}
