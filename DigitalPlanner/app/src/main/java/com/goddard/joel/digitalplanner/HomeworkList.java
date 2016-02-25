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
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

public class HomeworkList extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT = 1;
    private static final int REQUEST_CODE_NEW = 2;
    ListView homeworkList;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeworkEdit.class);
                startActivityForResult(i, REQUEST_CODE_NEW);
            }
        });

        db = new Database(this);

        homeworkList = (ListView) findViewById(R.id.homework_list);
        homeworkList.setAdapter(new HomeworkCursorAdapter(this, db));
        homeworkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Database", "ID: " + id);
                Intent i = new Intent(getApplicationContext(), HomeworkEdit.class);
                i.putExtra(HomeworkEdit.EXTRA_ID, id);
                startActivityForResult(i, REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homeworks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            Intent i = new Intent(this, CalendarActivity.class);
            startActivity(i);
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
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_EDIT:{
                    homeworkList.setAdapter(new HomeworkCursorAdapter(this, db));
                }
                case REQUEST_CODE_NEW:{
                    homeworkList.setAdapter(new HomeworkCursorAdapter(this, db));
                }
            }
        }
    }

    private class HomeworkCursorAdapter extends ArrayAdapter {


        public HomeworkCursorAdapter(Context context, Database db) {
            super(context, R.layout.homework_list_item);

            Cursor c = DatabaseTableHomework.getAll(db);
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
