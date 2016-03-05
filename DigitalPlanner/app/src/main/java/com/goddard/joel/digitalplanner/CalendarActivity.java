package com.goddard.joel.digitalplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REDRAW = 1;
    private Database db;
    private CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//Event called when activity created
        //DatabaseTest.test(this);
        super.onCreate(savedInstanceState);//run parent method
        setContentView(R.layout.activity_calendar);//set the layout to display
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//set up toolbar with actions

        db = new Database(this);//get/create database

        calendar = (CalendarView) findViewById(R.id.calendar);//get CalendarView layout
        calendar.setDb(db);
        calendar.setShowHomeworks(true);
        calendar.setShowCanceled(true);
        calendar.populate();//setup calendar to dispaly cards
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), LessonViewer.class);
                i.putExtra(LessonViewer.EXTRA_ID, id);
                startActivityForResult(i, REQUEST_CODE_REDRAW);//open LessonViewer activity to redraw when returned
            }
        });//set up what happens when a card is clicked

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BlockEdit.class);
                i.putExtra(BlockEdit.EXTRA_DAY, calendar.getDay());
                startActivityForResult(i, REQUEST_CODE_REDRAW);//open BlockEdit activity to redraw when returned
            }
        });//set up what happens when floating action button pressed

        //Run notification setup
        Intent i = new Intent(this, DailyService.class);
        i.setAction(DailyService.ACTION_SETUP);
        Calendar c = Calendar.getInstance();//get current time
        c = Util.setDateToStart(c);//set time to start of day
        c.add(Calendar.DAY_OF_YEAR, 1);// set time to tomorrow
        c.set(Calendar.MINUTE, 5);//set time to 5 minutes past midnight
        PendingIntent pi = PendingIntent.getService(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);//create intent to happen later
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pi);//cancel existing pending intent
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);//schedule pending intent for 5 minutes past midnight tommorow
        startService(i);//run service now to setup notifications for today
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create toolbar with buttons
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //when toolbar button pressed this is run
        int id = item.getItemId();

        if (id == R.id.action_homework) {//homework button
            Intent i = new Intent(this, HomeworkList.class);
            startActivityForResult(i, REQUEST_CODE_REDRAW);//open HomeworkList activity to redraw when returned
        }
        if(id == R.id.action_subjects){//subject manage button
            Intent i = new Intent(this, SubjectManager.class);
            startActivityForResult(i, REQUEST_CODE_REDRAW);//open SubjectManager activity to redraw when returned
        }
        if (id == R.id.action_settings){//settings button
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);//open SettingsActivity activity with no result
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//run when receiving result of activity
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_REDRAW){//if activity created to redraw on return
            calendar.populate();//redraw CalendarView
        }
    }
}
