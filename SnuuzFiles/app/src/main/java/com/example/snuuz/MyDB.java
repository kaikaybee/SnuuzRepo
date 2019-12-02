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

    public int getLastSleepTime(){
        db = getReadableDatabase();
        Cursor cr = db.rawQuery("select * from " + TABLE_NAME + ";", null );
        cr.moveToLast();
       return TimeParser(cr.getString(3), cr.getString(2));

    }

    static int TimeParser(String wake, String Sleep){


        String wakeHourtemp = wake.substring(0, 2);
        int wakeHourtemp2= Integer.parseInt(wakeHourtemp);

        String SleepHourtemp = Sleep.substring(0, 2);
        int SleepHourtemp2 = Integer.parseInt(SleepHourtemp);

        String wakeMintemp = wake.substring(3, 5);
        int wakeMintemp2 = Integer.parseInt(wakeMintemp);

        String SleepMintemp = Sleep.substring(3, 5);
        int SleepMintemp2 = Integer.parseInt(SleepMintemp);

        int minDiff = wakeMintemp2 - SleepMintemp2;
        if(minDiff < 0){
            wakeHourtemp2 -=1;
        }
        int hourDiff = wakeHourtemp2 - SleepHourtemp2;
        if(hourDiff < 0){
            hourDiff += 24;
        }

        return hourDiff;
    }
}