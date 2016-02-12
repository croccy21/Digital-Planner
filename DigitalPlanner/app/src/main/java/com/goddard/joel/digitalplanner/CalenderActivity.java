package com.goddard.joel.digitalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

public class CalenderActivity extends AppCompatActivity {

    private CalenderView calenderView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        
        db = new Database(this);

        calenderView = (CalenderView) findViewById(R.id.calendar);
        calenderView.setShowCanceled(false);
        calenderView.setOnItemClickListener(new CalenderView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("id", id);
            }
        });
    }


}
