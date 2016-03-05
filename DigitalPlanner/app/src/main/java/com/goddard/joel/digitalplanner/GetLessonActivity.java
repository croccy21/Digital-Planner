package com.goddard.joel.digitalplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class GetLessonActivity extends AppCompatActivity {

    public static final String EXTRA_LESSON_ID = "lesson id";
    private Database db;
    private CalendarView calenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_lesson);

        db = new Database(this);

        calenderView = (CalendarView) findViewById(R.id.calendar);
        calenderView.setDb(db);
        calenderView.populate();
        calenderView.setShowCanceled(false);
        calenderView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra(EXTRA_LESSON_ID, id);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
