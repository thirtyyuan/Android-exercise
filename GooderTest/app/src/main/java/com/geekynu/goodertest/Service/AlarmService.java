package com.geekynu.goodertest.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.geekynu.goodertest.Activity.MainActivity;
import com.geekynu.goodertest.Helper.AlarmReceiver;
import com.geekynu.goodertest.R;
import com.geekynu.goodertest.model.mySensor;
import com.geekynu.goodertest.util.HttpUtil;
import com.geekynu.goodertest.util.SensorInfo;
import com.geekynu.goodertest.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanhonglei on 8/14/16.
 */
public class AlarmService extends Service {
    private List<mySensor> mySensorList = new ArrayList<>();

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSensorInfo();
                AlarmTimer();

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getSensorInfo() {
        String getGatewayURL = "http://open.lewei50.com/api/V1/User/GetSensorswithgateway?userkey=";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AlarmService.this);
        final String userkey = prefs.getString("userKey", "");
        final String address  = getGatewayURL + userkey;
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                mySensorList = Utility.handleSensorResponse(response);
                if (mySensorList == null) {
                    if (SensorInfo.isAlarm) {
                        Intent alarmIntent = new Intent(AlarmService.this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(AlarmService.this, 0, alarmIntent, 0);
                        Notification notification = new Notification.Builder(AlarmService.this)
                                .setContentTitle(SensorInfo.sensorName + "数据异常")
                                .setContentText("请立即检查该传感器所在环境！")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pendingIntent)
                                .setWhen(System.currentTimeMillis())
                                .build();
                        startForeground(7, notification);
                    }
                } else {

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void AlarmTimer() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHours = 5 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHours;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
    }
}
