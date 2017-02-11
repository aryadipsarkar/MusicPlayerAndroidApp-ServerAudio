package com.example.arya.project5_audioserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arya's on 11/23/2016.
 */
public class DatabaseAdapter extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Records.db";
    public static final String TABLE_NAME = "Tracks";
    private static final String COLUMN_1= "OPERATION";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE  "+TABLE_NAME+"("+COLUMN_1+" VARCHAR(50))" ;
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String QUERY_TABLE="SELECT * FROM " +" " + TABLE_NAME;

    //the constructor
    public DatabaseAdapter(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //the over-ridding method to create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // the over-riding method to refresh the database on any changes to the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //method to insert data into the database
    public boolean insertData(String operation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1,operation);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //method to get all the data from the database
    public String[] getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(QUERY_TABLE,null);
        String arr[] = new String[1000];
        int i = 0;
        while (res.moveToNext()){
            arr[i] = res.getString(0);
            i++;
        }
        return arr;
    }
}