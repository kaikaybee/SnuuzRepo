package com.example.snuuz;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;


import java.io.*;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.Toast;
import android.widget.PopupWindow;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;

public class MainActivity extends AppCompatActivity {

    Button buttonStartSetDialog;

    Button buttonCancelAlarm;
    TextView textAlarmPrompt;
    Button PopupBtn;

    AlarmManager alarm;
    PendingIntent alarmIntent;

    TimePickerDialog timePickerDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Sets custom Toolbar to replace built-in actionBar
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        //Brian's code - please comment
        //Pop up window testing
        PopupBtn = findViewById((R.id.popupBtn));
        PopupBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_up, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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
                //cancelAlarm();
                alarm.cancel(alarmIntent);
            }
        });
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

    //Brian's code - please comment
    //Uses default time picker dialog to let user set alarm.
    //Uses Calender to get current time and to set a new calender instance.
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

            if(calSet.compareTo(calNow) <= 0){
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

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
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
