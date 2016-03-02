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
public class DatabaseTableHomework {
    public static String TABLE_HOMEWORK_NAME =      "homeworks";
    public static String FIELD_HOMEWORK_ID =        "homeworkID";
    public static String FIELD_LESSON_SET =         "lessonSet";
    public static String FIELD_LESSON_DUE =         "lessonDue";
    public static String FIELD_ESTIMATED_LENGTH =   "estimatedLength"; //Length in minutes
    public static String FIELD_SCHEDULED_TIME =     "schedueledTime";
    public static String FIELD_DESCRIPTION =        "description";
    public static String FIELD_DESCRIPTION_SHORT =  "shortDescription";
    public static String FIELD_DONE =               "done";

    public static String getCreateTable(){
        /**
         * gets the create table command for this table
         * */
        return "CREATE TABLE " + TABLE_HOMEWORK_NAME + " (" +
                FIELD_HOMEWORK_ID +         " INTEGER PRIMARY KEY," +
                FIELD_LESSON_SET +          " INTEGER," +
                FIELD_LESSON_DUE +          " INTEGER," +
                FIELD_ESTIMATED_LENGTH +    " INTEGER," +
                FIELD_SCHEDULED_TIME +      " TEXT," +
                FIELD_DESCRIPTION +         " TEXT," +
                FIELD_DESCRIPTION_SHORT +   " TEXT," +
                FIELD_DONE +                " INTEGER"+
                ");";
    }

    /**
     * Insert homework into database
     * @param db database
     * @param lessonSet id of lesson set
     * @param lessonDue id of lesson due
     * @param estimatedLength estimated length of homework
     * @param schelduledTime time scheduled to do homework
     * @param description description of homework
     * @param shortDescription short summary of description
     * @param done has the homework been done
     * @return id of homework
     */
    public static long insert(Database db, long lessonSet, long lessonDue, int estimatedLength, long schelduledTime, String description, String shortDescription, boolean done){
        ContentValues values = new ContentValues();
        values.put(FIELD_LESSON_SET,        lessonSet);
        values.put(FIELD_LESSON_DUE,        lessonDue);
        values.put(FIELD_ESTIMATED_LENGTH,  estimatedLength);
        values.put(FIELD_SCHEDULED_TIME,    schelduledTime);
        values.put(FIELD_DESCRIPTION,       description);
        values.put(FIELD_DESCRIPTION_SHORT, shortDescription);
        values.put(FIELD_DONE,              done);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long id = dbw.insert(TABLE_HOMEWORK_NAME, null, values);//This function automatically sanitizes input
        Log.d("DATABASE", String.format("New homework with Index %1$s, lesson set: %2$s, lesson due:%3$s, estimated length:%4$s, scheduled time:%5$s, description:%6$s, short description:%7$s}", id, lessonSet, lessonDue, estimatedLength, schelduledTime, description, shortDescription));
        return id;
    }

    /**
     * Update homework
     * @param db database
     * @param id id of homework
     * @param lessonSet id of lesson set
     * @param lessonDue id of lesson due
     * @param estimatedLength estimate length of homework
     * @param schelduledTime time scheduled to do homework
     * @param description description of homework
     * @param shortDescription short summary of description
     * @return id of homework
     */
    public static long update(Database db, long id, long lessonSet, long lessonDue, int estimatedLength, long schelduledTime, String description, String shortDescription, boolean done){
        ContentValues values = new ContentValues();
        values.put(FIELD_LESSON_SET,        lessonSet);
        values.put(FIELD_LESSON_DUE,        lessonDue);
        values.put(FIELD_ESTIMATED_LENGTH,  estimatedLength);
        values.put(FIELD_SCHEDULED_TIME,    schelduledTime);
        values.put(FIELD_DESCRIPTION,       description);
        values.put(FIELD_DESCRIPTION_SHORT, shortDescription);
        values.put(FIELD_DONE,              done);

        SQLiteDatabase dbw = db.getWritableDatabase();
        long idr = dbw.update(TABLE_HOMEWORK_NAME, values, String.format("%s=%d", FIELD_HOMEWORK_ID, id), null);//This function automatically sanitizes input
        Log.d("DATABASE", String.format("Update homework with Index %1$s, lesson set: %2$s, lesson due:%3$s, estimated length:%4$s, scheduled time:%5$s, description:%6$s, short description:%7$s}", id, lessonSet, lessonDue, estimatedLength, schelduledTime, description, shortDescription));
        return idr;
    }

