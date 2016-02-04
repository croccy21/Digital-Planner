package com.goddard.joel.digitalplanner;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BlockEdit extends AppCompatActivity {

    public static final String EXTRA_ID= "id";
    public static final String EXTRA_START_TIME = "start time";
    public static final String EXTRA_LENGTH = "length";
    public static final String EXTRA_DAY = "day";

    private int id = -1;
    private int startTime = -1;
    private int endTime = -1;
    private int length = -1;
    private Spinner spinner;
    private TextView startBox;
    private TextView endBox;
    private String day = "";
    private Database db;
    private Spinner subjectSpinner;
    private Spinner teacherSpinner;
    private Spinner locationSpinner;

    private Subject[] allSubjects;
    private Subject currentSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_edit);

        db = new Database(getApplicationContext());

        //get widgets from block_edit.xml
        startBox = (TextView) findViewById(R.id.block_start_time);
        endBox = (TextView) findViewById(R.id.block_end_time);
        spinner = (Spinner) findViewById(R.id.block_day_picker);
        subjectSpinner = (Spinner) findViewById(R.id.block_subject);
        teacherSpinner = (Spinner) findViewById(R.id.block_teacher);
        locationSpinner = (Spinner) findViewById(R.id.block_location);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Get ID passed from previous activity
        Intent intent = getIntent();
        id = intent.getIntExtra(EXTRA_ID, -1);

        //If using existing block populate from database
        if (id>=0){
            //Get data from database
            Cursor c = DatabaseTableBlock.getByID(db, id);
            startTime = c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_START_TIME));
            length = c.getInt(c.getColumnIndex(DatabaseTableBlock.FIELD_LENGTH));
            endTime=startTime+length;
            day=c.getString(c.getColumnIndex(DatabaseTableBlock.FIELD_DAY));

            //Populate widgets
            startBox.setText(String.format("%02d:%02d", startTime/60, startTime%60));
            endBox.setText(String.format("%02d:%02d", endTime/60, endTime%60));
            int spinnerPosition = adapter.getPosition(day);
            if (spinnerPosition>=0 && spinnerPosition<adapter.getCount()){
                spinner.setSelection(spinnerPosition);
            }
        }
        populateSubjectSpinner();
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    currentSubject = allSubjects[position - 1];
                    currentSubject.setSubjectTeacherFromDatabase(db);
                    currentSubject.setSubjectLocationFromDatabase(db);
                    populateLocationSpinner(currentSubject.getSubjectLocationNames());
                    populateTeacherSpinner(currentSubject.getSubjectTeacherNames());
                } else {
                    currentSubject = null;
                    populateTeacherSpinner(new ArrayList<String>());
                    populateLocationSpinner(new ArrayList<String>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void populateSubjectSpinner(){
        Cursor c = DatabaseTableSubject.getAll(db);
        Log.d("DATABASE", String.valueOf(c.getCount()));
        allSubjects = new Subject[c.getCount()];
        String[] names = new String[c.getCount()+1];
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            Log.d("DATABASE", String.valueOf(i));
            allSubjects[i]=new Subject(c.getLong(c.getColumnIndex(DatabaseTableSubject.FIELD_SUBJECT_ID)),
                    c.getString(c.getColumnIndex(DatabaseTableSubject.FIELD_NAME)));
            names[i+1]=allSubjects[i].getName();
            Log.d("DATABASE", allSubjects[i].getName());
            c.moveToNext();
        }
        names[0]="NO SUBJECT";
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
    }

    private void populateTeacherSpinner(List<String> teachers){
        teachers.add(0, "NO TEACHER");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teachers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(adapter);
    }

    private void populateLocationSpinner(List<String> locations){
        locations.add(0, "NO LOCATION");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    public void startTimeClick(View view) {
        Calendar c = Calendar.getInstance();
        if(startTime!=-1){
            c.set(Calendar.HOUR_OF_DAY, startTime/60);
            c.set(Calendar.MINUTE, startTime%60);
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime = hourOfDay*60+minute;
                length = endTime - startTime;
                Button b = (Button) findViewById(R.id.block_start_time);
                b.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void endTimeClick(View view){
        Calendar c = Calendar.getInstance();
        if(endTime!=-1){
            c.set(Calendar.HOUR_OF_DAY, endTime/60);
            c.set(Calendar.MINUTE, endTime%60);
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTime = hourOfDay*60+minute;
                length = endTime - startTime;
                Button b = (Button) findViewById(R.id.block_end_time);
                b.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void saveBtnClick(View view) {
        long subjectId;
        long currentTeacher;
        long currentLocation;

        if(currentSubject==null){
            return;
        }
        subjectId=currentSubject.getId();
        Log.d("DATABASE", String.valueOf(subjectId));

        int teacherPos = teacherSpinner.getSelectedItemPosition() - 1;
        if(teacherPos<0){
            return;
        }
        currentTeacher = currentSubject.getSubjectTeachers().get(teacherPos).getId();
        Log.d("DATABASE", String.valueOf(currentTeacher));

        int locationPos = locationSpinner.getSelectedItemPosition() - 1;
        if(locationPos<0){
            return;
        }
        currentLocation = currentSubject.getSubjectLocations().get(locationPos).getId();
        Log.d("DATABASE", String.valueOf(currentLocation));


        day = (String) spinner.getSelectedItem();
        int dayID;

        if (day.equals("Monday")){
            dayID = Calendar.MONDAY;
        }
        else if (day.equals("Tuesday")){
            dayID = Calendar.TUESDAY;
        }
        else if (day.equals("Wednesday")){
            dayID = Calendar.WEDNESDAY;
        }
        else if (day.equals("Thursday")){
            dayID = Calendar.THURSDAY;
        }
        else if (day.equals("Friday")){
            dayID = Calendar.FRIDAY;
        }
        else if (day.equals("Saturday")){
            dayID = Calendar.SATURDAY;
        }
        else if (day.equals("Sunday")){
            dayID = Calendar.SUNDAY;
        }
        else{
            return;
        }

        if(startTime<0 || endTime<0 || length<0){
            return;
        }

        DatabaseTableBlock.insert(db, dayID, startTime, length, subjectId, currentTeacher, currentLocation);

        Intent result = new Intent();
        result.putExtra(EXTRA_ID, id);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public void cancelBtnClick(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
