package com.goddard.joel.digitalplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.Date;

/**
 * Created by Joel Goddard on 14/12/2015.
 *
 * @author Joel Goddard
 */
public class Database extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "plannerDB";
    public static int DATABASE_VERSION = 1;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created and is used to create the tables
     * This is required for the database handler
     * @param db reference to the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseTableBlock.getCreateTable());
        db.execSQL(DatabaseTableSubject.getCreateTable());
        db.execSQL(DatabaseTableLocation.getCreateTable());
        db.execSQL(DatabaseTableTeacher.getCreateTable());
        db.execSQL(DatabaseTableSubjectLocation.getCreateTable());
        db.execSQL(DatabaseTableSubjectTeacher.getCreateTable());
        db.execSQL(DatabaseTableLesson.getCreateTable());
        db.execSQL(DatabaseTableHomework.getCreateTable());
    }


    /**
     * This is only used if I need to change the database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static void wipeDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
