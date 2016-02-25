package com.goddard.joel.digitalplanner;

import android.database.Cursor;

import java.util.Calendar;

/**
 * Created by Joel Goddard on 10/02/2016.
 *
 * @author Joel Goddard
 */
public class Homework {
    private long id;
    private Lesson lessonSet;
    private Lesson lessonDue;

    private String description;
    private String shortDescription;

    private int estimatedLength;
    private Calendar scheduleTime;

    private boolean done;

    public Homework(long id) {
        this.id = id;
    }

    public Homework(Database db, long id){
        this.id = id;
        Cursor c = DatabaseTableHomework.getByID(db, id);
        if(c.getCount()>0) {
            c.moveToFirst();
            description = c.getString(c.getColumnIndex(DatabaseTableHomework.FIELD_DESCRIPTION));
            shortDescription = c.getString(c.getColumnIndex(DatabaseTableHomework.FIELD_DESCRIPTION_SHORT));
            estimatedLength = c.getInt(c.getColumnIndex(DatabaseTableHomework.FIELD_ESTIMATED_LENGTH));
            scheduleTime = Calendar.getInstance();
            scheduleTime.setTimeInMillis(c.getLong(c.getColumnIndex(DatabaseTableHomework.FIELD_SCHEDULED_TIME)));
            done = c.getInt(c.getColumnIndex(DatabaseTableHomework.FIELD_DONE)) > 0;
            setLessonDue(db, c.getLong(c.getColumnIndex(DatabaseTableHomework.FIELD_LESSON_DUE)));
            setLessonSet(db, c.getLong(c.getColumnIndex(DatabaseTableHomework.FIELD_LESSON_SET)));
        }
        else {
            this.id = -1;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Lesson getLessonSet() {
        return lessonSet;
    }

    public void setLessonSet(Lesson lessonSet) {
        this.lessonSet = lessonSet;
    }

    public void setLessonSet(Database db, long lessonId){
        lessonSet = new Lesson(db, lessonId);
    }

    public void setLessonSet(Database db, long blockId, Calendar date){
        lessonSet = new Lesson(db, blockId, date);
    }

    public Lesson getLessonDue() {
        return lessonDue;
    }

    public void setLessonDue(Lesson lessonDue) {
        this.lessonDue = lessonDue;
    }

    public void setLessonDue(Database db, long lessonId){
        lessonDue = new Lesson(db, lessonId);
    }

    public void setLessonDue(Database db, long blockId, Calendar date){
        lessonDue = new Lesson(db, blockId, date);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getEstimatedLength() {
        return estimatedLength;
    }

    public void setEstimatedLength(int estimatedLength) {
        this.estimatedLength = estimatedLength;
    }

    public Calendar getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Calendar scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
