package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class LostFindActivity extends ActionBarActivity {
    private SharedPreferences mPrefs;
    private TextView tv_Safephone;
    private ImageView iv_protect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);

        boolean configed = mPrefs.getBoolean("configed", false);//判断是否进入过设置向导
        if (configed){
            setContentView(R.layout.activity_lost_find);
            //根据sp更新安全号码
            tv_Safephone = (TextView) findViewById(R.id.tv_phone);
            String phone = mPrefs.getString("safe_phone", "");
            tv_Safephone.setText(phone);

            //根据sp更新锁
            iv_protect = (ImageView) findViewById(R.id.iv_protect);
            boolean protect = mPrefs.getBoolean("protect", false);
            if (protect){
                iv_protect.setImageResource(R.drawable.lock);
            }else {
                iv_protect.setImageResource(R.drawable.unlock);
            }

        }else {
            //跳转设置向导页
            finish();
            startActivity(new Intent(this,Setup1Activity.class));
        }


    }
    //重新进入设置向导
    public void reEnter(View view){
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }
}
