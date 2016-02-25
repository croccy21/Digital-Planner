package com.goddard.joel.digitalplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

public class LessonViewer extends AppCompatActivity {

    public static String EXTRA_ID = "extra id";

    private Lesson l;
    private TextView lessonTitle;
    private TextView lessonTime;
    private Switch lessonEnabled;
    private ListView homeworksDue;
    private ListView homeworksSet;
    private Database db;
    private AdapterView.OnItemClickListener homeworkClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(getApplicationContext(), HomeworkEdit.class);
            i.putExtra(HomeworkEdit.EXTRA_ID, id);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new Database(this);
        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_ID, -1);
        if(id<0){
            l=null;
        }
        else{
            l = new Lesson(db, id);
        }

        lessonTitle = (TextView) findViewById(R.id.lesson_name);
        lessonTime = (TextView) findViewById(R.id.lesson_time);
        lessonEnabled = (Switch) findViewById(R.id.cancel_switch);
        homeworksDue = (ListView) findViewById(R.id.homework_due);
        homeworksSet = (ListView) findViewById(R.id.homework_set);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeworkEdit.class);
                i.putExtra(HomeworkEdit.EXTRA_SET_LESSON, l.getId());
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lesson_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent i = new Intent(this, BlockEdit.class);
            Log.d("DATABASE", "Id placed " + l.getBlock().getId());
            i.putExtra(BlockEdit.EXTRA_ID, l.getBlock().getId());
            startActivity(i);
        }
        if (id == R.id.action_delete){
            DatabaseTableLesson.delete(db, l.getId());
            setResult(RESULT_CANCELED);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void populate(){
        if(l!=null){
            lessonTitle.setText(getString(R.string.lesson_data_string,
                    l.getBlock().getSubject().getName(),
                    l.getBlock().getTeacher().getName(),
                    l.getBlock().getLocation().getName()));
            lessonTime.setText(getString(R.string.lesson_time_string, l.getBlock().getStartTime() / 60, l.getBlock().getStartTime() % 60,
                    l.getBlock().getEndTime() / 60, l.getBlock().getEndTime() % 60,
                    l.getBlock().getLength() / 60, l.getBlock().getLength() % 60));
            lessonEnabled.setChecked(!l.isCanceled());
            lessonEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    l.setCanceled(!isChecked);
                }
            });

            Cursor dueCursor = DatabaseTableHomework.getByLessonDue(db, l.getId());
            homeworksDue.setAdapter(new HomeworkCursorAdapter(this, dueCursor));
            homeworksDue.setOnItemClickListener(homeworkClickListener);

            Cursor setCursor = DatabaseTableHomework.getByLessonSet(db, l.getId());
            homeworksSet.setAdapter(new HomeworkCursorAdapter(this, setCursor));
            homeworksSet.setOnItemClickListener(homeworkClickListener);
        }

    }

    private class HomeworkCursorAdapter extends ArrayAdapter {


        public HomeworkCursorAdapter(Context context, Cursor c) {
            super(context, R.layout.homework_list_item);

            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                add(new Homework(db, c.getLong(c.getColumnIndex(DatabaseTableHomework.FIELD_HOMEWORK_ID))));
                c.moveToNext();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.homework_list_item, null);
            }

            Homework h = (Homework) getItem(position);
            if(h!=null){
                TextView description = (TextView) v.findViewById(R.id.long_description);
                TextView shortDescription = (TextView) v.findViewById(R.id.short_description);
                TextView length = (TextView) v.findViewById(R.id.length);
                TextView subject = (TextView) v.findViewById(R.id.subject_display);

                description.setText(h.getDescription());
                shortDescription.setText(h.getShortDescription());
                length.setText(String.format("%s days", Util.daysBetweenCalendars(h.getLessonDue().getDate(), Calendar.getInstance())));
                subject.setText(h.getLessonSet().getBlock().getSubject().getName());
            }
            return v;
        }

        @Override
        public long getItemId(int position) {
            return ((Homework)getItem(position)).getId();
        }
    }





}
