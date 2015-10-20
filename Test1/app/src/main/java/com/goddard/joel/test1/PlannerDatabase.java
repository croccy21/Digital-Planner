package com.goddard.joel.test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joel Goddard on 20/10/2015.
 *
 * @author Joel Goddard
 */
public class PlannerDatabase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlannerDatabase.db";
    private static final String HOMEWORK_TABLE_NAME = "homework";
    private static final String HOMEWORK_COLUMN_ID = "_id";
    private static final String HOMEWORK_COLUMN_NAME = "name";
    private static final String HOMEWORK_COLUMN_DESCRIPTION = "description";
    private static final String HOMEWORK_CREATE_TABLE = "CREATE TABLE " + HOMEWORK_TABLE_NAME + " (" +
            HOMEWORK_COLUMN_ID + "INTEGER PRIMARY KEY, " +
            HOMEWORK_COLUMN_NAME + "TEXT, " +
            HOMEWORK_COLUMN_DESCRIPTION + "TEXT" + ")";

    PlannerDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HOMEWORK_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " +HOMEWORK_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertHomework(String name, String description){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOMEWORK_COLUMN_NAME, name);
        contentValues.put(HOMEWORK_COLUMN_DESCRIPTION, description);
        db.insert(HOMEWORK_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateHomework(int id, String name, String description){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOMEWORK_COLUMN_NAME, name);
        contentValues.put(HOMEWORK_COLUMN_DESCRIPTION,description);
        db.update(HOMEWORK_TABLE_NAME, contentValues, HOMEWORK_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getHomeworke(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =db.rawQuery("SELECT * FROM " + HOMEWORK_TABLE_NAME + " WHERE " +
                HOMEWORK_COLUMN_ID + "=?", new String[] {Integer.toString(id)});
        return res;
    }

    //public Cursor getAllPersons(){
//
   // }
}
