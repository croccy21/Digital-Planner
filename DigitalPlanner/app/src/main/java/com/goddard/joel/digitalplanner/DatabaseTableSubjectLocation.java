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
public class DatabaseTableSubjectLocation {
    public static String TABLE_SUBJECT_LOCATION_NAME =  "subjectLocation";
    public static String FIELD_LOCATION_ID =            "locationID";
    public static String FIELD_SUBJECT_ID =             "subjectID";

    /**
     * @return command to create the table
     */
    public static String getCreateTable(){
        return "CREATE TABLE " + TABLE_SUBJECT_LOCATION_NAME + " (" +
                FIELD_LOCATION_ID +         " INTEGER," +
                FIELD_SUBJECT_ID +          " INTEGER," +
                "PRIMARY KEY (" + FIELD_LOCATION_ID +
                ", " + FIELD_SUBJECT_ID + ")" +
                ");";
    }

    /**
     * Joins location to subject
     * @param db database
     * @param subjectID id of subject
     * @param locationID id of location
     */
    public static void insert(Database db, long subjectID, long locationID){
        ContentValues values = new ContentValues();
        values.put(FIELD_SUBJECT_ID, subjectID);
        values.put(FIELD_LOCATION_ID, locationID);

        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.insert(TABLE_SUBJECT_LOCATION_NAME, null, values);
        Log.d("DATABASE", String.format("New subject location with subjectID: %1$s, locationID:%2$s}", subjectID, locationID));
    }

    /**
     * Delete a single entry
     * @param db database
     * @param subjectID id of subject
     * @param locationID id of location
     */
    public static void delete(Database db, long subjectID, long locationID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_LOCATION_NAME,
                String.format("%s=%d AND %s=%d", FIELD_SUBJECT_ID, subjectID, FIELD_LOCATION_ID, locationID),
                null);
    }

    /**
     * Deletes all locations connected to a subject
     * @param db database
     * @param subjectID subject id
     */
    public static void deleteBySubject(Database db, long subjectID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_LOCATION_NAME,
                String.format("%s=%d", FIELD_SUBJECT_ID, subjectID),
                null);
    }

    /**
     * Deletes all subjects connected to a location
     * @param db database
     * @param locationID location id
     */
    public static void deleteByLocation(Database db, long locationID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_LOCATION_NAME,
                String.format("%s=%d", FIELD_LOCATION_ID, locationID),
                null);
    }

    /**
     * Get all locations by subject
     * @param db database
     * @param subjectID id of subject
     * @return Cursor containing all locations connected to subject id
     */
    public static Cursor getAllBySubjectID(Database db, long subjectID){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_SUBJECT_LOCATION_NAME, null, String.format("%s=%d", FIELD_SUBJECT_ID, subjectID), null, null, null, null, null);
    }

    /**
     * get all subjects by location
     * @param db database
     * @param locationID id of location
     * @return curor containing all subjects connected to location id
     */
    public static Cursor getAllByLocationID(Database db, long locationID){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_SUBJECT_LOCATION_NAME, null, String.format("%s=%d", FIELD_LOCATION_ID, locationID), null, null, null, null, null);
    }
}
