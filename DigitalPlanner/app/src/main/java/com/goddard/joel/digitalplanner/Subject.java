package com.goddard.joel.digitalplanner;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel Goddard on 28/01/2016.
 *
 * @author Joel Goddard
 */
public class Subject {
    private long id;
    private String name;
    private int colourValue;

    private ArrayList<Teacher> subjectTeachers = new ArrayList<Teacher>();
    private ArrayList<Location> subjectLocations = new ArrayList<Location>();

    public Subject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(Database db, long id){
        this.id = id;
        Log.d("Database", String.valueOf(id));
        Cursor c = DatabaseTableSubject.getByID(db, id);
        c.moveToFirst();
        name = c.getString(c.getColumnIndex(DatabaseTableSubject.FIELD_NAME));
        colourValue = c.getInt(c.getColumnIndex((DatabaseTableSubject.FIELD_COLOUR)));
        setSubjectTeacherFromDatabase(db);
        setSubjectLocationFromDatabase(db);
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColourValue() {
        return colourValue;
    }

    public void setColourValue(int colourValue) {
        this.colourValue = colourValue;
    }

    public ArrayList<Teacher> getSubjectTeachers() {
        return subjectTeachers;
    }

    public List<String> getSubjectTeacherNames(){
        ArrayList<String> strings = new ArrayList<String>();
        for(Teacher t:subjectTeachers){
            strings.add(t.getName());
        }
        return strings;
    }

    public void setSubjectTeachers(ArrayList<Teacher> subjectTeachers) {
        this.subjectTeachers = subjectTeachers;
    }

    public ArrayList<Location> getSubjectLocations() {
        return subjectLocations;
    }

    public List<String> getSubjectLocationNames() {
        ArrayList<String> strings = new ArrayList<String>();
        for(Location l:subjectLocations){
            strings.add(l.getName());
        }
        return strings;
    }

    public void setSubjectLocations(ArrayList<Location> subjectLocations) {
        this.subjectLocations = subjectLocations;
    }

    public void setSubjectTeacherFromDatabase(Database db){
        Cursor teachers = DatabaseTableSubjectTeacher.getAllBySubjectIDWithTeacher(db, id);
        subjectTeachers.clear();
        teachers.moveToFirst();
        for(int i=0; i<teachers.getCount(); i++){
            subjectTeachers.add(new Teacher(
                    teachers.getLong(teachers.getColumnIndex(DatabaseTableSubjectTeacher.FIELD_TEACHER_ID)),
                    teachers.getString(teachers.getColumnIndex(DatabaseTableTeacher.FIELD_NAME))));
            teachers.moveToNext();
        }
    }

    public void setSubjectLocationFromDatabase(Database db){
        Cursor locations = DatabaseTableSubjectLocation.getAllBySubjectIDWithLocation(db, id);
        subjectLocations.clear();
        locations.moveToFirst();
        for(int i=0; i<locations.getCount(); i++){
            subjectLocations.add(new Location(locations.getLong(locations.getColumnIndex(DatabaseTableSubjectLocation.FIELD_LOCATION_ID)),
                    locations.getString(locations.getColumnIndex(DatabaseTableLocation.FIELD_NAME))));
            locations.moveToNext();
        }
    }
}
