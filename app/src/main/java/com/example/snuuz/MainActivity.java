package com.example.snuuz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.*;
import java.util.Calendar;
import android.util.*;
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

public class MainActivity extends AppCompatActivity {


    TimePicker myTimePicker;
    Button buttonstartSetDialog;
    Button buttonCancelAlarm;
    TextView textAlarmPrompt;



    TimePickerDialog timePickerDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textAlarmPrompt = (TextView)findViewById(R.id.alarmprompt);

        buttonstartSetDialog = (Button)findViewById(R.id.startSetDialog);
        buttonstartSetDialog.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textAlarmPrompt.setText("");
                openTimePickerDialog(false);


            }});

        buttonCancelAlarm = (Button)findViewById(R.id.cancel);
        buttonCancelAlarm.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //cancelAlarm();
            }});




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


        textAlarmPrompt.setText(targetCal.getTime().toString());
        try {

            FileOutputStream fileout=openFileOutput("SleepData.txt", MODE_APPEND);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);

           outputWriter.write("SleepTime: "+Calendar.getInstance().getTime().toString()+"\n");


            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
