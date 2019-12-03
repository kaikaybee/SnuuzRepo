package com.example.snuuz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    Button buttonStartSetDialog;
    Button buttonCancelAlarm;
    Button popUpHistory;
    TextView textAlarmPrompt;
    AlarmManager alarm;
    PendingIntent alarmIntent;

    TimePickerDialog timePickerDialog;

    @SuppressLint("StaticFieldLeak")
    static MyDB db;
    static String date;
    static String wake_up;
    static String sleep;
    static boolean alarmIsSet = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creates database
        db = new MyDB(this, "Sleep_Tracker", null, 1);
        Cursor cr = db.view();
        setContentView(R.layout.activity_main);

        // UI action bar and status bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Brian's code - please comment
        popUpHistory = findViewById(R.id.popupBtn);
        popUpHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getAll();
            }
        });

        //dialog for setting alarm
        textAlarmPrompt = findViewById(R.id.alarm_prompt);
        if(cr.getCount() != 0) {
            cr.moveToLast();
            String s = cr.getString(2);
            textAlarmPrompt.setText(s);
        }

        buttonStartSetDialog = findViewById(R.id.startSetDialog);
        buttonStartSetDialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textAlarmPrompt.setText("");
                openTimePickerDialog();

            }
        });
        buttonCancelAlarm = findViewById(R.id.cancel);
        buttonCancelAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alarm.cancel(alarmIntent);
            }
        });

        //Dynamically display imperative
        int hoursSlept = MainActivity.db.getLastSleepHours();
        TextView message = findViewById(R.id.imperative);
        String messageString = "";
        if(db.size() == 0){
            messageString += getString(R.string.welcome);
            message.setText(messageString);
            TextView alarmTimeWelcome = findViewById(R.id.alarm_prompt);
            String alarmTimeWelcomeString = "None";
            alarmTimeWelcome.setText(alarmTimeWelcomeString);
        }
        else {
            String hours = db.getLastSleepTime().substring(0, 2);
            if (hours.charAt(0) == '0')
                hours = hours.substring(1);
            String mins = db.getLastSleepTime().substring(3, 5);
            if (mins.charAt(0) == '0')
                mins = mins.substring(1);

            messageString = "You slept for " + hours + " hours and " + mins + " mins" + "\n";

            if (hoursSlept < 8)
                messageString += getString(R.string.sleep_more);
            else if (hoursSlept > 10)
                messageString += getString(R.string.sleep_less);
            else
                messageString += getString(R.string.hello);
            message.setText(messageString);
        }
    }

    //Replaces overflow menu of Toolbar with custom buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_bar, menu);
        String stat = db.getAvgBedTime();
        return true;
    }

    //Upon a Toolbar button press, loads whichever activity it should
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history: {
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openTimePickerDialog(){
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    //Takes inputted User alarm time and sets alarm
    OnTimeSetListener onTimeSetListener = new OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

           if(calSet.compareTo(calNow) <= 0) {
               //Today Set time passed, count to tomorrow
               calSet.add(Calendar.DATE, 1);
           }
            //Sets variables from time picker
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date_format = new SimpleDateFormat("MM-dd-yyyy");
            date = date_format.format((new Date()));

            //wake_up = calSet.get(calSet.HOUR_OF_DAY) + ":" + calSet.get(calSet.MINUTE);
            String zero = "0";
            String hour = "";
            String min = "";
            if(calSet.get(calSet.HOUR_OF_DAY) < 10){
                hour = zero+calSet.get(calSet.HOUR_OF_DAY);
            }
            else{
                hour = calSet.get(calSet.HOUR_OF_DAY)+"";
            }
            if(calSet.get(calSet.MINUTE) < 10){
                min = zero+calSet.get(calSet.MINUTE);
            }
            else{
                min = calSet.get(calSet.MINUTE)+"";
            }
            wake_up = hour+":"+min;
            String Shour = "";
            String Smin = "";
            if(calNow.get(Calendar.HOUR_OF_DAY) < 10){
                Shour = zero+calNow.get(Calendar.HOUR_OF_DAY);
            }
            else{
                Shour = calNow.get(Calendar.HOUR_OF_DAY)+"";
            }
            if(calNow.get(Calendar.MINUTE) < 10){
                Smin = zero+calNow.get(Calendar.MINUTE);
            }
            else{
                Smin = calNow.get(Calendar.MINUTE)+"";
            }
            sleep = Shour+":"+Smin;

            setAlarm(calSet);

        }};


    private void setAlarm(Calendar targetCal) {
    //sets alarm by sending alarm data to a receiver with a pending intent.
    //records time when the alarm was set by the user(not the time the alarm will go off)
    //inserts alarm time into database
        Intent AlarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, AlarmIntent, 0);
        alarm.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), alarmIntent);
        alarmIsSet = true;

        db.insert(date, wake_up, sleep);
        Cursor cr = db.view();
        cr.moveToLast();
        textAlarmPrompt.setText(wake_up);
    }

    public static String getDate(){
        return date;
    }

    public static String getSleep(){
        return sleep;
    }
}
