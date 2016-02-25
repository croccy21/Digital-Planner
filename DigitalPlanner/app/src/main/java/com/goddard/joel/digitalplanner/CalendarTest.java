package com.goddard.joel.digitalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
        c.setShowHomeworks(true);
        c.setShowCanceled(true);
        c.populate();
        c.setOnItemClickListener(new CalenderView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), LessonViewer.class);
                i.putExtra(LessonViewer.EXTRA_ID, id);
                startActivity(i);
            }
        });
    }
}
