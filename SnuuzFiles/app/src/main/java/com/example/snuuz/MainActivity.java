package com.example.snuuz;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;


import android.os.Bundle;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;


import android.widget.Toast;

import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.view.LayoutInflater;

import org.apache.http.params.CoreConnectionPNames;


public class MainActivity extends AppCompatActivity {

            Button buttonStartSetDialog;
            Button buttonCancelAlarm;
            Button popUpHistory;
            TextView textAlarmPrompt;
            TextView message;
            AlarmManager alarm;
            PendingIntent alarmIntent;

            TimePickerDialog timePickerDialog;

            static MyDB db;
            static String date;
            static String wake_up;
            static String sleep;

    String wakey = "08:00";
    String sleepy = "22:00";

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                //Creates database
                db = new MyDB(this, "Sleep_Tracker", null, 1);


        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Sets custom Toolbar to replace built-in actionBar
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        //Brian's code - please comment
        popUpHistory = findViewById(R.id.popupBtn);
        popUpHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //db.delete("12-01-2019");
                db.getAll();
            }
        });


        //dialog for setting alarm
        textAlarmPrompt = findViewById(R.id.alarm_prompt);
        buttonStartSetDialog = findViewById(R.id.startSetDialog);
        buttonStartSetDialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textAlarmPrompt.setText("");
                openTimePickerDialog(false);


            }
        });
        buttonCancelAlarm = findViewById(R.id.cancel);
        buttonCancelAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alarm.cancel(alarmIntent);

            }
        });

        //Dynamically set imperative message
        int hoursSlept = db.getLastSleepHours();
        //Toast.makeText(getBaseContext(), "Sleep time: "+hoursSlept,
                //Toast.LENGTH_SHORT).show();
        message = findViewById(R.id.imperative);
        String messageString = "You slept for " + db.getLastSleepTime()+"\n";
        if(hoursSlept < 8)
            messageString += getString(R.string.sleep_more);
        else if(hoursSlept > 10)
            messageString += getString(R.string.sleep_less);
        else
            messageString += getString(R.string.hello);
        message.setText(messageString);
    }


    //Replaces overflow menu of Toolbar with custom buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_bar, menu);
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
            case R.id.action_settings: {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openTimePickerDialog(boolean is24r){
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();



    }

    //Brian's code - please comment
    //Takes inputted User alarm time and sets alarm

    OnTimeSetListener onTimeSetListener
            = new OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


//            if(calSet.compareTo(calNow) <= 0){
//                //Today Set time passed, count to tomorrow
//                calSet.add(Calendar.DATE, 1);
//            }

            //Sets variables from time picker
            SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");
            SimpleDateFormat date_format = new SimpleDateFormat("MM-dd-yyyy");
            date = date_format.format((new Date()));
            wake_up = calSet.get(calSet.HOUR_OF_DAY) + ":" + calSet.get(calSet.MINUTE);
            sleep = time_format.format(calSet.getInstance().getTime());

            setAlarm(calSet, wake_up);


            setAlarm(calSet);
        }};
    private void setAlarm(Calendar targetCal) {
    //sets alarm by sending alarm data to a receiver with a pending intent.
    //records time when the alarm was set by the user(not the time the alarm will go off)

        textAlarmPrompt.setText(targetCal.getTime().toString());
        Intent AlarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);


        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, AlarmIntent, 0);
        alarm.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), alarmIntent);

        try {

            FileOutputStream fileout = openFileOutput("SleepData.txt", MODE_APPEND);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);

           outputWriter.write("SleepTime: "+Calendar.getInstance().getTime().toString()+"\n");


            outputWriter.close();

            //display file saved message for testing
            Toast.makeText(getBaseContext(), "Sleep time saved successfully!"+""+db.getLastSleepTime(),
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Inserts information into database
    private void setAlarm(Calendar targetCal, String wake_up) {

        db.insert(date, wake_up, sleep);
        //db.delete("11-28-2019");
        //db.getAll();
        textAlarmPrompt.setText(targetCal.getTime().toString());
        try {


            FileOutputStream fileout=openFileOutput("SleepData.txt", MODE_APPEND);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);

            outputWriter.write("SleepTime: "+wake_up+"\n");


            outputWriter.close();

            //display file saved message
            //Toast.makeText(getBaseContext(), "File saved successfully!",
                    //Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getDate(){
        return date;
    }

    public static String getSleep(){
        return sleep;
    }

    //Inserts item into database
    public void insert(View view) {
        String s1 = date;
        String s2 = wake_up;
        String s3 = sleep;
        db.insert(s1, s2, s3);
    }

    //Deletes item from database
    public void delete(View view) {
        String s = date;
        db.delete(s);
    }

    //Get all entries in database
    public void view(View view) {
        db.getAll();
    }

    // Update database
    public void update(View view) {
        String s1 = date;
        String s2 = wake_up;
        String s3 = sleep;
        db.update(s1, s2, s3);
    }

}
