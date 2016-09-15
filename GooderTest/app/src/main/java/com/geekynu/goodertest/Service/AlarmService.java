package com.geekynu.goodertest.Service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import android.support.annotation.Nullable;

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
            }
        }).start();
        AlarmTimer();
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
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish(final String response) {
                mySensorList = Utility.handleSensorResponse(response);
                if (SensorInfo.alarmDeviceNum != 0) {
                    Uri soundUri = Uri.fromFile(new File("/system/media/audio/notifications/Ding.ogg"));
                    long[] vibrates = {0, 1000, 1000, 1000};
                    Intent alarmIntent = new Intent(AlarmService.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(AlarmService.this, 0, alarmIntent, 0);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new Notification.Builder(AlarmService.this)
                            .setContentTitle(SensorInfo.alarmDeviceNum + " 个传感器数据异常")
                            .setContentText("请立即检查该传感器所在环境！")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setSound(soundUri)
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(vibrates)
                            .setFullScreenIntent(pendingIntent, false)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .build();
                    manager.notify(420, notification);
                }
                SensorInfo.alarmDeviceNum = 0;
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void AlarmTimer() {
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int fiveMins = 5 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + fiveMins;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
    }
}
