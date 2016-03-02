package com.goddard.joel.digitalplanner;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
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
    private boolean showHomeworks = false;
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

        View homeworkHolder = v.findViewById(R.id.lesson_homework_holder);
        TextView homeworkMessage = (TextView) v.findViewById(R.id.lesson_homework);
        TextView homeworkShort = (TextView) v.findViewById(R.id.lesson_homework_short);
        TextView homeworkLong = (TextView) v.findViewById(R.id.lesson_homework_long);
        CardView card = (CardView) v.findViewById(R.id.lesson_holder);
        TextView time = (TextView) v.findViewById(R.id.lesson_time);
        TextView subject = (TextView) v.findViewById(R.id.lesson_subject);
        Switch canceledSwitch = (Switch) v.findViewById(R.id.cancel_switch);

        if(l!=null) {
            final Block b = l.getBlock();
            if (b != null && (!l.isCanceled() || showCanceled)) {

                if(card != null && l.isCanceled()){
                    card.setCardBackgroundColor(Color.argb(100, 0x80, 0x80, 0x80));
                }
                else if (card!=null){
                    card.setCardBackgroundColor(Color.argb(100, 0xFF, 0xFF, 0xFF));
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
                        canceledSwitch.setOnCheckedChangeListener(null);
                        canceledSwitch.setChecked(!l.isCanceled());
                        canceledSwitch.setTag(position);
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
            ArrayList<Homework> homeworks = l.getHomeworksDue(db);
            if (homeworks.size()>0 && showHomeworks){
                homeworkHolder.setVisibility(View.VISIBLE);
                if(homeworks.size()==1){
                    Homework homework = homeworks.get(0);
                    String homeworkText = "HOMEWORK ";
                    if(homework.isDone()){
                        homeworkText+="DONE";
                        homeworkLong.setVisibility(View.GONE);
                    }
                    else{
                        homeworkText+="INCOMPLETE";
                        if(homework.getDescription().isEmpty()){
                            homeworkLong.setVisibility(View.GONE);
                        }
                        else{
                            homeworkLong.setVisibility(View.VISIBLE);
                            homeworkLong.setText(homework.getDescription());
                        }
                    }
                    homeworkMessage.setText(homeworkText);
                    homeworkShort.setVisibility(View.VISIBLE);
                    homeworkShort.setText(homework.getShortDescription());
                }
                else{
                    homeworkLong.setVisibility(View.GONE);
                    homeworkShort.setVisibility(View.GONE);
                    String homeworkText = String.format("%s HOMEWORKS", homeworks.size());
                    int dones=0;
                    for(Homework h:homeworks){
                        if(h.isDone()){
                            dones+=1;
                        }
                    }
                    homeworkText += String.format(" (%s DONE)", dones);
                    homeworkMessage.setText(homeworkText);
                }
            }
            else{
                homeworkHolder.setVisibility(View.GONE);
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

    public boolean isShowHomeworks() {
        return showHomeworks;
    }

    public void setShowHomeworks(boolean showHomeworks) {
        this.showHomeworks = showHomeworks;
    }
}
