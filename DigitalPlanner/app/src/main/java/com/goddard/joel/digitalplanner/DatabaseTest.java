package com.goddard.joel.digitalplanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Joel Goddard on 06/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTest {
    private static Database db;

    public static void printCursor(Cursor c, String name){
        c.moveToFirst();
        Log.d("DATABASE", String.format("%d rows returned for %s", c.getCount(), name));
        for(int i=0; i<c.getCount(); i++){
            String s = String.format("%s %d {", name, i);
            for(int j=0; j<c.getColumnCount(); j++){
                s += String.format("%1$s: %2$s", c.getColumnName(j), c.getString(j));
                if (j<c.getColumnCount()-1){
                    s+=", ";
                }
            }
            s+="}";
            Log.d("DATABASE", s);
            c.move(1);
        }
    }

    public static void printRowNames(Cursor c, String name){
        String s = name + ": ";
        for(int i=0; i<c.getColumnCount();i++){
            s += String.format("%1$s", c.getColumnName(i));
            if (i<c.getColumnCount()-1){
                s+=", ";
            }
        }
        Log.d("DATABASE", s);
    }

    public static void printAllTables(Database db){
        Log.d("DATABASE", "Print Teachers");
        Cursor cT = DatabaseTableTeacher.getAll(db);
        printCursor(cT, DatabaseTableTeacher.TABLE_TEACHER_NAME);

        Log.d("DATABASE", "Print Locations");
        Cursor cLo = DatabaseTableLocation.getAll(db);
        printCursor(cLo, DatabaseTableLocation.TABLE_LOCATION_NAME);

        Log.d("DATABASE", "Print Subjects");
        Cursor cS = DatabaseTableSubject.getAll(db);
        cS.moveToFirst();
        for(int i=0; i<cS.getCount(); i++){
            String s = String.format("%s %d {", DatabaseTableSubject.TABLE_SUBJECT_NAME, i);
            for(int j=0; j<cS.getColumnCount(); j++){
                s += String.format("%1$s: %2$s", cS.getColumnName(j), cS.getString(j));
                if (j<cS.getColumnCount()-1){
                    s+=", ";
                }
            }
            s+="}";
            Log.d("DATABASE", s);
            Log.d("DATABASE", "\tSubject Teachers");
            Cursor cST = DatabaseTableSubjectTeacher.getAllBySubjectID(db, cS.getLong(cS.getColumnIndex(DatabaseTableSubject.FIELD_SUBJECT_ID)));
            printCursor(cST, "\t" + DatabaseTableSubjectTeacher.TABLE_SUBJECT_TEACHER_NAME);
            Log.d("DATABASE", "\tSubject Locations");
            Cursor cSLo = DatabaseTableSubjectLocation.getAllBySubjectID(db, cS.getLong(cS.getColumnIndex(DatabaseTableSubject.FIELD_SUBJECT_ID)));
            printCursor(cSLo, "\t" + DatabaseTableSubjectLocation.TABLE_SUBJECT_LOCATION_NAME);
            cS.move(1);
        }

        Log.d("DATABASE", "Print Blocks");
        Cursor cB = DatabaseTableBlock.getAll(db);
        printCursor(cB, DatabaseTableBlock.TABLE_BLOCK_NAME);

        Log.d("DATABASE", "Print Lessons");
        Cursor cLe = DatabaseTableLesson.getAll(db);
        printCursor(cLe, DatabaseTableLesson.TABLE_LESSON_NAME);

        Log.d("DATABASE", "Print Homework");
        Cursor cH = DatabaseTableHomework.getAll(db);
        printCursor(cH, DatabaseTableHomework.TABLE_HOMEWORK_NAME);
    }

    public static void test(Context context){
        Log.d("DATABASE", "------------Starting test-------------");
        Log.d("DATABASE", "Deleting old database");
        Database.wipeDatabase(context);
        db = new Database(context);
        Log.d("DATABASE", "Creating new database");
        Log.d("DATABASE", "Creating table teachers for test 1.1.1");
        printRowNames(DatabaseTableTeacher.getAll(db), "Test 1.1.1");
        printCursor(DatabaseTableTeacher.getAll(db), "Test 1.1.2");
        DatabaseTableTeacher.insert(db, "Teacher A");
        printCursor(DatabaseTableTeacher.getAll(db), "Test 1.1.3");
        DatabaseTableTeacher.delete(db, 1);
        printCursor(DatabaseTableTeacher.getAll(db), "Test 1.1.4");
        DatabaseTableTeacher.insert(db, "Teacher B");
        DatabaseTableTeacher.insert(db, "Teacher C");
        DatabaseTableTeacher.insert(db, "Teacher D");
        printCursor(DatabaseTableTeacher.getAll(db), "Test 1.1.5");
        DatabaseTableTeacher.update(db, 2, "Teacher E");
        printCursor(DatabaseTableTeacher.getAll(db), "Test 1.1.6");
        printCursor(DatabaseTableTeacher.getByID(db, 3), "Test 1.1.7");
        printCursor(DatabaseTableTeacher.getByID(db, 4), "Test 1.1.8");

        Log.d("DATABASE", "Creating table locations for test 1.2.1");
        printRowNames(DatabaseTableLocation.getAll(db), "Test 1.2.1");
        printCursor(DatabaseTableLocation.getAll(db), "Test 1.2.2");
        DatabaseTableLocation.insert(db, "Location 1");
        printCursor(DatabaseTableLocation.getAll(db), "Test 1.2.3");
        DatabaseTableLocation.delete(db, 1);
        printCursor(DatabaseTableLocation.getAll(db), "Test 1.2.4");
        DatabaseTableLocation.insert(db, "Location 2");
        DatabaseTableLocation.insert(db, "Location 3");
        DatabaseTableLocation.insert(db, "Location 4");
        printCursor(DatabaseTableLocation.getAll(db), "Test 1.2.5");
        DatabaseTableLocation.update(db, 2, "Location 5");
        printCursor(DatabaseTableLocation.getAll(db), "Test 1.2.6");
        printCursor(DatabaseTableLocation.getByID(db, 3), "Test 1.2.7");
        printCursor(DatabaseTableLocation.getByID(db, 4), "Test 1.2.8");

        Log.d("DATABASE", "Creating table subject for test 1.3.1");
        printRowNames(DatabaseTableSubject.getAll(db), "Test 1.3.1");
        printCursor(DatabaseTableSubject.getAll(db), "Test 1.3.2");
        DatabaseTableSubject.insert(db, "Subject 1", 0);
        printCursor(DatabaseTableSubject.getAll(db), "Test 1.3.3");
        DatabaseTableSubject.delete(db, 1);
        printCursor(DatabaseTableSubject.getAll(db), "Test 1.3.4");
        DatabaseTableSubject.insert(db, "Subject 2", 0);
        DatabaseTableSubject.insert(db, "Subject 3", 0);
        DatabaseTableSubject.insert(db, "Subject 4", 0);
        printCursor(DatabaseTableSubject.getAll(db), "Test 1.3.5");
        DatabaseTableSubject.update(db, 2, "Subject 5", 0);
        printCursor(DatabaseTableSubject.getAll(db), "Test 1.3.6");
        printCursor(DatabaseTableSubject.getByID(db, 3), "Test 1.3.7");
        printCursor(DatabaseTableSubject.getByID(db, 4), "Test 1.3.8");

        Log.d("DATABASE", "Creating table subjectTeacher for test 1.4.1");
        printRowNames(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.1");
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.2");
        DatabaseTableSubjectTeacher.insert(db, 1, 1);
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.3");
        DatabaseTableSubjectTeacher.delete(db, 1, 1);
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.4");
        DatabaseTableSubjectTeacher.insert(db, 1, 1);
        DatabaseTableSubjectTeacher.insert(db, 2, 1);
        DatabaseTableSubjectTeacher.insert(db, 2, 3);
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.5");
        printCursor(DatabaseTableSubjectTeacher.getAllBySubjectID(db, 1), "Test 1.4.6");
        printCursor(DatabaseTableSubjectTeacher.getAllBySubjectID(db, 3), "Test 1.4.7");
        printCursor(DatabaseTableSubjectTeacher.getAllByTeacherID(db, 1), "Test 1.4.8");
        printCursor(DatabaseTableSubjectTeacher.getAllByTeacherID(db, 3), "Test 1.4.9");
        DatabaseTableSubjectTeacher.insert(db, 2, 2);
        DatabaseTableSubjectTeacher.insert(db, 1, 2);
        DatabaseTableSubjectTeacher.insert(db, 3, 3);
        DatabaseTableSubjectTeacher.insert(db, 3, 1);
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.10.0");
        DatabaseTableSubjectTeacher.deleteBySubject(db, 3);
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.10.1");
        DatabaseTableSubjectTeacher.deleteByTeacher(db, 2);
        printCursor(DatabaseTableSubjectTeacher.getAll(db), "Test 1.4.10.2");

        Log.d("DATABASE", "Creating table subjectLocation for test 1.5.1");
        printRowNames(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.1");
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.2");
        DatabaseTableSubjectLocation.insert(db, 1, 1);
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.3");
        DatabaseTableSubjectLocation.delete(db, 1, 1);
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.4");
        DatabaseTableSubjectLocation.insert(db, 1, 1);
        DatabaseTableSubjectLocation.insert(db, 2, 1);
        DatabaseTableSubjectLocation.insert(db, 2, 3);
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.5");
        printCursor(DatabaseTableSubjectLocation.getAllBySubjectID(db, 1), "Test 1.5.6");
        printCursor(DatabaseTableSubjectLocation.getAllBySubjectID(db, 3), "Test 1.5.7");
        printCursor(DatabaseTableSubjectLocation.getAllByLocationID(db, 1), "Test 1.5.8");
        printCursor(DatabaseTableSubjectLocation.getAllByLocationID(db, 3), "Test 1.5.9");
        DatabaseTableSubjectLocation.insert(db, 2, 2);
        DatabaseTableSubjectLocation.insert(db, 1, 2);
        DatabaseTableSubjectLocation.insert(db, 3, 3);
        DatabaseTableSubjectLocation.insert(db, 3, 1);
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.10.0");
        DatabaseTableSubjectLocation.deleteBySubject(db, 3);
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.10.1");
        DatabaseTableSubjectLocation.deleteByLocation(db, 2);
        printCursor(DatabaseTableSubjectLocation.getAll(db), "Test 1.5.10.2");

        Log.d("DATABASE", "Creating table block for test 1.6.1");
        printRowNames(DatabaseTableBlock.getAll(db), "Test 1.6.1");
        printCursor(DatabaseTableBlock.getAll(db), "Test 1.6.2");
        DatabaseTableBlock.insert(db, 1, 1200, 60, 2, 3, 2);
        printCursor(DatabaseTableBlock.getAll(db), "Test 1.6.3");
        DatabaseTableBlock.delete(db, 1);
        printCursor(DatabaseTableBlock.getAll(db), "Test 1.6.4");
        DatabaseTableBlock.insert(db, 4, 800, 65, 3, 2, 2);
        DatabaseTableBlock.insert(db, 0, 750, 60, 1, 3, 3);
        DatabaseTableBlock.insert(db, 6, 400, 30, 1, 2, 3);
        printCursor(DatabaseTableBlock.getAll(db), "Test 1.6.5");
        DatabaseTableBlock.update(db, 2, 4, 750, 70, 3, 2, 1);
        printCursor(DatabaseTableBlock.getAll(db), "Test 1.6.6");
        printCursor(DatabaseTableBlock.getByID(db, 2), "Test 1.6.7");
        printCursor(DatabaseTableBlock.getByID(db, 4), "Test 1.6.8");
        printCursor(DatabaseTableBlock.getByDay(db, 6), "Test 1.6.9");
        printCursor(DatabaseTableBlock.getByDay(db, 4), "Test 1.6.10");
        printCursor(DatabaseTableBlock.getByDay(db, 0), "Test 1.6.11");

        Log.d("DATABASE", "Creating table lesson for test 1.7.1");
        printRowNames(DatabaseTableLesson.getAll(db), "Test 1.7.1");
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.2");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(1454284800000L);
        DatabaseTableLesson.insert(db, 1, c, false);
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.3");
        DatabaseTableLesson.delete(db, 1);
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.4");
        c.setTimeInMillis(1454457600000L);
        DatabaseTableLesson.insert(db, 1, c, false);
        c.setTimeInMillis(1454544000000L);
        DatabaseTableLesson.insert(db, 2, c, true);
        c.setTimeInMillis(1454371200000L);
        DatabaseTableLesson.insert(db, 3, c, false);
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.5");
        c.setTimeInMillis(1454457600000L);
        DatabaseTableLesson.update(db, 2, 1, c, true);
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.6");
        printCursor(DatabaseTableLesson.getByID(db, 2), "Test 1.7.7");
        printCursor(DatabaseTableLesson.getByID(db, 4), "Test 1.7.8");
        c.setTimeInMillis(1454457600000L);
        printCursor(DatabaseTableLesson.getByBlockIDAndDate(db, 3, c), "Test 1.7.9");
        c.setTimeInMillis(1454544000000L);
        printCursor(DatabaseTableLesson.getByBlockIDAndDate(db, 4, c), "Test 1.7.10");
        c.setTimeInMillis(1454371200000L);
        printCursor(DatabaseTableLesson.getByBlockIDAndDate(db, 3, c), "Test 1.7.11");
        c=Calendar.getInstance();
        int mins = c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE)*60-15;
        DatabaseTableBlock.insert(db, c.get(Calendar.DAY_OF_WEEK), mins, 30, 3, 2, 1);
        c = Util.setDateToStart(c);
        DatabaseTableLesson.insert(db, 4, c, false);
        printCursor(DatabaseTableBlock.getAll(db), "Test 1.7.12.0a");
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.12.0b");
        printCursor(DatabaseTableLesson.getByCurrentDay(db), "Test 1.7.12.1");
        DatabaseTableLocation.delete(db, 4);
        printCursor(DatabaseTableLesson.getAll(db), "Test 1.7.13.0");
        printCursor(DatabaseTableLesson.getByCurrentDay(db), "Test 1.7.13.1");

        Log.d("DATABASE", "Creating table homework for test 1.8.1");
        printRowNames(DatabaseTableHomework.getAll(db), "Test 1.8.1");
        printCursor(DatabaseTableHomework.getAll(db), "Test 1.8.2");
        DatabaseTableHomework.insert(db, 1, 2, 0, 0, "Description A", "", false);
        printCursor(DatabaseTableHomework.getAll(db), "Test 1.8.3");
        DatabaseTableHomework.delete(db, 1);
        printCursor(DatabaseTableHomework.getAll(db), "Test 1.8.4");
        DatabaseTableHomework.insert(db, 1, 2, 0, 0, "Description B", "One line description", false);
        DatabaseTableHomework.insert(db, 1, 3, 0, 0,"Description C", "Long description with lots of padding padding padding padding padding padding", true);
        DatabaseTableHomework.insert(db, 2, 3, 0, 0, "Description D", "", false);
        printCursor(DatabaseTableHomework.getAll(db), "Test 1.8.5");
        DatabaseTableHomework.update(db, 3, 1, 1, 0, 0, "Description E", "", false);
        printCursor(DatabaseTableHomework.getAll(db), "Test 1.8.6");
        printCursor(DatabaseTableHomework.getByID(db, 2), "Test 1.8.7");
        printCursor(DatabaseTableHomework.getByID(db, 4), "Test 1.8.8");
        printCursor(DatabaseTableHomework.getByLessonSet(db, 1), "Test 1.8.9");
        printCursor(DatabaseTableHomework.getByLessonSet(db, 3), "Test 1.8.10");
        printCursor(DatabaseTableHomework.getByLessonDue(db, 3), "Test 1.8.11");
        printCursor(DatabaseTableHomework.getByLessonDue(db, 1), "Test 1.8.12");
    }
}
