package com.example.nearbylocaton.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Data.db";
    public static int VERSION = 1;
    public static String TABLE_NAME = "Data";
    public static String COL_ID = "Id";
    public static String COL_NAME = "PlaceName";
    public static String COL_LAT = "Lat";
    public static String COL_LNG = "Lng";           // DataType is int..?
    public static String COL_RATING = "Rating";  // DataType is long..?
    public static String COL_OPENTIME = "Open_Time";  // DataType is long..?
    public static String COL_ICON = "Icon";           // DataType is int..?

    private  SQLiteDatabase database;

    public static  String create_table = "create table "+TABLE_NAME+
            " (Id integer primary key, PlaceName text, Lat double, Lng double, Rating text, Open_Time text, Icon integer)";
    public DatabaseOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void openDatabase(){
        database = getWritableDatabase();
    }

    public void closeDatabase(){
        database.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getData(){
        openDatabase();
        String data = "Select * From "+TABLE_NAME;
        Cursor cursor = database.rawQuery(data,null);

        return cursor;

    }
    public void deleteData(int id){
        getWritableDatabase().delete(TABLE_NAME,"Id=?",new String[]{String.valueOf(id)});
    }

    public long addPlace(String placrName, double latitute,double longitude,String rating,String time,String icon ){

        openDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME,placrName);
        values.put(COL_LAT,latitute);
        values.put(COL_LNG,longitude);
        values.put(COL_RATING,rating);
        values.put(COL_OPENTIME,time);
        values.put(COL_ICON,icon);
        long id = database.insert(TABLE_NAME,null,values);
        closeDatabase();
        return id;
    }
}