package com.goddard.joel.digitalplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Joel Goddard on 05/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTableSubject {
    public static String TABLE_SUBJECT_NAME =       "subjects";
    public static String FIELD_SUBJECT_ID =         "subjectID";
    public static String FIELD_NAME =               "name";
    public static String FIELD_COLOUR =             "colour";//rrrgggbbb

    /**
     * @return command to create the table
     */
    public static String getCreateTable(){
        return "CREATE TABLE " + TABLE_SUBJECT_NAME + " (" +
                FIELD_SUBJECT_ID +      " INTEGER PRIMARY KEY," +
                FIELD_NAME +            " TEXT," +
                FIELD_COLOUR +          " TEXT" +
                ");";
    }

    /**
     * Inserts subject into database
     * @param db database
     * @param name subject name
     * @param color subject color
     * @return id of subject
     */
    public static long insert(Database db, String name, int color){
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, name);
        values.put(FIELD_COLOUR, color);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.insert(TABLE_SUBJECT_NAME, null, values);
        Log.d("DATABASE", String.format("New subject with Index %1$s, name: %2$s, colour: %3$s}", id, name, color));
        return id;
    }

    /**
     * Updates subject into database
     * @param db database
     * @param id id of subject
     * @param name subject name
     * @param color subject color
     * @return id of subject
     */
    public static long update(Database db, long id, String name, int color){
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, name);
        values.put(FIELD_COLOUR, color);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long idr = dbw.update(TABLE_SUBJECT_NAME, values, String.format("%s=%d", FIELD_SUBJECT_ID, id), null);
        Log.d("DATABASE", String.format("Update subject with Index %1$s, name: %2$s, colour: %3$s}", id, name, color));
        return idr;
    }

    /**
     * Deletes subject into database
     * @param db database
     * @param id id of subject
     */
    public static void delete(Database db, long id){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_NAME, String.format("%s=%d", FIELD_SUBJECT_ID, id), null);
        DatabaseTableSubjectTeacher.deleteBySubject(db, id);
        DatabaseTableSubjectLocation.deleteBySubject(db, id);
        Log.d("DATABASE", String.format("Delete subject with Index %1$s}", id));
    }

    /**
     * get all subjects in database
     * @param db database
     * @return cursor containing subjects
     */
    public static Cursor getAll(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_SUBJECT_NAME, null, null, null, null, null, null, null);
    }

    /**
     * get subject with id
     * @param db database
     * @param id subject id
     * @return Cursor with subject
     */
    public static Cursor getByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_SUBJECT_NAME, null, String.format("%s=%d", FIELD_SUBJECT_ID, id), null, null, null, null, null);
    }
}
