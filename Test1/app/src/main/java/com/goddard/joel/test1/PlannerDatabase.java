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

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "PlannerDatabase.db";
    public static final String HOMEWORK_TABLE_NAME = "homework";
    public static final String HOMEWORK_COLUMN_ID = "_id";
    public static final String HOMEWORK_COLUMN_GLOBAL_ID = "global_id";
    public static final String HOMEWORK_COLUMN_NAME = "name";
    public static final String HOMEWORK_COLUMN_DESCRIPTION = "description";
    public static final String HOMEWORK_COLUMN_DATE_CREATED = "date_created";
    public static final String HOMEWORK_COLUMN_DATE_SYNCED = "date_synced";
    public static final String HOMEWORK_CREATE_TABLE = "CREATE TABLE " + HOMEWORK_TABLE_NAME + " (" +
            HOMEWORK_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            HOMEWORK_COLUMN_NAME + " TEXT, " +
            HOMEWORK_COLUMN_DESCRIPTION + " TEXT" + ")";
    public static final String HOMEWORK_CREATE_TABLE_2 = "CREATE TABLE " + HOMEWORK_TABLE_NAME + " (" +
            HOMEWORK_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            HOMEWORK_COLUMN_GLOBAL_ID + " INTEGER DEFAULT NULL, " +
            HOMEWORK_COLUMN_NAME + " TEXT, " +
            HOMEWORK_COLUMN_DESCRIPTION + " TEXT DEFAULT NULL, " +
            HOMEWORK_COLUMN_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
            HOMEWORK_COLUMN_DATE_SYNCED + " TIMESTAMP DEFAULT NULL" + ")";

    PlannerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HOMEWORK_CREATE_TABLE_2);
        //insertHomework("Dummy", "This is not real data");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + HOMEWORK_TABLE_NAME);
        onCreate(db);
    }

    public long insertHomework(String name, String description){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOMEWORK_COLUMN_NAME, name);
        contentValues.put(HOMEWORK_COLUMN_DESCRIPTION, description);
        return db.insert(HOMEWORK_TABLE_NAME, null, contentValues);
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

    public Cursor getHomework(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =db.rawQuery("SELECT * FROM " + HOMEWORK_TABLE_NAME + " WHERE " +
                HOMEWORK_COLUMN_ID + "=?", new String[] {Integer.toString(id)});
        return res;
    }

    public Cursor getAllHomeworks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + HOMEWORK_TABLE_NAME,null);
        return res;
    }

    public Cursor getAllHomeworks(String... fields){
        String query = "SELECT ";
        for (String field:fields){
            query += field;
            if (field!=fields[fields.length-1]){
                query += ", ";
            }
            else{
                query += " ";
            }
        }
        query += "FROM " + HOMEWORK_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Integer deleteHomework(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(HOMEWORK_TABLE_NAME,
                HOMEWORK_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }
}
