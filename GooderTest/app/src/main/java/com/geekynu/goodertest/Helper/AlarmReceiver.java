package com.geekynu.goodertest.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.geekynu.goodertest.Service.AlarmService;

/**
 * Created by yuanhonglei on 8/14/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startService = new Intent(context, AlarmService.class);
        startService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startService);
    }
}
