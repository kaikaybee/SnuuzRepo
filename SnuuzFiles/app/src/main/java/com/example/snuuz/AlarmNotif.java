package com.example.snuuz;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmNotif extends AppCompatActivity {
    Button Dismiss;
    EditText WakeUpTIme;
    Button Snooze;

    AlarmManager alarm;
    PendingIntent alarmIntent;


    MyDB db;
    String date;
    String wake_up;
    String sleep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //new activity opened at the time the alarm was set. Bypasses screen locks.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notif);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);


        Snooze = findViewById(R.id.SnoozeBtn);
        //WakeUpTIme = findViewById((R.id.editTime));
        Snooze.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();
                calSet.set(Calendar.HOUR_OF_DAY, calNow.get(calNow.HOUR_OF_DAY));
                calSet.set(Calendar.MINUTE, calNow.get(calNow.MINUTE)+10);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);
                Intent AlarmIntent = new Intent(AlarmNotif.this, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(AlarmNotif.this, 0, AlarmIntent, 0);
                alarm.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), alarmIntent);
                finish();
                System.exit(0);
               //Log.println("date", date);
               // db.update(date, wake_up, sleep);
            }
        });

    }
}
