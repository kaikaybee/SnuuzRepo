package com.example.snuuz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyDB extends SQLiteOpenHelper{

    Context ctx;
    SQLiteDatabase db;
    private static String DB_NAME = "Sleep_Tracker";
    private static String TABLE_NAME = "sleep_tracker_table";
    private  static int VERSION = 1;

    public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VERSION);
        ctx = context;
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(id integer PRIMARY KEY, " +
                "date String, time_woke_up String, time_asleep String);");
        Toast.makeText(ctx, "DB is created", Toast.LENGTH_LONG);
        Log.d("tag", "was db created?");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        VERSION++;
        onCreate(db);
    }

    public long insert(String s1, String s2, String s3){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", s1);
        cv.put("time_woke_up", s2);
        cv.put("time_asleep", s3);
        return db.insert(TABLE_NAME, null, cv);
    }

    public void getAll(){
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null );
        StringBuilder sr = new StringBuilder();
        while(cr.moveToNext()){
            sr.append(cr.getString(1) + "    " + cr.getString(2) + "    " + cr.getString(3) + "\n ");
        }
        Toast.makeText(ctx, sr.toString(), Toast.LENGTH_LONG).show();
    }
    public void delete(String s){
        db = getWritableDatabase();
        db.delete(TABLE_NAME, "date = ?", new String[]{s});
    }

    public void update(String s1, String s2, String s3){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", s1);
        cv.put("time_woke_up", s2);
        cv.put("time_asleep", s3);
        db.update(TABLE_NAME, cv,  "date = ?", new String[]{s1});
    }

    public int getLastSleepHours(){
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null );
        cr.moveToLast();
       return hoursSlept(cr.getString(3), cr.getString(2));

    }

    public String getLastSleepTime(){
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null );
        cr.moveToLast();
        return timeSlept(cr.getString(3), cr.getString(2));
    }

    static int hoursSlept(String wake, String Sleep){
        String wakeHourString = wake.substring(0, 2);
        int wakeHour= Integer.parseInt(wakeHourString);

        String SleepHourString = Sleep.substring(0, 2);
        int SleepHour = Integer.parseInt(SleepHourString);

        String wakeMinString = wake.substring(3, 5);
        int wakeMin = Integer.parseInt(wakeMinString);

        String SleepMinString = Sleep.substring(3, 5);
        int SleepMin = Integer.parseInt(SleepMinString);

        int minDiff = wakeMin - SleepMin;
        if(minDiff < 0){
            wakeHour -=1;
        }
        int hourDiff = wakeHour - SleepHour;
        if(hourDiff < 0){
            hourDiff += 24;
        }

        return hourDiff;
    }

    static int minsSlept(String wake, String sleep){
        String wakeMinString = wake.substring(3, 5);
        int wakeMin = Integer.parseInt(wakeMinString);

        String sleepMinString = sleep.substring(3, 5);
        int sleepMin = Integer.parseInt(sleepMinString);

        int minDiff = wakeMin - sleepMin;
        if(minDiff < 0){
            minDiff += 60;
        }

        return minDiff;
    }

    static String timeSlept(String wake, String sleep){
        int hours = hoursSlept(wake, sleep);
        int mins = minsSlept(wake, sleep);
        return hours+":"+mins;
    }
}