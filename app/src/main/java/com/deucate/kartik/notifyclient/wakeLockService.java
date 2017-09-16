package com.deucate.kartik.notifyclient;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class wakeLockService extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context,new Intent(context,BackService.class));
    }
}
