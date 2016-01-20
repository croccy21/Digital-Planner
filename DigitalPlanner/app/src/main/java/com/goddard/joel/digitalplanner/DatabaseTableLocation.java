package com.goddard.joel.digitalplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;

/**
 * Created by Joel Goddard on 05/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTableLocation {
    public static String TABLE_LOCATION_NAME =          "locations";
    public static String FIELD_LOCATION_ID =            "locationID";
    public static String FIELD_NAME =                   "name";

    /**
     * @return command to create the table
     */
    public static String getCreateTable(){
        return "CREATE TABLE " + TABLE_LOCATION_NAME + " (" +
                FIELD_LOCATION_ID +         " INTEGER PRIMARY KEY," +
                FIELD_NAME +                " TEXT" +
                ");";
    }

    /**
     * Insert location into database
     * @param db database
     * @param name name of location
     * @return id of location
     */
    public static long insert(Database db, String name){
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, name);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.insert(TABLE_LOCATION_NAME, null, values);
        Log.d("DATABASE", String.format("New location with Index %1$s, name: %2$s}", id, name));
        return id;
    }

    /**
     * Updated location in database
     * @param db database
     * @param id location id
     * @param name name of teacher
     * @return id of location
     */
    public static long update(Database db, long id, String name){
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, name);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long idR = dbw.update(TABLE_LOCATION_NAME, values, String.format("%s=%d", FIELD_LOCATION_ID, id), null);
        Log.d("DATABASE", String.format("Updated location with Index %1$s, name: %2$s}", id, name));
        return idR;
    }

    /**
     * Deletes location by id from the database
     * @param db database
     * @param id id of location
     */
    public static void delete(Database db, long id){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_LOCATION_NAME, String.format("%s=%d", FIELD_LOCATION_ID, id), null);
        DatabaseTableSubjectLocation.deleteByLocation(db, id);
        Log.d("DATABASE", String.format("Deleted location with Index %1$s}", id));
    }

    /**
     * Returns all locations in the database
     * @param db database
     * @return cursor containing locations
     */
    public static Cursor getAll(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_LOCATION_NAME, null, null, null, null, null, null, null);
    }

    /**
     * Get location by id
     * @param db database
     * @param id id of location
     * @return Cursor containing location
     */
    public static Cursor getByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_LOCATION_NAME, null, String.format("%s=%d", FIELD_LOCATION_ID, id), null, null, null, null, null);
    }
}
