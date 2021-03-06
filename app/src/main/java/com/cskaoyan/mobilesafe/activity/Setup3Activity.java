package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.monkeyzzi.monkeymobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {
    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_phone = (EditText) findViewById(R.id.et_phone);

        String phone = mPrefs.getString("safe_phone", "");
        et_phone.setText(phone);

    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_previousin, R.anim.trans_previousout);//进入动画和退出动画

    }

    @Override
    public void showNextPage() {
        String phone = et_phone.getText().toString().trim();//过滤空格

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mPrefs.edit().putString("safe_phone",phone).commit();//保存安全号码
        startActivity(new Intent(this, Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画


    }
    //选择联系人
    public void selectContact(View view){
        Intent intent = new Intent(this,ContactActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phone = data.getStringExtra("phone");
        phone=phone.replaceAll("-","").replaceAll(" ","");
        et_phone.setText(phone);//把电话号码设置给输入框
        super.onActivityResult(requestCode, resultCode, data);
    }
}