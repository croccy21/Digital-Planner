package com.goddard.joel.digitalplanner;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Joel Goddard on 30/01/2016.
 *
 * @author Joel Goddard
 */
public class CalenderAdapter extends ArrayAdapter {

    public OnLessonCanceledChanged getLessonCanceledChangedListener() {
        return lessonCanceledChangedListener;
    }

    public void setLessonCanceledChangedListener(OnLessonCanceledChanged lessonCanceledChangedListener) {
        this.lessonCanceledChangedListener = lessonCanceledChangedListener;
    }

    interface OnLessonCanceledChanged{
        void onLessonCancelChange(View v, int position, boolean canceled);
    }

    private Database db;
    private Calendar calendar;
    private boolean showCanceled = false;
    private int p;

    private OnLessonCanceledChanged lessonCanceledChangedListener;

    public CalenderAdapter(Context context, Database db, Calendar calendar) {
        super(context, R.layout.calendar_block);

        lessonCanceledChangedListener = new OnLessonCanceledChanged() {
            @Override
            public void onLessonCancelChange(View v, int position, boolean canceled) {

            }
        };

        this.calendar = calendar;
        this.db = db;
        Cursor blocks = DatabaseTableBlock.getByDay(db, calendar.get(Calendar.DAY_OF_WEEK));
        blocks.moveToFirst();
        Log.d("DATABASE", String.valueOf(blocks.getCount()));
        for(int i =0; i<blocks.getCount();i++){
            Lesson l = new Lesson(db, blocks.getLong(blocks.getColumnIndex(DatabaseTableBlock.FIELD_BLOCK_ID)),calendar);
            add(l);
            blocks.moveToNext();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.calendar_block, null);
        }

        Lesson l = (Lesson) getItem(position);
        if(l!=null) {
            final Block b = l.getBlock();
            if (b != null && (!l.isCanceled() || showCanceled)) {
                CardView card = (CardView) v.findViewById(R.id.lesson_holder);
                TextView time = (TextView) v.findViewById(R.id.lesson_time);
                TextView subject = (TextView) v.findViewById(R.id.lesson_subject);
                View homeworkHolder = v.findViewById(R.id.lesson_homework_holder);
                TextView homework = (TextView) v.findViewById(R.id.lesson_homework);
                Switch canceledSwitch = (Switch) v.findViewById(R.id.cancel_switch);

                if(card != null && l.isCanceled()){
                    card.setCardBackgroundColor(Color.argb(100, 0x80, 0x80, 0x80));
                }

                if (time != null) {
                    time.setText(String.format(v.getResources().getString(R.string.lesson_time_string),
                            b.getStartTime() / 60, b.getStartTime() % 60,
                            b.getEndTime() / 60, b.getEndTime() % 60,
                            b.getLength() / 60, b.getLength() % 60));
                }

                if (subject != null) {
                    String subjectName;
                    String locationName;
                    String teacherName;
                    if (b.getSubject() == null) {
                        subjectName = "no subject";
                        teacherName = "no teacher";
                        locationName = "no location";
                    } else {
                        subjectName = b.getSubject().getName();
                        if (b.getTeacher() == null) {
                            teacherName = "no teacher";
                        } else {
                            teacherName = b.getTeacher().getName();
                        }
                        if (b.getLocation() == null) {
                            locationName = "no location";
                        } else {
                            locationName = b.getLocation().getName();
                        }
                    }
                    subject.setText(String.format(v.getResources().getString(R.string.lesson_data_string),
                            subjectName, teacherName, locationName));
                }
                if(canceledSwitch!=null){
                    if(showCanceled) {
                        canceledSwitch.setVisibility(View.VISIBLE);
                        canceledSwitch.setChecked(!l.isCanceled());
                        canceledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                lessonCanceledChangedListener.onLessonCancelChange(buttonView, position, !isChecked);
                            }
                        });
                    }
                    else{
                        canceledSwitch.setVisibility(View.GONE);
                    }
                }
            }
        }

        return v;
    }

    @Override
    public long getItemId(int position) {
        return ((Lesson) getItem(position)).getId();
    }

    public boolean isShowCanceled() {
        return showCanceled;
    }

    public void setShowCanceled(boolean showCanceled) {
        this.showCanceled = showCanceled;
    }
}
