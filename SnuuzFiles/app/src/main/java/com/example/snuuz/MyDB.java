package com.example.snuuz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyDB extends SQLiteOpenHelper{

    private Context ctx;
    private SQLiteDatabase db;
    private static String DB_NAME = "Sleep_Tracker";
    private static String TABLE_NAME = "sleep_tracker_table";
    private  static int VERSION = 1;

    MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    long insert(String s1, String s2, String s3){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", s1);
        cv.put("time_woke_up", s2);
        cv.put("time_asleep", s3);
        return db.insert(TABLE_NAME, null, cv);
    }

    void getAll(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            StringBuilder sr = new StringBuilder();
            while (cr.moveToNext()) {
                sr.append(cr.getString(1) + "    " + cr.getString(2) + "    " + cr.getString(3) + "\n ");
            }
            Toast.makeText(ctx, sr.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void lastWake(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            StringBuilder sr = new StringBuilder();
            cr.moveToLast();
            sr.append(cr.getString(3));
        }
    }
    public void delete(String s){
        db = getWritableDatabase();
        db.delete(TABLE_NAME, "date = ?", new String[]{s});
    }

    void update(String s1, String s2, String s3){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", s1);
        cv.put("time_woke_up", s2);
        cv.put("time_asleep", s3);
        db.update(TABLE_NAME, cv,  "date = ?", new String[]{s1});
    }

    Cursor view() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null);
//        StringBuilder sr = new StringBuilder();
//        cursor.moveToLast();
//        sr.append(cursor.getString(3));
//        Toast.makeText(ctx, sr.toString(), Toast.LENGTH_LONG).show();
        return cr;
    }

    int size(){
        Cursor cr = view();
        return cr.getCount();
    }

    int getLastSleepHours(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            cr.moveToLast();
            return hoursSlept(cr.getString(2), cr.getString(3));
        }

        return -1;
    }

    String getLastSleepTime(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            cr.moveToLast();
            return timeSlept(cr.getString(2), cr.getString(3));
        }

        return "Datanase Empty";
    }

    private static int hoursSlept(String wake, String sleep){
        int wakeHour  = hoursToInt(wake);
        int sleepHour = hoursToInt(sleep);
        int wakeMin   = minsToInt(wake);
        int sleepMin  = minsToInt(sleep);
        int minDiff   = wakeMin - sleepMin;
        if(minDiff < 0){
            wakeHour -= 1;
        }
        int hourDiff = wakeHour - sleepHour;
        if(hourDiff < 0){
            hourDiff += 24;
        }

        return hourDiff;
    }

    private static int minsSlept(String wake, String sleep){
        int wakeMin = minsToInt(wake);
        int sleepMin = minsToInt(sleep);
        int minDiff = wakeMin - sleepMin;
        if(minDiff < 0){
            minDiff += 60;
        }

        return minDiff;
    }

    private String timeSlept(String wake, String sleep){
        int hours = hoursSlept(wake, sleep);
        int mins = minsSlept(wake, sleep);

        return intsToTime(hours, mins);
    }

    String intsToTime(int hours, int mins){
        String returnString = "";
        if(hours%10 == hours)
            returnString += "0";
        returnString += hours+":";
        if(mins%10 == mins)
            returnString += "0";
        returnString += mins;
        return returnString;
    }

    private static int hoursToInt(String time){
        if(time.length() != 5)
            return -1;
        return Integer.parseInt(time.substring(0, 2));
    }

    private static int minsToInt(String time){
        if(time.length() != 5)
            return -1;
        return Integer.parseInt(time.substring(3, 5));
    }

    String getAvgBedTime(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            int time = 0;
            int count = 0;
            while (cr.moveToNext()) {
                time += 60 * hoursToInt(cr.getString(2));
                time += minsToInt(cr.getString(2));
                count++;
            }
            time = time / count;
            int hours = time / 60;
            int mins = time % 60;
            return intsToTime(hours, mins);
        }

        return "Database Empty";
    }

    String getAvgWakeUpTime(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            int time = 0;
            int count = 0;
            while (cr.moveToNext()) {
                time += 60 * hoursToInt(cr.getString(2));
                time += minsToInt(cr.getString(2));
                count++;
            }
            time = time / count;
            int hours = time / 60;
            int mins = time % 60;
            return intsToTime(hours, mins);
        }

        return "Database Empty";
    }

    String getAvgSleepTime(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            int totalHours = 0;
            int totalMins = 0;
            int count = 0;
            while (cr.moveToNext()) {
                totalHours += hoursSlept(cr.getString(3), cr.getString(2));
                totalMins += minsSlept(cr.getString(3), cr.getString(2));
                count++;
            }
            double avgHours = (double) totalHours / count;
            double avgMins = (double) totalMins / count;

            return avgHours + ":" + avgMins;
        }

        return "Database Empty";
    }

    double getAvgSleepCycles(){
        String sleepTime = getAvgSleepTime();
        int mins = hoursToInt(sleepTime)*60 + minsToInt(sleepTime);
        double sleepCycles = 0;
        if(mins > 90){
            mins -= 90;
            sleepCycles += 1;
            sleepCycles += (double)mins/110;
        }
        else
            sleepCycles += (double)mins/90;

        return sleepCycles;
    }

    String getAvgRem(){
        String sleepTime = getAvgSleepTime();
        int mins = hoursToInt(sleepTime)*60 + minsToInt(sleepTime);
        mins = mins/10;
        return "Your average rem time is: "+ mins/60 + " hours and " + mins%60 + " minutes";
    }

    String getAvgDeepSleep(){
        String sleepTime = getAvgSleepTime();
        int mins = hoursToInt(sleepTime)*60 + minsToInt(sleepTime);
        mins = (mins/10)*4;
        return "Your average deep sleep time is: "+ mins/60 + " hours and " + mins%60 + " minutes";
    }

    double getStdDev(){
        db = getReadableDatabase();
        Cursor cr = view();
        if(cr.getCount() != 0) {
            String avgSleepTime = getAvgSleepTime();
            int avgMins = hoursToInt(avgSleepTime) * 60 + minsToInt(avgSleepTime);
            int diffSquaredTotal = 0;
            int currentMins = 0;
            int count = 0;
            while (cr.moveToNext()) {
                currentMins = hoursSlept(cr.getString(3), cr.getString(2)) * 60
                        + minsSlept(cr.getString(3), cr.getString(2));
                diffSquaredTotal += (currentMins - avgMins) ^ 2;
                count++;
            }
            double stdDev = 0;
            if (count > 1)
                stdDev = diffSquaredTotal / (count - 1);

            return stdDev;
        }

        return -1;
    }
}