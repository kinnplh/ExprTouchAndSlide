package com.example.kinnplh.exprtouchandslide;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.WHITE;


/*更加符合实际的  场景
    不管点到哪里都可以 给点击的位置加上标志 直到所有的图标被覆盖  桌面和键盘（全键盘）  图片

    拖拽 图标  滑动条  地图
    快速滑动  翻页（上下、左右）。。
    长按 logo  长按菜单  选取文字 图片

    切向力 转圈的形式
    指关节  双击、写字
    缩放 照片

    多指  地图

    不用写~ 缩放 拖动 旋转 快速滑动 长按 -- 桌面图标
    点击 -- 屏幕  folder

    桌面程序  控制任务

    每个任务找到合适的场景  场景的种类较少 不受影响
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new ControlLayout(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return super.onKeyDown(keyCode, event);
        return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        //return super.onKeyLongPress(keyCode, event);
        return false;
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        //return super.onKeyShortcut(keyCode, event);
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //return super.onKeyUp(keyCode, event);
        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        //return super.onKeyMultiple(keyCode, repeatCount, event);
        return false;
    }
}


class ControlLayout extends FrameLayout{

    MainOperationView[] views;
    private int crtViewIndex;
    Context parent;
    public static String SDCardPath;
    private void initViews(){
        SDCardPath = Environment.getExternalStorageDirectory().getPath() + "/huaweiTouch/" + new Date().toGMTString() + ".txt";
        views = new MainOperationView[24];
        views[0] = new StageTouch(parent, "在接下来的实验中，你需要以【单手拇指握姿】，【轻点】屏幕上出现的正方形。一共需要点击45次。");
        views[1] = new StageTouch(parent, "在接下来的实验中，你需要以【双手食指握姿】，【轻点】屏幕上出现的正方形。一共需要点击45次。");
        views[2] = new StageTouch(parent, "在接下来的实验中，你需要以【单手拇指握姿】，【重点】屏幕上出现的正方形。一共需要点击45次。");
        views[3] = new StageTouch(parent, "在接下来的实验中，你需要以【双手食指握姿】，【重点】屏幕上出现的正方形。一共需要点击45次。");
        views[4] = new StageTouch(parent, "在接下来的实验中，你需要以【双手食指握姿】，用【右手指关节】点击屏幕上出现的正方形。一共需要点击45次。");
        views[5] = new StageDrag(parent, "在接下来的实验中，你需要以【单手拇指握姿】，将屏幕中间的正方形【沿直线】拖动到圆圈所在的位置（以手指进入圆圈为准），并松手。共需拖拽20次。");
        views[6] = new StageDrag(parent, "在接下来的实验中，你需要以【双手食指握姿】，将屏幕中间的正方形【沿直线】拖动到圆圈所在的位置（以手指进入圆圈为准），并松手。共需拖拽20次。");
        views[7] = new StageSlide(parent, "在接下来的实验中，你需要以【单手拇指握姿】将方块，沿着【粗线的方向】快速地滑动，不必准确控制滑动的最终位置。共需要滑动40次。");
        views[8] = new StageSlide(parent, "在接下来的实验中，你需要以【双手食指握姿】将方块，沿着【粗线的方向】快速地滑动，不必准确控制滑动的最终位置。共需要滑动40次。");
        views[9] = new StageLongTouch(parent, "在接下来的实验中，你需要以【单手拇指握姿】，长按击屏幕上出现的正方形，直到正方形变红。一共需要长按45次。");
        views[10] = new StageLongTouch(parent, "在接下来的实验中，你需要以【双手食指握姿】，长按击屏幕上出现的正方形，直到正方形变红。一共需要长按45次。");
        views[11] = new StageLongTouch(parent, "在接下来的实验中，你需要以【双手食指握姿】，用【右手指关节】长按击屏幕上出现的正方形，直到正方形变红。一共需要长按45次。");
        views[12] = new StageTangentialForce(parent, "在接下来的实验中，你需要以【单手拇指握姿】，对屏幕上的方块，从方块中心施加向【线段方向】的切向力，直到图案变红。一共需要施加180次切向力。");
        views[13] = new StageTangentialForce(parent, "在接下来的实验中，你需要以【双手食指握姿】，对屏幕上的方块，从方块中心施加向【线段方向】的切向力，直到图案变红。一共需要施加180次切向力。");
        views[14] = new StageZoom(parent, true, "在接下来的实验中，你需要以【双拇指握姿】，使用【两只拇指】将屏幕中间的黑色正方形用双指进行放大，直到其大于红色正方形。共需放大5次。");
        views[15] = new StageZoom(parent, true, "在接下来的实验中，你需要以【双手食指握姿】，使用【同手拇指和食指】将屏幕中间的黑色正方形用双指进行放大，直到其大于红色正方形。共需放大5次。");
        views[16] = new StageZoom(parent, false, "在接下来的实验中，你需要以【双拇指握姿】，使用【两只拇指】将屏幕中间的黑色正方形用双指进行缩小，直到红色正方形露出。共需放大5次。");
        views[17] = new StageZoom(parent, false, "在接下来的实验中，你需要以【双手食指握姿】，使用【同手拇指和食指】将屏幕中间的黑色正方形用双指进行缩小，直到红色正方形露出。共需放大5次。");
        views[18] = new StageRotate(parent, "在接下来的实验中，你需要以【双拇指握姿】，使用【两只拇指】将屏幕中间的黑色正方形用双指进行【顺时针】旋转，对旋转的最终位置【没有要求】。共需旋转5次。");
        views[19] = new StageRotate(parent, "在接下来的实验中，你需要以【双手食指握姿】，使用【同手拇指和食指】将屏幕中间的黑色正方形用双指进行【顺时针】旋转，对旋转的最终位置【没有要求】。共需旋转5次。");
        views[20] = new StageRotate(parent, "在接下来的实验中，你需要以【双拇指握姿】，使用【两只拇指】将屏幕中间的黑色正方形用双指进行【逆时针】旋转，对旋转的最终位置【没有要求】。共需旋转5次。");
        views[21] = new StageRotate(parent, "在接下来的实验中，你需要以【双手食指握姿】，使用【同手拇指和食指】将屏幕中间的黑色正方形用双指进行【逆时针】旋转，对旋转的最终位置【没有要求】。共需旋转5次。");
        views[22] = new StageMultiTouchSlide(parent, 2, "在接下来的实验中，你需要以【双手食指握姿】，使用【两只手指】从【屏幕边缘的圆圈】开始，大致沿着【直线方向】，进行多指滑动。共需滑动20次。");
        views[23] = new StageMultiTouchSlide(parent, 3, "在接下来的实验中，你需要以【双手食指握姿】，使用【三只手指】从【屏幕边缘的圆圈】开始，大致沿着【直线方向】，进行多指滑动。共需滑动20次。");

        crtViewIndex = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle(String.format("阶段%d", crtViewIndex + 1));
        builder.setMessage(views[crtViewIndex].InfoWhenStart);
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addView(views[crtViewIndex]);
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        AlertDialog res = builder.create();
        res.setCanceledOnTouchOutside(false);
        res.show();

    }


    public ControlLayout(Context context) {
        super(context);
        setBackgroundColor(WHITE);
        parent = context;
        initViews();
    }

    public boolean onTouchEvent(MotionEvent e){
        views[crtViewIndex].eventHandler(e);
        if(views[crtViewIndex].crtStageFinished){
            removeView(views[crtViewIndex]);
            crtViewIndex += 1;
            if(crtViewIndex == views.length){
                AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                builder.setMessage("实验结束，感谢您的参与。");
                builder.setTitle("信息");
                builder.setPositiveButton("开始新实验", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        initViews();
                    }
                });
                builder.setNegativeButton("结束实验", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((Activity) parent).finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return true;
                    }
                });
                AlertDialog res = builder.create();
                res.setCanceledOnTouchOutside(false);
                res.show();

                Log.i("end","It is the end!");
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                builder.setTitle(String.format("阶段%d", crtViewIndex + 1));
                builder.setMessage(views[crtViewIndex].InfoWhenStart);
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addView(views[crtViewIndex]);
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return true;
                    }
                });
                AlertDialog res = builder.create();
                res.setCanceledOnTouchOutside(false);
                res.show();
            }
        }

        return true;
    }
}
