package com.example.snuuz;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import java.util.Calendar;
import android.net.Uri;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.widget.TextView;

public class AlarmNotif extends AppCompatActivity {
    Button Dismiss;
    EditText WakeUpTIme;
    Button Snooze;

    AlarmManager alarm;
    PendingIntent alarmIntent;


    //MyDB db;
    String date;
    String wake_up;
    String sleep;
    String Wake_upInput;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //new activity opened at the time the alarm was set. Bypasses screen locks.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notif);
        //Sets custom Toolbar to replace built-in actionBar
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        date = MainActivity.getDate();
        sleep = MainActivity.getSleep();
        WakeUpTIme = findViewById(R.id.editTime);


        Dismiss = findViewById((R.id.wake_up_timeDialog));
        Dismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Wake_upInput = WakeUpTIme.getText().toString();
                //Toast.makeText(getBaseContext(), bb,
                        //Toast.LENGTH_SHORT).show();
                //MainActivity.db.update(date, "hi", "hi");
                //MainActivity.db.getAll();
                wake_up = Wake_upInput;
                MainActivity.db.update(date, wake_up, sleep);
                finish();
                System.exit(0);
            }
        });
        Snooze = findViewById(R.id.SnoozeBtn);
        Snooze.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();
                calSet.set(Calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR_OF_DAY));
                calSet.set(Calendar.MINUTE, calNow.get(Calendar.MINUTE)+1);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);
                Intent AlarmIntent = new Intent(AlarmNotif.this, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(AlarmNotif.this, 0, AlarmIntent, 0);
                alarm.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), alarmIntent);
                wake_up = calSet.get(Calendar.HOUR_OF_DAY) + ":" + calSet.get(Calendar.MINUTE);
                MainActivity.db.update(date, wake_up, sleep);
                finish();
                System.exit(0);
               //Log.println("date", date);
               // db.update(date, wake_up, sleep);
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
            case R.id.action_clock: {
                int hoursSlept = MainActivity.db.getLastSleepHours();
                TextView message = findViewById(R.id.imperative);
                String messageString = "You slept for " + MainActivity.db.getLastSleepTime()+"\n";
                if(hoursSlept < 8)
                    messageString += getString(R.string.sleep_more);
                else if(hoursSlept > 10)
                    messageString += getString(R.string.sleep_less);
                else
                    messageString += getString(R.string.hello);
                message.setText(messageString);
                Intent mainIntent = new Intent(AlarmNotif.this, MainActivity.class);
                startActivity(mainIntent);
                //Dynamically set imperative message

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
