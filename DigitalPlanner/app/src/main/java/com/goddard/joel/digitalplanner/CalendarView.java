package com.goddard.joel.digitalplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * TODO: document your custom view class.
 */
public class CalendarView extends LinearLayout {

    public interface OnItemClickListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    private OnItemClickListener onItemClickListener;

    private boolean showCanceled;
    private boolean showHomeworks;

    private Button nextDay;
    private Button previousDay;
    private Button chooseDayButton;
    private ListView list;

    private int selected;

    private Database db;
    private Calendar calendar;

    private OnClickListener onNextButtonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            populate();
        }
    };

    private OnClickListener onPreviousButtonClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            populate();
        }
    };

    private OnClickListener onSelectClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialogue = new DatePickerDialog(getContext(), dateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialogue.show();
        }
    };

    private DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);
            populate();
        }
    };

    public CalendarView(Context context) {
        super(context);
        init(null, 0);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };

        calendar = Calendar.getInstance();
        inflate(getContext(), R.layout.calendar_widget, this);
        nextDay = (Button) findViewById(R.id.next_button);
        previousDay = (Button) findViewById(R.id.previous_button);
        chooseDayButton = (Button) findViewById(R.id.day_text);
        list = (ListView) findViewById(R.id.event_list);

        nextDay.setOnClickListener(onNextButtonClick);
        previousDay.setOnClickListener(onPreviousButtonClick);
        chooseDayButton.setOnClickListener(onSelectClick);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(parent, view, position, id);
            }
        });

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CalendarView, defStyle, 0);
        a.recycle();
    }

    public void populate(){
        CalendarAdapter adapter = new CalendarAdapter(getContext(), db, calendar);
        adapter.setShowCanceled(showCanceled);
        adapter.setShowHomeworks(showHomeworks);
        adapter.setLessonCanceledChangedListener(new CalendarAdapter.OnLessonCanceledChanged() {
            @Override
            public void onLessonCancelChange(View v, int position, boolean canceled) {
                Log.d("DATABASE", String.format("position: %d", position));
                Lesson l = (Lesson) list.getAdapter().getItem(position);
                DatabaseTableLesson.update(db, l.getId(), l.getBlock().getId(), l.getDate(), canceled);
                populate();
            }
        });
        list.setAdapter(adapter);
        SimpleDateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_button_text));
        chooseDayButton.setText(df.format(calendar.getTime()));
    }

    public Database getDb() {
        return db;
    }

    public void setDb(Database db) {
        this.db = db;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    
    public void setSelected(int itemPosition){
        selected = itemPosition;
        list.setSelection(selected);
    }

    public boolean isShowCanceled() {
        return showCanceled;
    }

    /**
     * The cancel button is not working correctly for unknown reasons
     * @param showCanceled
     */
    public void setShowCanceled(boolean showCanceled) {
        this.showCanceled = showCanceled;
        CalendarAdapter adapter = (CalendarAdapter) list.getAdapter();
        if(adapter!=null) {
            adapter.setShowCanceled(showCanceled);
            list.invalidate();
        }
    }

    public boolean isShowHomeworks() {
        return showHomeworks;
    }

    public void setShowHomeworks(boolean showHomeworks) {
        this.showHomeworks = showHomeworks;
        CalendarAdapter adapter = (CalendarAdapter) list.getAdapter();
        if(adapter!=null) {
            adapter.setShowHomeworks(showHomeworks);
            list.invalidate();
        }
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
