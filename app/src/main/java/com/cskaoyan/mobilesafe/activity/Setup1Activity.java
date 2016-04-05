package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showPreviousPage() {

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(Setup1Activity.this,Setup2Activity.class));
        finish();

        //两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }

}
