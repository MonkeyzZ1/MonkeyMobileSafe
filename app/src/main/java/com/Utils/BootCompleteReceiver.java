package com.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by MonkeyzZi on 2016/4/5.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protect = sp.getBoolean("protect", false);
        if (protect){
            String sim = sp.getString("sim", null);//获取绑定的sim卡

            if (!TextUtils.isEmpty(sim)){

                //获取当前的手机sim卡
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentSim = tm.getSimSerialNumber()+"111";//拿到当前手机的sim卡

                if (sim.equals(currentSim)){
                    Log.i("您的手机安全", "");
                }else {
                    Log.i("您的手机sim卡变更","发送报警短信");
                    String phone = sp.getString("safe_phone", "");//读取安全号码

                    //发送短信给安全号码
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone,null,"sim card has changed ！",null,null);
                }

            }
        }


    }
}
