package com.goddard.joel.digitalplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Joel Goddard on 05/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTableBlock {
    public static String TABLE_BLOCK_NAME =     "blocks";
    public static String FIELD_BLOCK_ID =       "blockID";
    public static String FIELD_DAY =            "day";
    public static String FIELD_START_TIME =     "startTime";    //Time in minutes
    public static String FIELD_LENGTH =         "length";       //Length in minutes
    public static String FIELD_SUBJECT_ID =     "subjectID";
    public static String FIELD_TEACHER_ID =     "teacherID";
    public static String FIELD_LOCATION_ID =    "locationID";

    public static String getCreateTable(){
        /**
         * gets the create table command for this table
         * */
        return "CREATE TABLE " + TABLE_BLOCK_NAME + " (" +
                FIELD_BLOCK_ID +    " INTEGER PRIMARY KEY," +
                FIELD_DAY +         " INTEGER," +
                FIELD_START_TIME +  " INTEGER," +
                FIELD_LENGTH +      " INTEGER," +
                FIELD_SUBJECT_ID +  " INTEGER," +
                FIELD_TEACHER_ID +  " INTEGER," +
                FIELD_LOCATION_ID + " INTEGER" +
                ");";
    }

    /**
     * Inserts a block into database
     * @param db database
     * @param day int representing day from {@link Calendar}
     * @param startTime start time in minutes since 00:00
     * @param length length in minutes
     * @param subjectID id of subject
     * @param teacherID id of teacher
     * @param locationID id of location
     * @return id of block
     */
    public static long insert(Database db, int day, int startTime, int length, long subjectID, long teacherID, long locationID){
        ContentValues values = new ContentValues();
        values.put(FIELD_DAY, day);
        values.put(FIELD_START_TIME, startTime);
        values.put(FIELD_LENGTH, length);
        values.put(FIELD_SUBJECT_ID, subjectID);
        values.put(FIELD_TEACHER_ID, teacherID);
        values.put(FIELD_LOCATION_ID, locationID);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.insert(TABLE_BLOCK_NAME, null, values);
        Log.d("DATABASE", String.format("New block with Index %1$s, Day: %2$s, startTime %3$s, length %4$s, subjectID %5$s, teacher %6$s, location %7$s",
                id, day, startTime, length, subjectID, teacherID, locationID));
        return id;
    }

    /**
     * Updates a block into database
     * @param db database
     * @param id block id
     * @param day int representing day
     * @param startTime start time in minutes since 00:00
     * @param length length in minutes
     * @param subjectID id of subject
     * @param teacherID id of teacher
     * @param locationID id of location
     * @return id of block
     */
    public static long update(Database db, long id, int day, int startTime, int length, long subjectID, long teacherID, long locationID){
        ContentValues values = new ContentValues();
        values.put(FIELD_DAY, day);
        values.put(FIELD_START_TIME, startTime);
        values.put(FIELD_LENGTH, length);
        values.put(FIELD_SUBJECT_ID, subjectID);
        values.put(FIELD_TEACHER_ID, teacherID);
        values.put(FIELD_LOCATION_ID, locationID);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long idr = dbw.update(TABLE_BLOCK_NAME, values, String.format("%s=%d", FIELD_BLOCK_ID, id), null);
        Log.d("DATABASE", String.format("Update block with Index %1$s, Day: %2$s, startTime %3$s, length %4$s}", id, day, startTime, length));
        return idr;
    }

    /**
     * Deletes a block into database
     * @param db database
     * @param id block id
     */
    public static void delete(Database db, long id){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_BLOCK_NAME, String.format("%s=%d", FIELD_BLOCK_ID, id), null);
        Log.d("DATABASE", String.format("Delete block with Index %1$s}", id));
    }

    /**
     * get all blocks
     * @param db database
     * @return cursor containing all blocks
     */
    public static Cursor getAll(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_BLOCK_NAME, null, null, null, null, null, FIELD_START_TIME, null);
    }

    /**
     * Get block with id
     * @param db database
     * @param id block id
     * @return cursor containing requested block
     */
    public static Cursor getByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_BLOCK_NAME, null, String.format("%s=%d", FIELD_BLOCK_ID, id), null, null, null, FIELD_START_TIME, null);
    }

    public static Cursor getByDay(Database db, int day) {
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_BLOCK_NAME, null, String.format("%s=%d", FIELD_DAY, day), null, null, null, FIELD_START_TIME, null);
    }
}
