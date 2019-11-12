package com.example.snuuz;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class AlarmNotif extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //new activity opened at the time the alarm was set. Bypasses screen locks.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notif);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }
}
