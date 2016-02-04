package com.goddard.joel.digitalplanner;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Joel Goddard on 30/01/2016.
 *
 * @author Joel Goddard
 */
public class CalenderAdapter extends ArrayAdapter {

    private Database db;

    public CalenderAdapter(Context context, Database db, Calendar calendar) {
        super(context, R.layout.calendar_block);
        this.db = db;
        Cursor blocks = DatabaseTableBlock.getByDay(db, calendar.get(Calendar.DAY_OF_WEEK));
        blocks.moveToFirst();
        Log.d("DATABASE", String.valueOf(blocks.getCount()));
        for(int i =0; i<blocks.getCount();i++){
            Block b = new Block(blocks.getLong(blocks.getColumnIndex(DatabaseTableBlock.FIELD_BLOCK_ID)));
            b.setStartTime(blocks.getInt(blocks.getColumnIndex(DatabaseTableBlock.FIELD_START_TIME)));
            b.setLength(blocks.getInt(blocks.getColumnIndex(DatabaseTableBlock.FIELD_LENGTH)));
            b.setDay(blocks.getInt(blocks.getColumnIndex(DatabaseTableBlock.FIELD_DAY)));
            b.setSubject(new Subject(db, blocks.getLong(blocks.getColumnIndex(DatabaseTableBlock.FIELD_SUBJECT_ID))));
            b.setLocation(new Location(db, blocks.getLong(blocks.getColumnIndex(DatabaseTableBlock.FIELD_LOCATION_ID))));
            b.setTeacher(new Teacher(db, blocks.getLong(blocks.getColumnIndex(DatabaseTableBlock.FIELD_TEACHER_ID))));
            add(b);
            Log.d("DATABASE", String.valueOf(b.getId()));
            blocks.moveToNext();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.calendar_block, null);
        }

        Block b = (Block) getItem(position);

        if(b!=null){
            TextView time = (TextView) v.findViewById(R.id.lesson_time);
            TextView subject = (TextView) v.findViewById(R.id.lesson_subject);
            View homeworkHolder = v.findViewById(R.id.lesson_homework_holder);
            TextView homework = (TextView) v.findViewById(R.id.lesson_homework);

            if (time!=null){
                time.setText(String.format(v.getResources().getString(R.string.lesson_time_string),
                        b.getStartTime()/60, b.getStartTime()%60,
                        b.getEndTime()/60, b.getEndTime()%60,
                        b.getLength()/60, b.getLength()%60));
            }

            if (subject!=null){
                String subjectName;
                String locationName;
                String teacherName;
                if(b.getSubject()==null){
                    subjectName="no subject";
                    teacherName="no teacher";
                    locationName="no location";
                }
                else{
                    subjectName = b.getSubject().getName();
                    if(b.getTeacher()==null){
                        teacherName="no teacher";
                    }
                    else{
                        teacherName = b.getTeacher().getName();
                    }
                    if(b.getLocation()==null){
                        locationName = "no location";
                    }
                    else{
                        locationName = b.getLocation().getName();
                    }
                }
                subject.setText(String.format(v.getResources().getString(R.string.lesson_data_string),
                        subjectName, teacherName, locationName));
            }
        }

        return v;
    }
}
