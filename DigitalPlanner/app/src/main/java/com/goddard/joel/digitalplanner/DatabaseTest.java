package com.goddard.joel.digitalplanner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Joel Goddard on 06/01/2016.
 *
 * @author Joel Goddard
 */
public class DatabaseTest {
    private static Database db;

    public static void printCursor(Cursor c, String name){
        c.moveToFirst();
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

        long idTeacherA = DatabaseTableTeacher.insert(db, "Teacher A");
        long idTeacherB = DatabaseTableTeacher.insert(db, "Teacher B");
        long idTeacherC = DatabaseTableTeacher.insert(db, "Teacher C");
        long idTeacherD = DatabaseTableTeacher.insert(db, "Teacher D");
        long idTeacherE = DatabaseTableTeacher.insert(db, "Teacher E");
        long idTeacherF = DatabaseTableTeacher.insert(db, "Teacher F");

        long idLocation1 = DatabaseTableLocation.insert(db, "A001");
        long idLocation2 = DatabaseTableLocation.insert(db, "A002");
        long idLocation3 = DatabaseTableLocation.insert(db, "B003");
        long idLocation4 = DatabaseTableLocation.insert(db, "C004");
        long idLocation5 = DatabaseTableLocation.insert(db, "D005");
        long idLocation6 = DatabaseTableLocation.insert(db, "D006");

        long idSubjectA = DatabaseTableSubject.insert(db, "Subject A", Color.rgb(20, 60, 20));
        long idSubjectB = DatabaseTableSubject.insert(db, "Subject B", Color.rgb(100, 60, 20));
        long idSubjectC = DatabaseTableSubject.insert(db, "Subject C", Color.rgb(250, 60, 20));
        long idSubjectD = DatabaseTableSubject.insert(db, "Subject D", Color.rgb(20, 60, 250));
        long idSubjectE = DatabaseTableSubject.insert(db, "Subject E", Color.rgb(20, 90, 20));
        long idSubjectF = DatabaseTableSubject.insert(db, "Subject F", Color.rgb(60, 60, 90));

        DatabaseTableSubjectTeacher.insert(db, idSubjectA, idTeacherA);
        DatabaseTableSubjectTeacher.insert(db, idSubjectB, idTeacherA);
        DatabaseTableSubjectTeacher.insert(db, idSubjectB, idTeacherB);
        DatabaseTableSubjectTeacher.insert(db, idSubjectC, idTeacherC);
        DatabaseTableSubjectTeacher.insert(db, idSubjectC, idTeacherD);
        DatabaseTableSubjectTeacher.insert(db, idSubjectD, idTeacherE);
        DatabaseTableSubjectTeacher.insert(db, idSubjectE, idTeacherD);
        DatabaseTableSubjectTeacher.insert(db, idSubjectE, idTeacherF);
        DatabaseTableSubjectTeacher.insert(db, idSubjectF, idTeacherF);

        DatabaseTableSubjectLocation.insert(db, idSubjectA, idLocation1);
        DatabaseTableSubjectLocation.insert(db, idSubjectB, idLocation1);
        DatabaseTableSubjectLocation.insert(db, idSubjectB, idLocation2);
        DatabaseTableSubjectLocation.insert(db, idSubjectC, idLocation3);
        DatabaseTableSubjectLocation.insert(db, idSubjectC, idLocation4);
        DatabaseTableSubjectLocation.insert(db, idSubjectD, idLocation5);
        DatabaseTableSubjectLocation.insert(db, idSubjectE, idLocation4);
        DatabaseTableSubjectLocation.insert(db, idSubjectE, idLocation6);
        DatabaseTableSubjectLocation.insert(db, idSubjectF, idLocation6);

        long idBlockA1 = DatabaseTableBlock.insert(db, 1,  20, 65, idSubjectA, idTeacherA, idLocation1);
        long idBlockA2 = DatabaseTableBlock.insert(db, 1,  90, 65, idSubjectB, idTeacherB, idLocation4);
        long idBlockA3 = DatabaseTableBlock.insert(db, 1, 160, 65, idSubjectC, idTeacherC, idLocation6);
        long idBlockA4 = DatabaseTableBlock.insert(db, 1, 230, 65, idSubjectA, idTeacherD, idLocation3);
        long idBlockA5 = DatabaseTableBlock.insert(db, 2, 300, 65, idSubjectD, idTeacherE, idLocation3);
        long idBlockB1 = DatabaseTableBlock.insert(db, 2,  30, 65, idSubjectE, idTeacherF, idLocation5);
        long idBlockB2 = DatabaseTableBlock.insert(db, 2, 100, 65, idSubjectF, idTeacherB, idLocation6);
        long idBlockB3 = DatabaseTableBlock.insert(db, 2, 170, 65, -1,         -1,         -1         );
        long idBlockB4 = DatabaseTableBlock.insert(db, 2, 240, 65, idSubjectC, idTeacherE, idLocation4);
        long idBlockB5 = DatabaseTableBlock.insert(db, 2, 310, 65, idSubjectB, idTeacherC, idLocation1);

        long idLesson1a = DatabaseTableLesson.insert(db, idBlockA1, Calendar.getInstance(), false);
        long idLesson2a = DatabaseTableLesson.insert(db, idBlockA2, Calendar.getInstance(), false);
        long idLesson3a = DatabaseTableLesson.insert(db, idBlockA3, Calendar.getInstance(), false);
        long idLesson4a = DatabaseTableLesson.insert(db, idBlockA4, Calendar.getInstance(), false);
        long idLesson5a = DatabaseTableLesson.insert(db, idBlockA5, Calendar.getInstance(), false);
        long idLesson6a = DatabaseTableLesson.insert(db, idBlockB1, Calendar.getInstance(), false);
        long idLesson7a = DatabaseTableLesson.insert(db, idBlockB2, Calendar.getInstance(), false);
        long idLesson8a = DatabaseTableLesson.insert(db, idBlockB3, Calendar.getInstance(), false);
        long idLesson1b = DatabaseTableLesson.insert(db, idBlockA1, Calendar.getInstance(), false);
        long idLesson2b = DatabaseTableLesson.insert(db, idBlockA2, Calendar.getInstance(), false);
        long idLesson3b = DatabaseTableLesson.insert(db, idBlockA3, Calendar.getInstance(), false);
        long idLesson4b = DatabaseTableLesson.insert(db, idBlockA4, Calendar.getInstance(), false);
        long idLesson5b = DatabaseTableLesson.insert(db, idBlockA5, Calendar.getInstance(), false);
        long idLesson6b = DatabaseTableLesson.insert(db, idBlockB1, Calendar.getInstance(), false);
        long idLesson7b = DatabaseTableLesson.insert(db, idBlockB2, Calendar.getInstance(), false);
        long idLesson8b = DatabaseTableLesson.insert(db, idBlockB3, Calendar.getInstance(), false);
        long idLesson1c = DatabaseTableLesson.insert(db, idBlockB4, Calendar.getInstance(), false);
        long idLesson2c = DatabaseTableLesson.insert(db, idBlockB4, Calendar.getInstance(), false);
        long idLesson3c = DatabaseTableLesson.insert(db, idBlockB5, Calendar.getInstance(), false);
        long idLesson4c = DatabaseTableLesson.insert(db, idBlockA1, Calendar.getInstance(), false);
        long idLesson5c = DatabaseTableLesson.insert(db, idBlockA2, Calendar.getInstance(), false);

        long idHomework1 = DatabaseTableHomework.insert(db, idLesson1a, idLesson1b, 60, Calendar.getInstance(), "Do the homework 1", "h1");
        long idHomework2 = DatabaseTableHomework.insert(db, idLesson2a, idLesson6a, 60, Calendar.getInstance(), "Do the homework 2", "h2");
        long idHomework3 = DatabaseTableHomework.insert(db, idLesson2a, idLesson4b, 120, Calendar.getInstance(), "Do the homework 3", "h3");
        long idHomework4 = DatabaseTableHomework.insert(db, idLesson3a, idLesson5b, 60, Calendar.getInstance(), "Do the homework 4", "h4");
        long idHomework5 = DatabaseTableHomework.insert(db, idLesson1b, idLesson2c, 60, Calendar.getInstance(), "Do the homework 5", "h5");
        long idHomework6 = DatabaseTableHomework.insert(db, idLesson3a, idLesson3c, 60, Calendar.getInstance(), "Do the homework 6", "h6");

        printAllTables(db);

        Log.d("DATABASE", "--------------------------------------------------\n" +
                "Deleting Stuff");

        DatabaseTableTeacher.delete(db, idTeacherF);
        DatabaseTableLocation.delete(db, idLocation6);
        DatabaseTableSubject.delete(db, idSubjectF);
        DatabaseTableSubjectLocation.delete(db, idSubjectE, idLocation4);
        DatabaseTableSubjectTeacher.delete(db, idSubjectE, idTeacherD);
        DatabaseTableBlock.delete(db, idBlockB5);
        DatabaseTableLesson.delete(db, idLesson8b);
        DatabaseTableHomework.delete(db, idHomework6);

        printAllTables(db);

//        Log.d("DATABASE", "Print Block Table");
//        Cursor c = DatabaseTableBlock.getAll(db);
//        c.moveToFirst();
//        for(int i=0; i<c.getCount();i++){
//            Log.d("DATABASE", String.format("Block: id %1$s, day %2$s, startTime %3$s, length %4$s",
//                    c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_BLOCK_ID)),
//                    c.getString(c.getColumnIndex(DatabaseTableBlock.FIELD_DAY)),
//                    c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_START_TIME)),
//                    c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_LENGTH))));
//            c.move(1);
//        }
//        c.close();
//        c = DatabaseTableBlock.getByID(db, idBlock);
//        Log.d("DATABASE", String.valueOf(idBlock));
//        c.moveToFirst();
//        Log.d("DATABASE", String.format("Block: id %1$s, day %2$s, startTime %3$s, length %4$s",
//                c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_BLOCK_ID)),
//                c.getString(c.getColumnIndex(DatabaseTableBlock.FIELD_DAY)),
//                c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_START_TIME)),
//                c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_LENGTH))));
//
//        SQLiteDatabase dbr = db.getReadableDatabase();
//        c.close();
//        c = DatabaseTableLesson.getAllWithBlock(db);
//
//        c.moveToFirst();
//        for(int i=0; i<c.getCount();i++){
//            Log.d("DATABASE", String.format("Lesson: date %s, canceled %s, day %s, startTime %s, length %s, subject %s, colour %s, teacher %s, location %s",
//                    c.getString(c.getColumnIndex(DatabaseTableLesson.TABLE_LESSON_NAME + DatabaseTableLesson.FIELD_DAY)),
//                    c.getInt(c.getColumnIndex(DatabaseTableLesson.FIELD_CANCELED)),
//                    c.getInt(c.getColumnIndex(DatabaseTableBlock.TABLE_BLOCK_NAME + DatabaseTableBlock.FIELD_DAY)),
//                    c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_START_TIME)),
//                    c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_LENGTH)),
//                    c.getString(c.getColumnIndex(DatabaseTableSubject.TABLE_SUBJECT_NAME + DatabaseTableSubject.FIELD_NAME)),
//                    c.getInt(c.getColumnIndex(DatabaseTableSubject.FIELD_COLOUR)),
//                    c.getString(c.getColumnIndex(DatabaseTableTeacher.TABLE_TEACHER_NAME + DatabaseTableTeacher.FIELD_NAME)),
//                    c.getString(c.getColumnIndex(DatabaseTableLocation.TABLE_LOCATION_NAME + DatabaseTableLocation.FIELD_NAME))
//                    ));
//            c.move(1);
//
//        }
    }
}
