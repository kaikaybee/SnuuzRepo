package com.example.snuuz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


public class HistoryActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Sets custom Toolbar to replace built-in actionBar
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
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
            case R.id.action_settings: {
                Intent settingsIntent = new Intent(HistoryActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            }
            case R.id.action_clock: {
                Intent mainIntent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(mainIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
