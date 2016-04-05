package com.Utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by MonkeyzZi on 2016/4/4.
 */
public class MD5Utils {
    //MD5加密
    public static String encode(String password) {


        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(password.getBytes());

            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);//将整数转为16进制
                Log.i("hexString", hexString + "");
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;//如果是1位的话，补0
                }
                sb.append(hexString);

            }
            Log.i("MD5:", sb.toString() + "");//MD5都是32位
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //没有该算法时抛出异常
        }
        return "";
    }

}
