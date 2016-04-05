package com.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.monkeyzzi.monkeymobilesafe.R;

/**
 * Created by MonkeyzZi on 2016/4/5.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        for (Object object: objects){//短信最多140字节，超出的话，会分为多条短信发送
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);

            String originatingAddress = message.getOriginatingAddress();//短信来源好吗

            String messageBody = message.getMessageBody();//短信内容

            Log.i("originatingAddress" + originatingAddress, "messageBody" + messageBody);

            if ("#*alarm*#".equals(messageBody)){
                //播放报警音乐
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1f,1f);
                player.setLooping(true);
                player.start();


                abortBroadcast();//中断短信的传递，从而系统短信app就收不到内容了
            }else if ("#*location*#".equals(messageBody)){
                //获取经纬度坐标
                context.startService(new Intent(context,LocationService.class));

                SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);

                String location = sp.getString("location", "");

                Log.i("location:",location+"");

                abortBroadcast();
            }else if ("#*wipedata*#".equals(messageBody)){

                abortBroadcast();
            }else if ("#*lockscreen*#".equals(messageBody)){

                abortBroadcast();
            }

        }

    }
}
