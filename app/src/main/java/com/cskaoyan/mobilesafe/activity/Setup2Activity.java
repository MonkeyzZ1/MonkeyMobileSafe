package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class Setup2Activity extends BaseSetupActivity {
    private SettingItemView siv_sim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        siv_sim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = mPrefs.getString("sim", null);
        if (!TextUtils.isEmpty(sim)){
            siv_sim.setChecked(true);
        }else {
            siv_sim.setChecked(false);
        }

        siv_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siv_sim.isChecked()){
                    siv_sim.setChecked(false);
                    mPrefs.edit().remove("sim").commit();//删除已经绑定的sim卡
                }else {
                    siv_sim.setChecked(true);
                    //保存SIM卡信息
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();//获取sim卡序列号
                    Log.i("SIM卡序列号:", simSerialNumber + "");

                    mPrefs.edit().putString("sim",simSerialNumber).commit();
                }
            }
        });


    }
    public void showPreviousPage(){
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_previousin, R.anim.trans_previousout);//进入动画和退出动画

    }
    public void showNextPage(){
        //如果sim卡没有绑定，就不允许进入下一个页面
        String sim = mPrefs.getString("sim", null);
        if (TextUtils.isEmpty(sim)){
            Toast.makeText(this, "您还未绑定sim卡，请绑定先~", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }





}
