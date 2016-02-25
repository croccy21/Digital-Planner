package com.goddard.joel.digitalplanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_BLOCK = 1;
    private static final int REQUEST_CODE_SUBJECT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //DatabaseTest.test(this);

        Intent i = new Intent(this, DailyService.class);
        i.setAction(DailyService.ACTION_SETUP);
        startService(i);
        Calendar c = Calendar.getInstance();
        c = Util.setDateToStart(c);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.MINUTE, 5);
        PendingIntent pi = PendingIntent.getService(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pi);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_BLOCK){
            if(resultCode== Activity.RESULT_OK){
                int start = data.getIntExtra(BlockEdit.EXTRA_START_TIME, -1);
                int length = data.getIntExtra(BlockEdit.EXTRA_LENGTH, -1);
                String day = data.getStringExtra(BlockEdit.EXTRA_DAY);
                Log.d("Return Data", String.format("Start time: %s, length: %s, day %s", start, length, day));
            }
        }

    }

    public void blockTestClick(View view) {
        Intent intent = new Intent(this, BlockEdit.class);
        intent.putExtra(BlockEdit.EXTRA_START_TIME, 1040);
        intent.putExtra(BlockEdit.EXTRA_LENGTH, 65);
        intent.putExtra(BlockEdit.EXTRA_DAY, "Tuesday");
        startActivityForResult(intent, REQUEST_CODE_BLOCK);
    }

    public void subjectTestClick(View view) {
        Intent intent = new Intent(this, SubjectEdit.class);
        startActivityForResult(intent, REQUEST_CODE_SUBJECT);
    }

    public void homeworkTestClick(View view) {
        Intent intent = new Intent(this, HomeworkList.class);
        startActivity(intent);
    }

    public void calendarTestClick(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void notificationTestClick(View view) {
        Database db = new Database(this);
        Block b = new Block(0, 1000, 60, Calendar.MONDAY);
        b.setSubject(new Subject(0, "Maths"));
        b.setTeacher(new Teacher(0, "Steve"));
        b.setLocation(new Location(0, "A004"));
        Lesson l = new Lesson(0, Util.setDateToStart(Calendar.getInstance()), b, false);
        Homework h = new Homework(db, 1);
        NewLessonNotification.notify(getApplicationContext(), l, h, 1);
    }
}
