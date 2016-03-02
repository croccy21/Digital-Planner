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
    private CalenderView c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseTest.test(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new Database(this);

        c = (CalenderView) findViewById(R.id.calendar);
        c.setDb(db);
        c.setShowHomeworks(true);
        c.setShowCanceled(true);
        c.populate();
        c.setOnItemClickListener(new CalenderView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), LessonViewer.class);
                i.putExtra(LessonViewer.EXTRA_ID, id);
                startActivityForResult(i, REQUEST_CODE_REDRAW);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BlockEdit.class);
                i.putExtra(BlockEdit.EXTRA_DAY, c.getDay());
                startActivityForResult(i, REQUEST_CODE_REDRAW);
            }
        });

        Intent i = new Intent(this, DailyService.class);
        i.setAction(DailyService.ACTION_SETUP);
        Calendar c = Calendar.getInstance();
        c = Util.setDateToStart(c);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.MINUTE, 5);
        PendingIntent pi = PendingIntent.getService(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pi);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        startService(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_homework) {
            Intent i = new Intent(this, HomeworkList.class);
            startActivityForResult(i, REQUEST_CODE_REDRAW);
        }
        if(id == R.id.action_subjects){
            Intent i = new Intent(this, SubjectList.class);
            startActivityForResult(i, REQUEST_CODE_REDRAW);
        }
        if (id == R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_REDRAW){
            c.populate();
        }
    }
}
