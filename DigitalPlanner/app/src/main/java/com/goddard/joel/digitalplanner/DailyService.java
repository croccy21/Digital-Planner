package com.goddard.joel.digitalplanner;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class DailyService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_NOTIFY = "com.goddard.joel.digitalplanner.action.NOTIFY";
    public static final String ACTION_SETUP = "com.goddard.joel.digitalplanner.action.SETUP";

    public static final String EXTRA_LESSON_ID = "com.goddard.joel.digitalplanner.extra.LESSON_ID";
    public static final String EXTRA_DATE = "com.goddard.joel.digitalplanner.extra.DATE";

    public DailyService() {
        super("DailyNotificationCalculationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d("Notification", action);
            if (ACTION_NOTIFY.equals(action)) {
                final long id = intent.getLongExtra(EXTRA_LESSON_ID, -1);
                if(id>=0) {
                    handleActionNotify(id);
                }
            } else if (ACTION_SETUP.equals(action)) {
                handleActionSetupNotifications();
            }
        }
    }

    private void handleActionSetupNotifications(){
        Log.d("Notification", "setup notification");
        Database db = new Database(this);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Cursor c = DatabaseTableBlock.getByDay(db, day);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            Lesson l = new Lesson(db, c.getLong(c.getColumnIndex(DatabaseTableBlock.FIELD_BLOCK_ID)), calendar);
            if(!l.isCanceled()){
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);//gets the alarm service
                calendar = Util.setDateToStart(calendar);//resets the calendar day to zero
                calendar.set(Calendar.MINUTE, l.getBlock().getStartTime() - 5);//sets minutes to that of lesson start - 5
                Calendar now = Calendar.getInstance();
                if(calendar.getTimeInMillis()>now.getTimeInMillis()) {//If in future
                    Intent intent = new Intent(this, DailyService.class);//Creates intent to open notification
                    intent.setAction(ACTION_NOTIFY);
                    intent.putExtra(EXTRA_LESSON_ID, l.getId());
                    PendingIntent pi = PendingIntent.getService(this, (int) l.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);//Creates pending intent
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);//sets alarm to wake up phone
                    Log.d("Notification", "Scheduling notification " + l.getId() + " for " +calendar.get(Calendar.HOUR_OF_DAY)+ ":"+ calendar.get(Calendar.MINUTE));
                }
                else{
                    Log.d("Notification", "Notification " + l.getId() + " in past: not scheduling");
                }
            }
            else{
                Log.d("Notification", "Notification " + l.getId() + " canceled: not scheduling");
            }
            c.moveToNext();
        }
    }

    public void handleActionNotify(long lessonId){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notification_enabled = sharedPref.getBoolean(getResources().getString(
                R.string.pref_key_notification_lesson), true);
        if(notification_enabled) {
            Log.d("Notification", "activate notification for " + lessonId);
            Database db = new Database(this);
            Cursor cursor = DatabaseTableLesson.getByID(db, lessonId);
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {
                Lesson l = new Lesson(db, cursor.getLong(cursor.getColumnIndex(DatabaseTableLesson.FIELD_LESSON_ID)));
                ArrayList<Homework> homeworks = l.getHomeworksDue(db);
                Homework homework;
                if (homeworks.size() > 0) {
                    homework = homeworks.get(0);
                } else {
                    homework = null;
                }
                NewLessonNotification.notify(this, l, homework, 1);
            }
        }
        else{
            Log.d("Notification","Not displaying notification as notifications disabled");
        }
    }
}
