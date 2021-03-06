package com.goddard.joel.digitalplanner;

import android.database.Cursor;

/**
 * Created by Joel Goddard on 01/02/2016.
 *
 * @author Joel Goddard
 */
public class Block {
    private long id;
    private int startTime;
    private int length;
    private int day;
    private Subject subject;
    private Teacher teacher;
    private Location location;

    public Block(long id) {
        this.id = id;
    }

    public Block(long id, int startTime, int length, int day) {
        this.id = id;
        this.startTime = startTime;
        this.length = length;
        this.day = day;
    }

    public Block(Database db, long id){
        Cursor c = DatabaseTableBlock.getByID(db, id);
        c.moveToFirst();
        this.id = id;
        startTime = c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_START_TIME));
        length = c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_LENGTH));
        day = c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_DAY));
        subject = new Subject(db, c.getLong(c.getColumnIndex(DatabaseTableBlock.FIELD_SUBJECT_ID)));
        location = new Location(db, c.getLong(c.getColumnIndex(DatabaseTableBlock.FIELD_LOCATION_ID)));
        teacher = new Teacher(db, c.getLong(c.getColumnIndex(DatabaseTableBlock.FIELD_TEACHER_ID)));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getEndTime() {
        return startTime+length;
    }
}
