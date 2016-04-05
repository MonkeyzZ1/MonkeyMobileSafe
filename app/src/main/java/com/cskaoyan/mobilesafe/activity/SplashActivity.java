package com.cskaoyan.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Utils.StreamUtils;
import com.example.monkeyzzi.monkeymobilesafe.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends ActionBarActivity {
    private TextView tv_version;
    private TextView tv_progress;
    private String mVersionName;
    private int mVersionCode;
    private String mDesc;
    protected static final int CODE_UPDATE_DIALOG=0;
    protected static final int CODE_URL_ERROR=1;
    protected static final int CODE_NET_ERROR=2;
    protected static final int CODE_JSON_ERROR=3;
    protected static final int CODE_ENTER_HOME=4;


    private String mDownloadUrl;

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case CODE_UPDATE_DIALOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "json错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;

                default:
                    break;



            }
        }
    };
    private SharedPreferences mPrefs;
    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        tv_version = (TextView)findViewById(R.id.tv_version);
        tv_version.setText("版本号:" + getVersionName());
        //默认隐藏
        tv_progress = (TextView)findViewById(R.id.tv_progress);

        mPrefs = getSharedPreferences("config", MODE_PRIVATE);

        copyDB("address.db");//拷贝归属地查询数据库
        //判断是否需要自动更新
        boolean autoUpdate = mPrefs.getBoolean("auto_update", true);
        if (autoUpdate){
            checkVersion();
        }else {
            myhandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,2000);//延时2后发送消息
        }
        //渐变的动画效果
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        rl_root.startAnimation(anim);
    }
    private  String getVersionName(){
        PackageManager packageManager = getPackageManager();
        try {
            //获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            Log.i("versionCode&versionName", versionCode + " " + versionName + " ");

            return versionName ;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }
    private  int getVersionCode(){
        PackageManager packageManager = getPackageManager();
        try {
            //获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode ;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;

    }
    /*从服务器获取版本信息进行校验
    *
    * */
    private void checkVersion(){
        final long startTime = System.currentTimeMillis();

        new Thread(){
            @Override
            public void run() {
                Message msg = Message.obtain();
                HttpURLConnection conn =null;
                try {
                    URL url = new URL("http://192.168.3.33:8080/update.json");
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");//设置请求方法
                    conn.setConnectTimeout(5000);//设置连接超时
                    conn.setReadTimeout(5000);//设置响应超时，连接上了，但是服务器迟迟不给响应
                    conn.connect();//连接服务器

                    int responseCode = conn.getResponseCode();
                    if (responseCode==200){
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        Log.i("网络返回:",result+"");

                        //解析json
                        JSONObject jo = new JSONObject(result);

                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDesc = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");

                        Log.i("版本描述:",mDownloadUrl);
                        if (mVersionCode>getVersionCode()){
                            //判断是否有更新
                            //服务器的VersionCode大于本地的VersionCode
                            msg.what = CODE_UPDATE_DIALOG;
                        }else{
                            //没有版本更新
                            msg.what=CODE_ENTER_HOME;
                        }

                    }


                } catch (MalformedURLException e) {
                    //url错误的异常
                    msg.what= CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误的异常
                    msg.what= CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    //json解析失败
                    msg.what= CODE_JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    long endTime = System.currentTimeMillis();
                    //访问网络花费的时间
                    long timeUsed= endTime-startTime;

                    if (timeUsed<2000){
                        //强制休眠一段时间，保证闪屏页显示两秒钟
                        try {
                            Thread.sleep(2000-timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    myhandler.sendMessage(msg);

                    if (conn!=null){
                        conn.disconnect();
                    }
                }

            }
        }.start();

    }
    protected void showUpdateDailog(){

        new AlertDialog.Builder(this)
                .setTitle("发现新版本"+mVersionName)
                .setMessage(mDesc)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        enterHome();
                    }
                })
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Tag", "立即更新");
                        download();
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        enterHome();
                    }
                }).show();
    }
    //下载apk文件
    protected void download(){
        //判断是否有sdcard

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            tv_progress.setVisibility(View.VISIBLE);//显示进度
            String target = Environment.getExternalStorageDirectory()+"/update.apk";
            //XUtils
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                //下载文件的进度
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.i("下载进度:", current + "/" + total);
                    tv_progress.setText("下载进度:"+current*100/total+"%");
                }
                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i("下载成功！！！","");
                    //跳转到系统下载页面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent,0);//如果用户取消安装的话，会返回结果

                }
                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(SplashActivity.this, "您的手机没有SD卡", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);

    }

    //进入主页面
    private void enterHome(){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();

    }
    //拷贝数据库
    private void copyDB(String dbName){

        File destFile = new File(getFilesDir(),dbName);

        if (destFile.exists()){
            Log.i("数据库",dbName+"已经存在");
            return;
        }

        FileOutputStream out = null;
        InputStream in = null ;
        try {
            in = getAssets().open(dbName);
            out=new FileOutputStream(destFile);

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len=in.read(bytes))!=-1){
                out.write(bytes,0,len);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
