package com.example.snuuz;


import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;




public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //receives the alarm data and starts a service at the given time.
        Intent AlarmNotif = new Intent(context, AlarmService.class);
        context.startService(AlarmNotif);


    }
}
