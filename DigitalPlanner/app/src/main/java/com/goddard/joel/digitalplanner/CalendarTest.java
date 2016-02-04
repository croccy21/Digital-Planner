package com.goddard.joel.digitalplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class CalendarTest extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);
        db = new Database(this);

        CalenderView c = (CalenderView) findViewById(R.id.calendar);
        c.setDb(db);
        c.populate();
    }
}
