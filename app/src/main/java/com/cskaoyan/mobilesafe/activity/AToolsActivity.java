package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class AToolsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }
    //归属地查询
    public void numberAddressQuery(View view){

        startActivity(new Intent(this,AddressActivity.class));
    }
}
