package com.goddard.joel.digitalplanner;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Helper class for showing and canceling new lesson
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NewLessonNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewLesson";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p/>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p/>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of new lesson notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context,
                              final Lesson lesson, final Homework homework, final int number) {
        final Resources res = context.getResources();

        final String subject = lesson.getBlock().getSubject().getName();
        final String teacher = lesson.getBlock().getTeacher().getName();
        final String location = lesson.getBlock().getLocation().getName();
        final int startTime = lesson.getBlock().getStartTime();
        Calendar c = lesson.getDate();
        c.set(Calendar.MINUTE, lesson.getBlock().getStartTime());
        final String homeworkString;
        final String fullHomework;
        final int icon;
        if(homework==null || homework.getId()<0){
            homeworkString = "No Homework Due";
            fullHomework = homeworkString;
            icon = R.drawable.ic_stat_new_lesson;
        }
        else {
            if (homework.isDone()){
                homeworkString = "Homework is Done";
                fullHomework = homeworkString;
                icon = R.drawable.ic_stat_new_lesson;
            }
            else{
                homeworkString = "Homework Incomplete!";
                fullHomework = homeworkString + "\n\t" +
                        homework.getShortDescription() + "\n\t" +
                        homework.getDescription();
                icon = R.drawable.ic_stat_homework_late;
            }
        }

        final String ticker = subject;
        final String title = res.getString(
                R.string.new_lesson_notification_title, subject, teacher);
        final String text = res.getString(
                R.string.new_lesson_notification_main_text, subject, teacher, location, startTime/60, startTime%60) + "\n" + fullHomework;

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = preference.getString(
                context.getString(R.string.pref_key_notification_ringtone), "DEFAULT_SOUND");
        final Uri sound = Uri.parse(strRingtonePreference);
        final boolean vibrate = preference.getBoolean(
                context.getString(R.string.pref_key_lesson_notification_vibrate), true);


        Intent intent = new Intent(context, LessonViewer.class);
        intent.putExtra(LessonViewer.EXTRA_ID, lesson.getId());
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)


                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)

                        // All fields below this line are optional.

                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                .setSound(sound)

                .setWhen(c.getTimeInMillis())

                        // Set the pending intent to be initiated when the user touches
                        // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                        // Show expanded text content on devices running Android 4.1 or
                        // later.
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText(subject + ": " + homeworkString))

                        // Example additional actions for this notification. These will
                        // only show on devices running Android 4.1 or later, so you
                        // should ensure that the activity in this notification's
                        // content intent provides access to the same actions in
                        // another way.
                .addAction(
                        R.drawable.ic_action_new_homework,
                        res.getString(R.string.action_new_homework),
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, HomeworkEdit.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(false)

                        // Set appropriate defaults for the notification light, sound,
                        // and vibration.
                .setDefaults(Notification.DEFAULT_LIGHTS);
        if(!vibrate){
            builder.setVibrate(null);
        }

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, Lesson, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
