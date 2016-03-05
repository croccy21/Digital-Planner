package com.goddard.joel.digitalplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Joel Goddard on 05/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTableLesson {
    public static String TABLE_LESSON_NAME =       "lessons";
    public static String FIELD_LESSON_ID =         "lessonID";
    public static String FIELD_BLOCK_ID =          "blockID";
    public static String FIELD_DAY =               "day";
    public static String FIELD_CANCELED =          "canceled";//1 for canceled or 0 for not canceled

    /**
     * @return command to create the table
     */
    public static String getCreateTable(){
        return "CREATE TABLE " + TABLE_LESSON_NAME + " (" +
                FIELD_LESSON_ID +      " INTEGER PRIMARY KEY," +
                FIELD_BLOCK_ID +      " INTEGER," +
                FIELD_DAY +            " TEXT," +
                FIELD_CANCELED +       " TEXT" +
                ");";
    }

    /**
     * Insets lesson into database
     * @param db database
     * @param blockID id of block lesson is in
     * @param date date of lesson
     * @param canceled is it canceled
     * @return id of lesson
     */
    public static long insert(Database db, long blockID, Calendar date, boolean canceled){
        ContentValues values = new ContentValues();
        date = Util.setDateToStart(date);
        values.put(FIELD_BLOCK_ID, blockID);
        values.put(FIELD_DAY, date.getTimeInMillis());
        values.put(FIELD_CANCELED, canceled);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.insert(TABLE_LESSON_NAME, null, values);
        Log.d("DATABASE", String.format("New lesson with Index %1$s, Block ID: %2$s, day %3$s, canceled %4$s}", id, blockID, date, canceled));
        return id;
    }

    /**
     * Update lesson into database
     * @param db database
     * @param lessonID id of the lesson to update
     * @param blockID id of block lesson is in
     * @param date date of lesson
     * @param canceled is it canceled
     * @return id of lesson
     */
    public static long update(Database db, long lessonID, long blockID, Calendar date, boolean canceled){
        ContentValues values = new ContentValues();
        date = Util.setDateToStart(date);
        values.put(FIELD_BLOCK_ID, blockID);
        values.put(FIELD_DAY, date.getTimeInMillis());
        values.put(FIELD_CANCELED, canceled);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.update(TABLE_LESSON_NAME, values, String.format("%s=%d", FIELD_LESSON_ID, lessonID), null);
        Log.d("DATABASE", String.format("Update lesson with Index %1$s, Block ID: %2$s, day %3$s, canceled %4$s}", id, blockID, date, canceled));
        return id;
    }

    /**
     * Delete lesson from database
     * @param db database
     * @param lessonID id of lesson
     */
    public static void delete(Database db, long lessonID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_LESSON_NAME, String.format("%s=%d", FIELD_LESSON_ID, lessonID), null);
        Log.d("DATABASE", String.format("Delete lesson with Index %1$s", lessonID));
    }

    public static void deleteByBlock(Database db, long blockId) {
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_LESSON_NAME, String.format("%s=%d", FIELD_BLOCK_ID, blockId), null);
        Log.d("DATABASE", String.format("Deleted lessons with block %1$s", blockId));
    }

    /**
     * Get all lessons
     * @param db database
     * @return cursor containing all lessons
     */
    public static Cursor getAll(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_LESSON_NAME, null, null, null, null, null, null, null);
    }

    /**
     * Get all lessons by day
     * @param db database
     * @param id id of lesson
     * @return cursor containing lesson of that id
     */
    public static Cursor getByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_LESSON_NAME, null, String.format("%s=%d", FIELD_LESSON_ID, id), null, null, null, null, null);
    }

    public static Cursor getByBlockIDAndDate(Database db, long blockID, Calendar date){
        SQLiteDatabase dbr = db.getReadableDatabase();
        date = Util.setDateToStart(date);
        return dbr.query(false, TABLE_LESSON_NAME, null, String.format("%s=%d AND %s=%d", FIELD_BLOCK_ID, blockID, FIELD_DAY, date.getTimeInMillis()), null, null, null, null, null);
    }

    /**
     * Get all lessons with data from other blocks
     * @param db database
     * @return cursor with lesson and allblock information
     */
    public static Cursor getAllWithBlock(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.rawQuery(String.format("SELECT %1$s as %23$s, %2$s, %3$s as %24$s, %4$s, %5$s, %6$s as %25$s, %7$s, %8$s as %26$s, %9$s as %27$s " +
                        "FROM %10$s " +
                        "LEFT JOIN %11$s ON %10$s.%15$s=%11$s.%16$s " +
                        "LEFT JOIN %12$s ON %11$s.%17$s=%12$s.%20$s " +
                        "LEFT JOIN %13$s ON %11$s.%18$s=%13$s.%21$s " +
                        "LEFT JOIN %14$s ON %11$s.%19$s=%14$s.%22$s",
                DatabaseTableLesson.TABLE_LESSON_NAME + "." + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableLesson.FIELD_CANCELED,
                DatabaseTableBlock.TABLE_BLOCK_NAME + "." + DatabaseTableBlock.FIELD_DAY,
                DatabaseTableBlock.FIELD_START_TIME,
                DatabaseTableBlock.FIELD_LENGTH,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + "." + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableSubject.FIELD_COLOUR,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + "." + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableLocation.TABLE_LOCATION_NAME + "." + DatabaseTableLocation.FIELD_NAME,
                DatabaseTableLesson.TABLE_LESSON_NAME,
                DatabaseTableBlock.TABLE_BLOCK_NAME,
                DatabaseTableSubject.TABLE_SUBJECT_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME,
                DatabaseTableLocation.TABLE_LOCATION_NAME,
                DatabaseTableLesson.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_SUBJECT_ID,
                DatabaseTableBlock.FIELD_TEACHER_ID,
                DatabaseTableBlock.FIELD_LOCATION_ID,
                DatabaseTableSubject.FIELD_SUBJECT_ID,
                DatabaseTableTeacher.FIELD_TEACHER_ID,
                DatabaseTableLocation.FIELD_LOCATION_ID,
                DatabaseTableLesson.TABLE_LESSON_NAME + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableBlock.TABLE_BLOCK_NAME + DatabaseTableBlock.FIELD_DAY,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableLocation.TABLE_LOCATION_NAME + DatabaseTableLocation.FIELD_NAME
        ), null);
    }

    /**
     * Get all lessons with data from other blocks with id
     * @param db database
     * @param id id of lesson
     * @return all lessons with data from block
     */
    public static Cursor getByIDWithBlock(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.rawQuery(String.format("SELECT %1$s as %23$s, %2$s, %3$s as %24$s, %4$s, %5$s, %6$s as %25$s, %7$s, %8$s as %26$s, %9$s as %27$s " +
                        "FROM %10$s " +
                        "LEFT JOIN %11$s ON %10$s.%15$s=%11$s.%16$s " +
                        "LEFT JOIN %12$s ON %11$s.%17$s=%12$s.%20$s " +
                        "LEFT JOIN %13$s ON %11$s.%18$s=%13$s.%21$s " +
                        "LEFT JOIN %14$s ON %11$s.%19$s=%14$s.%22$s " +
                        "WHERE %28$s=%29$s",
                DatabaseTableLesson.TABLE_LESSON_NAME + "." + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableLesson.FIELD_CANCELED,
                DatabaseTableBlock.TABLE_BLOCK_NAME + "." + DatabaseTableBlock.FIELD_DAY,
                DatabaseTableBlock.FIELD_START_TIME,
                DatabaseTableBlock.FIELD_LENGTH,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + "." + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableSubject.FIELD_COLOUR,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + "." + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableLocation.TABLE_LOCATION_NAME + "." + DatabaseTableLocation.FIELD_NAME,
                DatabaseTableLesson.TABLE_LESSON_NAME,
                DatabaseTableBlock.TABLE_BLOCK_NAME,
                DatabaseTableSubject.TABLE_SUBJECT_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME,
                DatabaseTableLocation.TABLE_LOCATION_NAME,
                DatabaseTableLesson.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_SUBJECT_ID,
                DatabaseTableBlock.FIELD_TEACHER_ID,
                DatabaseTableBlock.FIELD_LOCATION_ID,
                DatabaseTableSubject.FIELD_SUBJECT_ID,
                DatabaseTableTeacher.FIELD_TEACHER_ID,
                DatabaseTableLocation.FIELD_LOCATION_ID,
                DatabaseTableLesson.TABLE_LESSON_NAME + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableBlock.TABLE_BLOCK_NAME + DatabaseTableBlock.FIELD_DAY,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableLocation.TABLE_LOCATION_NAME + DatabaseTableLocation.FIELD_NAME,
                DatabaseTableLesson.FIELD_LESSON_ID,
                id
        ), null);
    }

    public static Cursor getByCurrentDay(Database db){
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
        calendar = Util.setDateToStart(calendar);
        long day = calendar.getTimeInMillis();
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.rawQuery(String.format(
                "SELECT %3$s, %1$s.%4$s as %4$s, %1$s.%5$s as %5$s, %6$s " +
                        "FROM %1$S " +
                        "LEFT JOIN %2$s ON %1$s.%4$s = %2$s.%7$s " +
                        "WHERE %1$s.%5$s=%11$s " +
                        "AND %8$s<%10$s " +
                        "AND %8$s+%9$s>%10$s",
                DatabaseTableLesson.TABLE_LESSON_NAME,
                DatabaseTableBlock.TABLE_BLOCK_NAME,
                DatabaseTableLesson.FIELD_LESSON_ID,
                DatabaseTableLesson.FIELD_BLOCK_ID,
                DatabaseTableLesson.FIELD_DAY,
                DatabaseTableLesson.FIELD_CANCELED,
                DatabaseTableBlock.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_START_TIME,
                DatabaseTableBlock.FIELD_LENGTH,
                time,
                day
        ), null);
    }


    public static Cursor getByDay(Database db, Calendar calendar) {
        calendar = Util.setDateToStart(calendar);
        long date = calendar.getTimeInMillis();
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_LESSON_NAME, null, String.format("%s=%d", FIELD_DAY, date), null, null, null, null, null);
    }
}
