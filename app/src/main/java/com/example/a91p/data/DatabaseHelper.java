package com.example.a91p.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //define sql command with all advert elements
        String CREATE_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "( " + Util.NAME + " TEXT PRIMARY KEY, "
                + Util.POST_TYPE + " TEXT," + Util.PHONE + " TEXT," + Util.DESCRIPTION + " TEXT," + Util.DATE + " TEXT," + Util.LOCATION + " TEXT )";

        //execute sql
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //define sql drop command
        String DROP_ADVERT_TABLE = "DROP TABLE IF EXISTS";

        //execute sql
        db.execSQL(DROP_ADVERT_TABLE, new String[]{Util.TABLE_NAME});

        //call to recreate database
        onCreate(db);
    }

    public long insertAdvert(Advert advert) {
        //obtain database and define values
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        //set each element into values
        contentValues.put(Util.NAME, advert.getName());
        contentValues.put(Util.POST_TYPE, advert.getPostType());
        contentValues.put(Util.PHONE, advert.getPhone());
        contentValues.put(Util.DESCRIPTION, advert.getDescription());
        contentValues.put(Util.DATE, advert.getDate());
        contentValues.put(Util.LOCATION, advert.getLocation());

        //insert values into table row
        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();

        //return table row
        return newRowId;
    }

    //check for duplicate values
    public boolean advertDupCheck(String name, String phone, String description, String date, String location) {
        //obtain database
        SQLiteDatabase db = this.getReadableDatabase();

        //sql where condition
        String DUPLICATE_CHECK = Util.NAME + " = '" + name + "' AND " + Util.PHONE + " = '" + phone + "' AND " + Util.DESCRIPTION + " = '" + description + "' AND " + Util.DATE + " = '" + date + "' AND " + Util.LOCATION + " = '" + location + "'";

        //search for values
        Cursor cursor = db.query(Util.TABLE_NAME, null, DUPLICATE_CHECK, null, null, null, null);
        int numberOfRows = cursor.getCount();
        db.close();

        //return result
        if (numberOfRows == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    //give access to database
    public List<Advert> getData() {
        //obtain database
        SQLiteDatabase db = this.getReadableDatabase();

        List<Advert> advertList = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String postType = cursor.getString(1);
                String phone = cursor.getString(2);
                String description = cursor.getString(3);
                String date = cursor.getString(4);
                String location = cursor.getString(5);

                advertList.add(new Advert(name, postType, phone, description, date, location));
            }
            while (cursor.moveToNext());
        }

        return advertList;
    }

    public void delete(String name) {
        //obtain database
        SQLiteDatabase db = this.getWritableDatabase();

        //search for id and delete
        db.delete(Util.TABLE_NAME, Util.NAME + "= '" + name + "'", null);

        db.close();
    }
}
