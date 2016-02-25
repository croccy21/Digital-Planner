package com.goddard.joel.digitalplanner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeworkEdit extends AppCompatActivity {

    private static final int REQUEST_CODE_SET = 1;
    private static final int REQUEST_CODE_DUE = 2;
    public static final String EXTRA_ID = "homework id";
    public static final String EXTRA_SET_LESSON = "lesson set id";
    private Homework homework;
    TextView subject;
    TextView setDay;
    TextView dueDay;
    EditText descriptionShort;
    EditText description;
    Switch done;
    private Database db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_edit);

        db = new Database(this);

        subject = (TextView) findViewById(R.id.subject_display);
        setDay = (TextView) findViewById(R.id.set_time);
        dueDay = (TextView) findViewById(R.id.due_time);
        description = (EditText) findViewById(R.id.long_description);
        descriptionShort = (EditText) findViewById(R.id.short_description);
        done = (Switch) findViewById(R.id.done_switch);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    done.setText("Done");
                }
                else{
                    done.setText("Not Done");
                }
                homework.setDone(isChecked);
            }
        });

        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_ID, -1);
        long lessonSetId = i.getLongExtra(EXTRA_SET_LESSON, -1);

        if(id>=0){
            homework = new Homework(db, id);
            drawLessonSet();
            drawLessonDue();
            description.setText(homework.getDescription());
            descriptionShort.setText(homework.getShortDescription());
            done.setChecked(homework.isDone());
        }
        else{
            homework = new Homework(-1);
            if(lessonSetId<0) {
                Cursor c = DatabaseTableLesson.getByCurrentDay(db);
                if (c.getCount() == 1) {
                    c.moveToFirst();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(c.getLong(c.getColumnIndex(DatabaseTableLesson.FIELD_DAY)));
                    homework.setLessonSet(new Lesson(
                            c.getLong(c.getColumnIndex(DatabaseTableLesson.FIELD_LESSON_ID)),
                            calendar,
                            new Block(db, c.getLong(c.getColumnIndex(DatabaseTableLesson.FIELD_BLOCK_ID))),
                            c.getInt(c.getColumnIndex(DatabaseTableLesson.FIELD_CANCELED)) > 0
                    ));
                    drawLessonSet();
                }
            }
            else{
                homework.setLessonSet(db, lessonSetId);
                drawLessonSet();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            switch (requestCode){
                case REQUEST_CODE_SET:{
                    homework.setLessonSet(db, data.getLongExtra(GetLessonActivity.EXTRA_LESSON_ID, -1));
                    drawLessonSet();
                    break;
                }
                case REQUEST_CODE_DUE: {
                    homework.setLessonDue(db, data.getLongExtra(GetLessonActivity.EXTRA_LESSON_ID, -1));
                    drawLessonDue();
                    break;
                }
            }
        }
    }

    private void drawLessonSet(){
        assert homework.getLessonSet()!=null;
        SimpleDateFormat df = new SimpleDateFormat(getResources().getString(R.string.short_date));
        setDay.setText(df.format(homework.getLessonSet().getDate().getTime()));
        assert homework.getLessonSet().getBlock()!=null;
        assert homework.getLessonSet().getBlock().getSubject()!=null;
        assert homework.getLessonSet().getBlock().getSubject().getName()!=null;
        subject.setText(homework.getLessonSet().getBlock().getSubject().getName());
    }

    private void drawLessonDue(){
        assert homework.getLessonDue()!=null;
        SimpleDateFormat df = new SimpleDateFormat(getResources().getString(R.string.short_date));
        dueDay.setText(df.format(homework.getLessonDue().getDate().getTime()));
    }

    public void lessonSetClick(View view) {
        Intent intent = new Intent(this, GetLessonActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SET);
    }

    public void lessonDueClick(View view) {
        Intent intent = new Intent(this, GetLessonActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DUE);
    }

    public void saveBtnClick(View view) {
        Intent i = new Intent();
        if(homework.getLessonSet()==null || homework.getLessonDue()==null){
            return;
        }

        homework.setDescription(description.getText().toString());
        homework.setShortDescription(descriptionShort.getText().toString());

        if(homework.getDescription().isEmpty() || homework.getShortDescription().isEmpty()){
            return;
        }

        if(homework.getId()<0){
            long id = DatabaseTableHomework.insert(db,
                    homework.getLessonSet().getId(),
                    homework.getLessonDue().getId(),
                    homework.getEstimatedLength(),
                    homework.getScheduleTime(),
                    homework.getDescription(),
                    homework.getShortDescription(),
                    homework.isDone()
            );
            homework.setId(id);
        }
        else{
            DatabaseTableHomework.update(db, homework.getId(),
                    homework.getLessonSet().getId(),
                    homework.getLessonDue().getId(),
                    homework.getEstimatedLength(),
                    homework.getScheduleTime(),
                    homework.getDescription(),
                    homework.getShortDescription(),
                    homework.isDone()
            );
        }
        i.putExtra(EXTRA_ID, homework.getId());
        setResult(RESULT_OK, i);
        finish();
    }

    public void cancelBtnClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
