package com.goddard.joel.digitalplanner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SubjectEdit extends AppCompatActivity {

    private static final int SELECTION_BUTTON = 0;
    private static final int SELECTION_ADD = 1;
    private static final int SELECTION_NEW = 2;
    private static final int TYPE_TEACHER = 0;
    private static final int TYPE_LOCATION = 1;

    public static final String EXTRA_ID = "id";

    int type = 0;

    private LinearLayout buttonHolder;
    private LinearLayout addItemHolder;
    private LinearLayout newItemHolder;

    private Spinner itemSpinner;
    private EditText newItemEdit;

    private EditText subjectNameEdit;


    private Teacher[] allTeachers;
    private ArrayList<Teacher> subjectTeachers = new ArrayList<Teacher>();
    private ArrayList<Teacher> teachersRemaining = new ArrayList<Teacher>();

    private Location[] allLocations;
    private ArrayList<Location> subjectLocations = new ArrayList<Location>();
    private ArrayList<Location> locationsRemaining = new ArrayList<Location>();
    private Database db;

    private ListView teacherList;
    public ArrayAdapter<String> teacherAdapter;
    private ListView locationList;
    public ArrayAdapter<String> locationAdapter;
    private long id;
    private int selection=SELECTION_BUTTON;

    //TODO Custom Array Adapter for the this view


    /**
     * Run when view is created
     * @param savedInstanceState required for super
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_edit);
        
        db = new Database(getApplicationContext());

        Intent intent = getIntent();
        id = intent.getLongExtra(EXTRA_ID, -1);

        //Gets all of the widgets from activity_subject_edit.xml
        buttonHolder = (LinearLayout) findViewById(R.id.button_holder);
        addItemHolder = (LinearLayout) findViewById(R.id.add_item_holder);
        newItemHolder = (LinearLayout) findViewById(R.id.new_item_holder);
        itemSpinner = (Spinner) findViewById(R.id.add_spinner);
        newItemEdit = (EditText) findViewById(R.id.new_item_edit);
        subjectNameEdit = (EditText) findViewById(R.id.subject_name_edit);

        teacherList = (ListView) findViewById(R.id.subject_teacher_list);
        locationList = (ListView) findViewById(R.id.subject_location_list);

        //Gets data from existing subject if an ID is passed to the layout
        if(id!=-1) {
            DatabaseTableSubject.getByID(db, id);
            Cursor teachers = DatabaseTableSubjectTeacher.getAllBySubjectIDWithTeacher(db, id);
            teachers.moveToFirst();
            for(int i=0; i<teachers.getCount(); i++){
                subjectTeachers.add(new Teacher(teachers.getLong(teachers.getColumnIndex(DatabaseTableSubjectTeacher.FIELD_TEACHER_ID)),
                        teachers.getString(teachers.getColumnIndex(DatabaseTableTeacher.FIELD_NAME))));
                teachers.moveToNext();
            }
            Cursor locations = DatabaseTableSubjectLocation.getAllBySubjectIDWithLocation(db, id);
            locations.moveToFirst();
            for(int i=0; i<locations.getCount(); i++){
                subjectLocations.add(new Location(locations.getLong(locations.getColumnIndex(DatabaseTableSubjectLocation.FIELD_LOCATION_ID)),
                        locations.getString(locations.getColumnIndex(DatabaseTableLocation.FIELD_NAME))));
                locations.moveToNext();
            }
            Cursor c = DatabaseTableSubject.getByID(db, id);
            c.moveToFirst();
            if(c.getCount()==1) {
                String name = c.getString(c.getColumnIndex(DatabaseTableSubject.FIELD_NAME));
                subjectNameEdit.setText(name);
            }
        }

        //Populates allTeachers array from database
        Cursor cT = DatabaseTableTeacher.getAll(db);
        allTeachers = new Teacher[cT.getCount()];
        cT.moveToFirst();
        for(int i=0; i<cT.getCount(); i++){
            allTeachers[i] = new Teacher(cT.getLong(  cT.getColumnIndex(DatabaseTableTeacher.FIELD_TEACHER_ID)),
                    cT.getString(cT.getColumnIndex(DatabaseTableTeacher.FIELD_NAME)));
            cT.move(1);
        }

        //Populates allLocations array from database
        Cursor cL = DatabaseTableLocation.getAll(db);
        allLocations = new Location[cL.getCount()];
        cL.moveToFirst();
        for(int i=0; i<cL.getCount(); i++){
            allLocations[i] = new Location(cL.getLong(  cL.getColumnIndex(DatabaseTableLocation.FIELD_LOCATION_ID)),
                    cL.getString(cT.getColumnIndex(DatabaseTableLocation.FIELD_NAME)));
            cL.move(1);
        }

        teacherAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, packTeacher(subjectTeachers));
        teacherList.setAdapter(teacherAdapter);

        locationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, packLocation(subjectLocations));
        locationList.setAdapter(locationAdapter);

        //Checks when NEW [TEACHER/LOCATION] is selected and goes to selection new
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case TYPE_TEACHER:{
                        if(position==teachersRemaining.size()){
                            setSelection(SELECTION_NEW);
                        }
                        break;
                    }
                    case TYPE_LOCATION:{
                        if(position==locationsRemaining.size()){
                            setSelection(SELECTION_NEW);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Packs the teacher array into a string array
     * @param teachers list of teachers
     * @return array containing teacher names
     */
    private String[] packSpecialTeachers(ArrayList<Teacher> teachers){
        ArrayList<String> strings = new ArrayList<String>();
        for(Teacher t:teachers){
            strings.add(t.getName());
        }
        strings.add("NEW TEACHER");
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Packs a list of teachers into a list of strings
     * @param teachers array of teachers
     * @return a list of strings contianng teacher names
     */
    private static List<String> packTeacher(ArrayList<Teacher> teachers){
        ArrayList<String> strings = new ArrayList<String>();
        for(Teacher t:teachers){
            strings.add(t.getName());
        }
        return strings;
    }

    /**
     * Packs an array of locations into an array of location names
     * @param locations array of locations
     * @return array of location names as strings
     */
    private String[] packSpecialLocations(ArrayList<Location> locations){
        ArrayList<String> strings = new ArrayList<String>();
        for(Location l:locations){
            strings.add(l.getName());
        }
        strings.add("NEW LOCATION");
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Packs an array of locations into a list containing the location names
     * @param locations array of locations
     * @return list of location names as strings
     */
    private static List<String> packLocation(ArrayList<Location> locations){
        ArrayList<String> strings = new ArrayList<String>();
        for(Location l:locations){
            strings.add(l.getName());
        }
        return strings;
    }

    /**
     * This procedure updates the spinner containing the teachers (same spinner as for locations)
     * Updating takes the list of all teachers created and removes teachers already added
     * Then the pack special function is called which returns a list of strings with the teacher names
     * as well as an extra entry for NEW TEACHER
     * This will remove the locations from the spinner
     */
    private void updateTeachers(){
        Cursor cT = DatabaseTableTeacher.getAll(db);
        allTeachers = new Teacher[cT.getCount()];
        teachersRemaining.clear();
        cT.moveToFirst();
        for(int i=0; i<cT.getCount(); i++){
            allTeachers[i] = new Teacher(cT.getLong(  cT.getColumnIndex(DatabaseTableTeacher.FIELD_TEACHER_ID)),
                    cT.getString(cT.getColumnIndex(DatabaseTableTeacher.FIELD_NAME)));

            boolean inArray=false;
            int j=0;
            while (!inArray && j < subjectTeachers.size()){
                if(subjectTeachers.get(j).getId()==allTeachers[i].getId()){
                    inArray=true;
                }
                j++;
            }
            if(!inArray){
                teachersRemaining.add(allTeachers[i]);
            }
            cT.move(1);
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, packSpecialTeachers(teachersRemaining));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        itemSpinner.setAdapter(adapter);
    }

    /**
     * This procedure updates the spinner containing the locations (same spinner as for teachers)
     * Updating takes the list of all locations created and removes locations already added
     * Then the pack special function is called which returns a list of strings with the location names
     * as well as an extra entry for NEW LOCATION
     * This will remove the teachers from the spinner
     */
    private void updateLocations(){
        Cursor cL = DatabaseTableLocation.getAll(db);
        allLocations = new Location[cL.getCount()];
        locationsRemaining.clear();
        cL.moveToFirst();
        for(int i=0; i<cL.getCount(); i++){
            allLocations[i] = new Location(cL.getLong(  cL.getColumnIndex(DatabaseTableLocation.FIELD_LOCATION_ID)),
                    cL.getString(cL.getColumnIndex(DatabaseTableTeacher.FIELD_NAME)));
            boolean inArray=false;
            int j=0;
            while (!inArray && j < subjectLocations.size()){
                if(subjectLocations.get(j).getId()==allLocations[i].getId()){
                    inArray=true;
                }
                j++;
            }
            if(!inArray){
                locationsRemaining.add(allLocations[i]);
            }
            cL.move(1);
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_item, packSpecialLocations(locationsRemaining));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        itemSpinner.setAdapter(adapter);
    }

    /**
     * Switches between the different input layouts:
     *  SELECTION_BUTTON (default) Buttons to choose to add teacher or add location
     *  SELECTION_ADD Spinner to select the teacher/location or choose to add a new one
     *  SELECTION_NEW TextEdit to input the teachers name
     * @param selection which state to go to
     */
    void setSelection(int selection){
        this.selection = selection;
        switch (selection){
            case SELECTION_BUTTON:{
                buttonHolder.setVisibility(View.VISIBLE);
                addItemHolder.setVisibility(View.GONE);
                newItemHolder.setVisibility(View.GONE);
                break;
            }
            case SELECTION_ADD:{
                buttonHolder.setVisibility(View.GONE);
                addItemHolder.setVisibility(View.VISIBLE);
                newItemHolder.setVisibility(View.GONE);
                break;
            }
            case SELECTION_NEW:{
                buttonHolder.setVisibility(View.GONE);
                addItemHolder.setVisibility(View.GONE);
                newItemHolder.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    /**
     * Switches between teacher and location addition
     * This calls the relevant update either updateTeachers or updateLocations
     * @param type teacher/location
     */
    public void setType(int type){
        this.type=type;
        switch (type){
            case TYPE_TEACHER:{
                newItemEdit.setHint("Teacher Name (e.g. Mr Smith)");
                updateTeachers();
                break;
            }
            case TYPE_LOCATION:{
                newItemEdit.setHint("Location (e.g. X007)");
                updateLocations();
                break;
            }
        }
    }

    /**
     * Adds a teacher to the teacher {@link ListView}
     * @param t teacher
     */
    public void addTeacherToList(Teacher t){
        subjectTeachers.add(t);
        teacherAdapter.insert(t.getName(), teacherAdapter.getCount());
        teacherAdapter.notifyDataSetChanged();
    }

    /**
     * Adds a location to the location {@link ListView}
     * @param l location
     */
    public void addLocationToList(Location l){
        subjectLocations.add(l);
        locationAdapter.insert(l.getName(), locationAdapter.getCount());
        locationAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the add teacher button is clicked
     * Sets selection to add and type to teacher
     * @param view required parameter
     */
    public void addTeacherClick(View view) {
        setSelection(SELECTION_ADD);
        setType(TYPE_TEACHER);
    }

    /**
     * Called when the add location button is clicked
     * Sets selection to add and type to location
     * @param view required parameter
     */
    public void addLocationClick(View view) {
        setSelection(SELECTION_ADD);
        setType(TYPE_LOCATION);
    }

    /**
     * Called when the done button is pressed on the add selection
     * Adds a [teacher/location] to the respective list and sets selection to button
     * Unless NEW [item] is selected, in which case it sets selection to new
     * @param view required parameter
     */
    public void addItemDone(View view) {
        int pos = itemSpinner.getSelectedItemPosition();
        switch (type) {
            case TYPE_TEACHER: {
                if(pos<teachersRemaining.size()) {
                    addTeacherToList(teachersRemaining.get(pos));
                    setSelection(SELECTION_BUTTON);
                }
                else {
                    setSelection(SELECTION_NEW);
                }
                break;
            }
            case TYPE_LOCATION: {
                if(pos<locationsRemaining.size()) {
                    addLocationToList(locationsRemaining.get(pos));
                    setSelection(SELECTION_BUTTON);
                }
                else{
                    setSelection(SELECTION_NEW);
                }
                break;
            }
        }
    }

    /**
     * Called when the done button is pressed on the new selection
     * Adds new [teacher/location] to database and adds the new [teacher/location] to respective list
     * Also calls the respective update function to update the spinner
     * @param view required parameter
     */
    public void newItemDone(View view) {
        String text = newItemEdit.getText().toString();
        if(!text.isEmpty()) {
            switch (type) {
                case TYPE_TEACHER: {
                    DatabaseTableTeacher.insert(db, text);
                    updateTeachers();
                    addTeacherToList(teachersRemaining.get(teachersRemaining.size() - 1));
                    break;
                }
                case TYPE_LOCATION: {
                    DatabaseTableLocation.insert(db, text);
                    updateLocations();
                    addLocationToList(locationsRemaining.get(locationsRemaining.size() - 1));
                    break;
                }
            }
            setSelection(SELECTION_BUTTON);
        }
        newItemEdit.setText("");
    }

    /**
     * Called when save button is clicked
     * Will add this subject to the database and then exit the activity with result code OK
     * and the id will be returned to the previous activity
     * @param view required parameter
     */
    public void saveBtnClick(View view) {
        if(id==-1) {
            id = DatabaseTableSubject.insert(db, subjectNameEdit.getText().toString(), 0);
        }
        else{
            DatabaseTableSubject.update(db, id, subjectNameEdit.getText().toString(), 0);
        }
        DatabaseTableSubjectTeacher.deleteBySubject(db, id);
        for(Teacher t:subjectTeachers){
            DatabaseTableSubjectTeacher.insert(db, id, t.getId());
            Log.d("DATABASE", String.format("Added %1$s to %2$s", t.getName(), id));
        }
        DatabaseTableSubjectLocation.deleteBySubject(db, id);
        for(Location l:subjectLocations){
            DatabaseTableSubjectLocation.insert(db, id, l.getId());
        }
        Intent result = new Intent();
        result.putExtra(EXTRA_ID, id);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    /**
     * Called when cancel button to clicked
     * Exits activity with result code CANCELED
     * @param view required parameter
     */
    public void cancelBtnClick(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /**
     * Overrides back button functionality
     *
     * If selection is button then calls the super to exit activity with result code canceled
     * If selection is add then goes to selection button
     * If selection is new then goes to selection add
     */
    @Override
    public void onBackPressed() {
        switch(selection){
            case SELECTION_BUTTON:{
                super.onBackPressed();
                break;
            }
            case SELECTION_ADD:{
                setSelection(SELECTION_BUTTON);
                break;
            }
            case SELECTION_NEW:{
                setSelection(SELECTION_ADD);
                break;
            }
        }
    }

    public void delete(View view) {
        if(id>0){
            DatabaseTableSubject.delete(db, id);
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
