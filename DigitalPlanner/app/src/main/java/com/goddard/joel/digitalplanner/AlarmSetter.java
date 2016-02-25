package com.goddard.joel.digitalplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmSetter extends BroadcastReceiver {
    public AlarmSetter() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, DailyService.class);
        i.setAction(DailyService.ACTION_SETUP);
        Calendar c = Calendar.getInstance();
        c = Util.setDateToStart(c);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.MINUTE, 5);
        PendingIntent pi = PendingIntent.getService(context, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pi);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }
}
