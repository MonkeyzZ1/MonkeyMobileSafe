package com.cskaoyan.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.monkeyzzi.monkeymobilesafe.R;

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector mDetector;
    public SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setup);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            //监听手势滑动事件
            //e1表示起始点，e2表示滑动终点，velocityX表示水平速度，velocityY表示竖直速度
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //判断纵向滑动幅度是否过大，过大的话不允许切换界面
                if (Math.abs(e2.getRawY()-e2.getRawY())>100){
                    Toast.makeText(BaseSetupActivity.this, "会不会滑呀~你酱紫滑是不对的呢~", Toast.LENGTH_SHORT).show();
                    return true ;
                }
                if (Math.abs(velocityX)<100){
                    Toast.makeText(BaseSetupActivity.this, "会不会滑呀~你酱紫滑是不对的呢~滑的太慢了吖", Toast.LENGTH_SHORT).show();
                    return true ;
                }
                //向右滑，上一页
                if (e2.getRawX()-e1.getRawX()>150){
                    showPreviousPage();
                    return true ;
                }
                //向左滑，下一页
                if (e1.getRawX()-e2.getRawX()>150){
                    showNextPage();
                    return true ;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    public abstract void showPreviousPage();

    public abstract void showNextPage();

    //下一页
    public void next(View view) {
        showNextPage();
    }

    //上一页
    public void previous(View view) {
        showPreviousPage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);//委托手势识别器处理触摸事件
        return super.onTouchEvent(event);
    }

}
