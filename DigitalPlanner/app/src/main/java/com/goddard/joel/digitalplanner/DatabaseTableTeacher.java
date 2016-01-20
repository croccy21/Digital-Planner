package com.goddard.joel.digitalplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Joel Goddard on 05/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTableTeacher {
    public static String TABLE_TEACHER_NAME =       "teachers";
    public static String FIELD_TEACHER_ID =         "teacherID";
    public static String FIELD_NAME =               "name";

    /**
     * @return command to create the table
     */
    public static String getCreateTable(){
        return "CREATE TABLE " + TABLE_TEACHER_NAME + " (" +
                FIELD_TEACHER_ID +      " INTEGER PRIMARY KEY," +
                FIELD_NAME +            " TEXT" +
                ");";
    }

    /**
     * Inserts a teacher into the database
     * @param db database
     * @param name name of teacher
     * @return id of entry
     */
    public static long insert(Database db, String name){
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, name);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.insert(TABLE_TEACHER_NAME, null, values);
        Log.d("DATABASE", String.format("New teacher with Index %1$s, name: %2$s}", id, name));
        return id;
    }

    /**
     * Updated teacher in database
     * @param db database
     * @param id teacher id
     * @param name name of teacher
     * @return id of teacher
     */
    public static long update(Database db, long id, String name){
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, name);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long idR = dbw.update(TABLE_TEACHER_NAME, values, String.format("%s=%d", FIELD_TEACHER_ID, id), null);
        Log.d("DATABASE", String.format("Updated teacher with Index %1$s, name: %2$s}", id, name));
        return idR;
    }

    /**
     * Deletes teacher by id from the database
     * @param db database
     * @param id id of teacher
     */
    public static void delete(Database db, long id){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_TEACHER_NAME, String.format("%s=%d", FIELD_TEACHER_ID, id), null);
        DatabaseTableSubjectTeacher.deleteByTeacher(db, id);
        Log.d("DATABASE", String.format("Deleted teacher with Index %1$s}", id));
    }

    /**
     * Gets all of the teachers in the database
     * @param db database
     * @return Cursor containing data
     */
    public static Cursor getAll(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_TEACHER_NAME, null, null, null, null, null, null, null);
    }

    /**
     * Get the teacher with the specified ID from the database
     * @param db database
     * @param id id of teacher
     * @return Cursor containing teacher
     */
    public static Cursor getByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_TEACHER_NAME, null, String.format("%s=%d", FIELD_TEACHER_ID, id), null, null, null, null, null);
    }
}