    /**
     * delete homework
     * @param db database
     * @param id id of homework
     */
    public static void delete(Database db, long id){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_HOMEWORK_NAME, String.format("%s=%d", FIELD_HOMEWORK_ID, id), null);
        Log.d("DATABASE", String.format("Delete homework with Index %1$s}", id));
    }

    /**
     * get all homework
     * @param db database
     * @return cursor containing all homework
     */
    public static Cursor getAll(Database db){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_HOMEWORK_NAME, null, null, null, null, null, null, null);
    }

    /**
     * get homework by id
     * @param db database
     * @param id id
     * @return cursor containing homework with id
     */
    public static Cursor getByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_HOMEWORK_NAME, null, String.format("%s=%d", FIELD_HOMEWORK_ID, id), null, null, null, null, null);
    }

    public static Cursor getByLessonSet(Database db, long lessonId){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_HOMEWORK_NAME, null, String.format("%s=%d", FIELD_LESSON_SET, lessonId), null, null, null, null, null);
    }

    public static Cursor getByLessonDue(Database db, long lessonId){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_HOMEWORK_NAME, null, String.format("%s=%d", FIELD_LESSON_DUE, lessonId), null, null, null, null, null);
    }

    /**
     * get homeworks with other tables data
     * @param db database
     * @return cursor containing all homeworks and data from other tables
     */
    public static Cursor getAllWithLessons(Database db) {
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.rawQuery(String.format(
                "SELECT %1$s, %2$s, %3$s, " +
                        "%4$s as %24$s, %5$s, %6$s, %7$s as %25$s, %8$s, %9$s as %26$s " +
                        "FROM %10$s " +
                        "LEFT JOIN %11$s ON %10$s.%15$s=%11$s.%17$s " +
                        "LEFT JOIN %11$s ON %10$s.%16$s=%11$s.%17$s " +
                        "LEFT JOIN %12$s ON %11$s.%18$s=%12$s.%19$s " +
                        "LEFT JOIN %13$s ON %12$s.%20$s=%13$s.%22$s " +
                        "LEFT JOIN %14$s ON %12$s.%21$s=%14$s.%23$s",
                DatabaseTableHomework.FIELD_DESCRIPTION,
                DatabaseTableHomework.FIELD_DESCRIPTION_SHORT,
                DatabaseTableHomework.FIELD_ESTIMATED_LENGTH,
                DatabaseTableLesson.TABLE_LESSON_NAME + "." + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableLesson.FIELD_CANCELED,
                DatabaseTableBlock.FIELD_START_TIME,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + "." + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableSubject.FIELD_COLOUR,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + "." + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableHomework.TABLE_HOMEWORK_NAME,
                DatabaseTableLesson.TABLE_LESSON_NAME,
                DatabaseTableBlock.TABLE_BLOCK_NAME,
                DatabaseTableSubject.TABLE_SUBJECT_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME,
                DatabaseTableHomework.FIELD_LESSON_SET,
                DatabaseTableHomework.FIELD_LESSON_DUE,
                DatabaseTableLesson.FIELD_LESSON_ID,
                DatabaseTableLesson.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_SUBJECT_ID,
                DatabaseTableBlock.FIELD_TEACHER_ID,
                DatabaseTableSubject.FIELD_SUBJECT_ID,
                DatabaseTableTeacher.FIELD_TEACHER_ID,
                DatabaseTableLesson.TABLE_LESSON_NAME + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + DatabaseTableTeacher.FIELD_NAME
        ), null);
    }

    public static Cursor getAllWithLessonsByID(Database db, long id){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.rawQuery(String.format("SELECT %1$s, %2$s, %3$s, %4$s as %24$s, %5$s, %6$s, %7$s as %25$s, %8$s, %9$s as %26$s " +
                        "FROM %10$s " +
                        "LEFT JOIN %11$s ON %10$s.%15$s=%11$s.%17$s " +
                        "LEFT JOIN %11$s ON %10$s.%16$s=%11$s.%17$s " +
                        "LEFT JOIN %12$s ON %11$s.%18$s=%12$s.%19$s " +
                        "LEFT JOIN %13$s ON %12$s.%20$s=%13$s.%22$s " +
                        "LEFT JOIN %14$s ON %12$s.%21$s=%14$s.%23$s " +
                        "WHERE %27$s=%28$s",
                DatabaseTableHomework.FIELD_DESCRIPTION,
                DatabaseTableHomework.FIELD_DESCRIPTION_SHORT,
                DatabaseTableHomework.FIELD_ESTIMATED_LENGTH,
                DatabaseTableLesson.TABLE_LESSON_NAME + "." + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableLesson.FIELD_CANCELED,
                DatabaseTableBlock.FIELD_START_TIME,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + "." + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableSubject.FIELD_COLOUR,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + "." + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableHomework.TABLE_HOMEWORK_NAME,
                DatabaseTableLesson.TABLE_LESSON_NAME,
                DatabaseTableBlock.TABLE_BLOCK_NAME,
                DatabaseTableSubject.TABLE_SUBJECT_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME,
                DatabaseTableHomework.FIELD_LESSON_SET,
                DatabaseTableHomework.FIELD_LESSON_DUE,
                DatabaseTableLesson.FIELD_LESSON_ID,
                DatabaseTableLesson.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_BLOCK_ID,
                DatabaseTableBlock.FIELD_SUBJECT_ID,
                DatabaseTableBlock.FIELD_TEACHER_ID,
                DatabaseTableSubject.FIELD_SUBJECT_ID,
                DatabaseTableTeacher.FIELD_TEACHER_ID,
                DatabaseTableLesson.TABLE_LESSON_NAME + DatabaseTableLesson.FIELD_DAY,
                DatabaseTableSubject.TABLE_SUBJECT_NAME + DatabaseTableSubject.FIELD_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME + DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableHomework.FIELD_HOMEWORK_ID,
                id
        ), null);
    }
}
