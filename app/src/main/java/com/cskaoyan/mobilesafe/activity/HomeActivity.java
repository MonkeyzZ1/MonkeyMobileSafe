package com.cskaoyan.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Utils.MD5Utils;
import com.example.monkeyzzi.monkeymobilesafe.R;

public class HomeActivity extends Activity {
    private GridView gv_home;
    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] mPics = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
            R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings};
    private Button bt_ok;
    private Button bt_off;
    private EditText et_password;
    private EditText et_password_confirm;
    private SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter());
        //设置监听
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //手机防盗
                        showPasswordDialog();
                        break;
                    case 7:
                        //高级工具
                        startActivity(new Intent(HomeActivity.this,AToolsActivity.class));

                        break;
                    case 8:
                        //设置中心
                        startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                        break;
                    default:
                        break;
                }

            }
        });
    }

    private void showPasswordDialog() {
        //判断是否设置密码
        String savedPassword = mPrefs.getString("password", null);
        if (!TextUtils.isEmpty(savedPassword)){
            showPasswordInputDialog();
        }else{

            //如果没有设置过，弹出设置密码的弹窗
            showPasswordSetDialog();

        }
    }
    //输入密码弹窗
    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_input_password, null);
        dialog.setView(view,0,0,0,0);

        et_password = (EditText)view.findViewById(R.id.et_password);

        bt_ok  = (Button) view.findViewById(R.id.bt_ok);
        bt_off = (Button) view.findViewById(R.id.bt_cancel);

        //点击确定
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                //密码校验++++
                if (!TextUtils.isEmpty(password)) {
                    String savedPassword = mPrefs.getString("password", null);
                    if (MD5Utils.encode(password).equals(savedPassword)){
                        Toast.makeText(HomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        //跳转到手机防盗页

                        startActivity(new Intent(HomeActivity.this,LostFindActivity.class));

                    }else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HomeActivity.this, "输入框内容不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //点击取消
        bt_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//隐藏dialog
            }
        });



        dialog.show();

    }

    //设置密码的弹窗
    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_set_password, null);
        dialog.setView(view,0,0,0,0);

        et_password = (EditText)view.findViewById(R.id.et_password);
        et_password_confirm = (EditText)view.findViewById(R.id.et_password_confirm);

        bt_ok  = (Button) view.findViewById(R.id.bt_ok);
        bt_off = (Button) view.findViewById(R.id.bt_cancel);

        //点击确定
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password        = et_password.getText().toString();
                String passwordConfirm = et_password_confirm.getText().toString();

                if (!TextUtils.isEmpty(password) && !passwordConfirm.isEmpty()){
                    if (password.equals(passwordConfirm)){
                        Toast.makeText(HomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        //将密码保存起来
                        mPrefs.edit().putString("password", MD5Utils.encode(password)).commit();

                        dialog.dismiss();


                    }else {
                        Toast.makeText(HomeActivity.this, "你输入的两次密码不一致哦~", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(HomeActivity.this, "输入框内容不能为空", Toast.LENGTH_SHORT).show();

                }


            }
        });
        //点击取消
        bt_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//隐藏dialog
            }
        });



        dialog.show();

    }


    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);

            ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

            tv_item.setText(mItems[position]);
            iv_item.setImageResource(mPics[position]);

            return view;
        }
    }

}

