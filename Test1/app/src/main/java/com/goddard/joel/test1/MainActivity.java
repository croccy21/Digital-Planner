package com.goddard.joel.test1;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private PlannerDatabase plannerDatabase;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        plannerDatabase= new PlannerDatabase(this);



        final Cursor cursor = plannerDatabase.getAllHomeworks();

        String[] columns = new String[]{
                PlannerDatabase.HOMEWORK_COLUMN_ID,
                PlannerDatabase.HOMEWORK_COLUMN_NAME,
                PlannerDatabase.HOMEWORK_COLUMN_DESCRIPTION
        };

        int [] widgets = new int[]{
                R.id.homework_id,
                R.id.homework_name,
                R.id.homework_description
        };

//        Log.d("Test1", String.format("ID: %d, NAME: %s, Description %s",
//                cursor.getInt(cursor.getColumnIndex(PlannerDatabase.HOMEWORK_COLUMN_ID)),
//                cursor.getString(cursor.getColumnIndex(PlannerDatabase.HOMEWORK_COLUMN_NAME)),
//                cursor.getString(cursor.getColumnIndex(PlannerDatabase.HOMEWORK_COLUMN_DESCRIPTION))));
        Log.d("Test1", String.format("%s, %s, %s",cursor.getColumnNames()));
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.homework_item,
                cursor, columns, widgets, 0);
        listView = (ListView) findViewById(R.id.homework_list);
        listView.setAdapter(cursorAdapter);

    }

    public void addData(View view) {
        plannerDatabase.insertHomework("Dummy", "This is not real data");
        Log.d("Test1", "Added dummy data");
        final Cursor cursor = plannerDatabase.getAllHomeworks();
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
    }
}
