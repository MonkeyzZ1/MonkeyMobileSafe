package com.Utils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by MonkeyzZi on 2016/4/5.
 */
public class LocationService  extends Service {

    private MyLcationListener listener;
    private LocationManager lm;
    private SharedPreferences mPrefs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPrefs = getSharedPreferences("config", MODE_PRIVATE);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
       /* List<String> AllProviders=lm.getAllProviders();
        Log.i("AllProviders:",AllProviders+"");*/
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);//是否允许付费，比如使用3g网络定位
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);

        listener = new MyLcationListener();
        lm.requestLocationUpdates(bestProvider, 0, 0, listener);


    }
    class MyLcationListener implements LocationListener {
        //位置发生变化
        @Override
        public void onLocationChanged(Location location) {
            String longitude ="经度:"+location.getLongitude();
            String latitude ="纬度:"+location.getLatitude();
            mPrefs.edit().putString("location","longitude:"+location.getLongitude()+"; latitude:"+location.getLatitude()).commit();

            stopSelf();//停掉Service

        }
        //位置提供者状态发生变化
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        //用户打开GPS服务
        @Override
        public void onProviderEnabled(String provider) {

        }
        //用户关闭GPS服务
        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);//当Activity销毁时，停止更新位置，节省电量
    }

}


