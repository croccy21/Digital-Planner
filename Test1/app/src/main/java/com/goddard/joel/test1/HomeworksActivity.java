package com.goddard.joel.test1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HomeworksActivity extends AppCompatActivity {

    private PlannerDatabase plannerDatabase;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeworks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        listView = (ListView) findViewById(R.id.homework_list);

        plannerDatabase = new PlannerDatabase(this);

        Cursor cursor = plannerDatabase.getAllHomeworks();

        String[] columns = new String[] {
                PlannerDatabase.HOMEWORK_COLUMN_ID,
                PlannerDatabase.HOMEWORK_COLUMN_NAME,
                PlannerDatabase.HOMEWORK_COLUMN_DESCRIPTION
        };

        int[] widgets = new int[] {
                R.id.homework_id,
                R.id.homework_name,
                R.id.homework_description
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.homework_item, cursor,  columns, widgets, 0);

        listView.setAdapter(cursorAdapter);

    }

    void updateList(){
        Cursor cursor = plannerDatabase.getAllHomeworks();
        ((SimpleCursorAdapter)listView.getAdapter()).changeCursor(cursor);
        listView.smoothScrollToPosition(listView.getMaxScrollAmount());
    }

    void addItem(){
        long id = plannerDatabase.insertHomework("Name", "Description");
        Intent i = new Intent(this, HomeworkEditActivity.class);
        updateList();
        startActivityForResult(i, (int) id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            String name = data.getStringExtra(HomeworkEditActivity.PARAMETER_NAME);
            String description = data.getStringExtra(HomeworkEditActivity.PARAMETER_DESCRIPTION);
            if(name!=null && description!=null){
                plannerDatabase.updateHomework(requestCode, name, description);
                updateList();
            }
        }
        else if (resultCode==RESULT_CANCELED){
            plannerDatabase.deleteHomework(requestCode);
            updateList();
        }
    }
}
