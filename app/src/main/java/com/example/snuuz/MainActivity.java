package com.example.snuuz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import java.io.*;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonStartSetDialog;

    Button buttonCancelAlarm;
    TextView textAlarmPrompt;



    TimePickerDialog timePickerDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets custom Toolbar to replace built-in actionBar
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        //Brian's code - please comment
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

    public void launchDatabaseActivity(View view) {
        Intent intent = new Intent(this, Database.class);
        startActivity(intent);
    }

}
