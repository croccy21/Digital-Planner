package com.goddard.joel.test1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HomeworksActivity extends AppCompatActivity {

    private PlannerDatabase plannerDatabase;
    private ListView listView;
    private TextView textName;
    private TextView textDescription;

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

        Cursor cursor = plannerDatabase.getAllHomeworks(PlannerDatabase.HOMEWORK_COLUMN_ID, PlannerDatabase.HOMEWORK_COLUMN_NAME, PlannerDatabase.HOMEWORK_COLUMN_DESCRIPTION);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView listView, View view, int position, long id) {
                Cursor itemCursor = (Cursor) listView.getItemAtPosition(position);

                textName = ((TextView) view.findViewById(R.id.homework_name));
                textDescription = ((TextView)view.findViewById(R.id.homework_description));

                String transitionName = getResources().getText(R.string.transition_homework_name).toString();
                String transitionDescription = getResources().getText(R.string.transition_homework_description).toString();
                textName.setTransitionName(transitionName);
                textDescription.setTransitionName(transitionDescription);

                String name = textName.getText().toString();
                String description = textDescription.getText().toString();

                Intent i = new Intent(getApplicationContext(), HomeworkEditActivity.class);
                i.putExtra(HomeworkEditActivity.PARAMETER_ID, (int)id);
                i.putExtra(HomeworkEditActivity.PARAMETER_NAME, name);
                i.putExtra(HomeworkEditActivity.PARAMETER_DESCRIPTION, description);
                ActivityOptionsCompat  options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeworksActivity.this,
                        Pair.create((View) textName, transitionName),
                        Pair.create((View) textDescription, transitionDescription));
                startActivityForResult(i, 3, options.toBundle());
            }
        });

    }

    void updateList(){
        Cursor cursor = plannerDatabase.getAllHomeworks(PlannerDatabase.HOMEWORK_COLUMN_ID, PlannerDatabase.HOMEWORK_COLUMN_NAME, PlannerDatabase.HOMEWORK_COLUMN_DESCRIPTION);
        ((SimpleCursorAdapter)listView.getAdapter()).changeCursor(cursor);
        listView.smoothScrollToPosition(listView.getMaxScrollAmount());
    }

    void addItem(){
        long id = plannerDatabase.insertHomework("Name", "Description");
        Intent i = new Intent(this, HomeworkEditActivity.class);
        i.putExtra(HomeworkEditActivity.PARAMETER_ID, (int) id);
        updateList();
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2||requestCode==3) {
            if (textName!=null){
                textName.setTransitionName(null);
            }
            if (textDescription!=null){
                textDescription.setTransitionName(null);
            }
            int id = data.getIntExtra(HomeworkEditActivity.PARAMETER_ID, -1);
            if (id>=0) {
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra(HomeworkEditActivity.PARAMETER_NAME);
                    String description = data.getStringExtra(HomeworkEditActivity.PARAMETER_DESCRIPTION);
                    if (name != null && description != null) {
                        plannerDatabase.updateHomework(id, name, description);
                        updateList();
                    }
                } else if (resultCode == RESULT_CANCELED && requestCode==2) {
                    plannerDatabase.deleteHomework(id);
                    updateList();
                }
            }
        }
    }
}
