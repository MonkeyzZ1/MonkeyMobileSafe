package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {
    private CheckBox cb_protect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        //根据sp保存的状态，更新checkbox
        boolean protect = mPrefs.getBoolean("protect", false);
        if (protect){
            cb_protect.setText("防盗保护已经开启");
            cb_protect.setChecked(true);
        }else {
            cb_protect.setText("防盗保护没有开启");
            cb_protect.setChecked(false);
        }

        //当checkbox发生变化时，调用此方法
        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_protect.setText("防盗保护已经开启");
                    mPrefs.edit().putBoolean("protect",true).commit();


                }else {
                    cb_protect.setText("防盗保护没有开启");
                    mPrefs.edit().putBoolean("protect",false).commit();


                }

            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_previousin, R.anim.trans_previousout);//进入动画和退出动画

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, LostFindActivity.class));
        finish();
        mPrefs.edit().putBoolean("configed",true).commit();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }

}
