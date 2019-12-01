package com.example.snuuz;

import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.net.Uri;

public class AlarmService extends Service {
    String date;
    IBinder Binder;
    Uri uri;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //opens an alarm notification activity.
        Intent testIntent = new Intent(this, AlarmNotif.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        date = intent.getStringExtra("date");
        testIntent.putExtra("date", date);
        startActivity(testIntent);
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //default binder method since i was getting an error without implementing it.
        return null;
    }
}
