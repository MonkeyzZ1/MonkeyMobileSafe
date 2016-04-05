package com.cskaoyan.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class SettingActivity extends Activity {
    private SettingItemView siv_update;//设置升级
    private SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPrefs = getSharedPreferences("config", MODE_PRIVATE);

        siv_update = (SettingItemView)findViewById(R.id.siv_update);
        // siv_update.setTitle("自动更新设置");

        boolean autoUpdate = mPrefs.getBoolean("auto_update", true);
        if (autoUpdate){
            // siv_update.setDesc("自动更新已经开启");
            siv_update.setChecked(true);
        }else {
            // siv_update.setDesc("自动更新已关闭");
            siv_update.setChecked(false);
        }
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断勾选状态
                if (siv_update.isChecked()){
                    //设置不勾选
                    siv_update.setChecked(false);
                    siv_update.setDesc("自动更新已关闭");
                    mPrefs.edit().putBoolean("auto_update",false).commit();
                }else{
                    siv_update.setChecked(true);
                    siv_update.setDesc("自动更新已经开启");
                    mPrefs.edit().putBoolean("auto_update",true).commit();

                }
            }
        });
    }
}