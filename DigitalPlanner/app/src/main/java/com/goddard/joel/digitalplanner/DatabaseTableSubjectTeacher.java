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
public class DatabaseTableSubjectTeacher {
    public static String TABLE_SUBJECT_TEACHER_NAME =  "subjectTeachers";
    public static String FIELD_TEACHER_ID =            "teacherID";
    public static String FIELD_SUBJECT_ID =            "subjectID";

    /**
     * @return command to create the table
     */
    public static String getCreateTable(){
        return "CREATE TABLE " + TABLE_SUBJECT_TEACHER_NAME + " (" +
                FIELD_TEACHER_ID +         " INTEGER," +
                FIELD_SUBJECT_ID +          " INTEGER," +
                "PRIMARY KEY (" + FIELD_TEACHER_ID +
                ", " + FIELD_SUBJECT_ID + ")" +
                ");";
    }

    /**
     * Joins teacher to subject
     * @param db database
     * @param subjectID id of subject
     * @param teacherID id of teacher
     */
    public static void insert(Database db, long subjectID, long teacherID){
        ContentValues values = new ContentValues();
        values.put(FIELD_SUBJECT_ID, subjectID);
        values.put(FIELD_TEACHER_ID, teacherID);

        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.insert(TABLE_SUBJECT_TEACHER_NAME, null, values);
        Log.d("DATABASE", String.format("New subject teacher with subjectID: %1$s, teacherID:%2$s}", subjectID, teacherID));
    }

    /**
     * Delete a single entry
     * @param db database
     * @param subjectID id of subject
     * @param teacherID id of teacher
     */
    public static void delete(Database db, long subjectID, long teacherID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_TEACHER_NAME,
                String.format("%s=%d AND %s=%d", FIELD_SUBJECT_ID, subjectID, FIELD_TEACHER_ID, teacherID),
                null);
    }

    /**
     * Deletes all teachers connected to a subject
     * @param db database
     * @param subjectID subject id
     */
    public static void deleteBySubject(Database db, long subjectID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_TEACHER_NAME,
                String.format("%s=%d", FIELD_SUBJECT_ID, subjectID),
                null);
    }

    /**
     * Deletes all subjects connected to a teacher
     * @param db database
     * @param teacherID teacher id
     */
    public static void deleteByTeacher(Database db, long teacherID){
        SQLiteDatabase dbw = db.getWritableDatabase();
        dbw.delete(TABLE_SUBJECT_TEACHER_NAME,
                String.format("%s=%d", FIELD_TEACHER_ID, teacherID),
                null);
    }

    /**
     * Get all teachers by subject
     * @param db database
     * @param subjectID id of subject
     * @return Cursor containing all teachers connected to subject id
     */
    public static Cursor getAllBySubjectID(Database db, long subjectID){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_SUBJECT_TEACHER_NAME, null, String.format("%s=%d", FIELD_SUBJECT_ID, subjectID), null, null, null, null, null);
    }

    /**
     * Get all teachers by subject with teacher data
     * @param db database
     * @param subjectID id of subject
     * @return Cursor containing all teachers connected to subject id
     */
    public static Cursor getAllBySubjectIDWithTeacher(Database db, long subjectID){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.rawQuery(String.format(
                "SELECT %4$s.%1$s as %1$s, %2$s " +
                        "FROM %4$s " +
                        "LEFT JOIN %5$s ON %4$s.%1$s=%5$s.%3$s " +
                        "WHERE %7$s=%6$s",
                DatabaseTableSubjectTeacher.FIELD_TEACHER_ID,
                DatabaseTableTeacher.FIELD_NAME,
                DatabaseTableTeacher.FIELD_TEACHER_ID,
                DatabaseTableSubjectTeacher.TABLE_SUBJECT_TEACHER_NAME,
                DatabaseTableTeacher.TABLE_TEACHER_NAME,
                subjectID,
                DatabaseTableSubject.FIELD_SUBJECT_ID
        ),null);
    }

    /**
     * get all subjects by teacher
     * @param db database
     * @param teacherID id of teacher
     * @return curor containing all subjects connected to teacher id
     */
    public static Cursor getAllByteacherID(Database db, long teacherID){
        SQLiteDatabase dbr = db.getReadableDatabase();
        return dbr.query(false, TABLE_SUBJECT_TEACHER_NAME, null, String.format("%s=%d", FIELD_TEACHER_ID, teacherID), null, null, null, null, null);
    }
}
