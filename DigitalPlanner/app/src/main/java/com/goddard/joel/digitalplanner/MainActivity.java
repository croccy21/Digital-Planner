package com.goddard.joel.digitalplanner;

import android.app.Activity;
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
            return true;
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
        Intent intent = new Intent(this, CalendarTest.class);
        startActivity(intent);
    }
}
