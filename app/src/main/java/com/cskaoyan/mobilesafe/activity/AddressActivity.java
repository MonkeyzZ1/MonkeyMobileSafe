package com.cskaoyan.mobilesafe.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.Utils.AddressDao;
import com.example.monkeyzzi.monkeymobilesafe.R;

public class AddressActivity extends ActionBarActivity {

    private EditText et_number;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        et_number = (EditText) findViewById(R.id.et_number);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }
    //开始查询
    public void query(View view){
        String number = et_number.getText().toString().trim();
        if (!TextUtils.isEmpty(number)){
            String address = AddressDao.getAdderss(number);
            tv_result.setText(address);
        }


    }
}
