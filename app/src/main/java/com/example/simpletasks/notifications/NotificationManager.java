package com.example.simpletasks.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.simpletasks.R;
import com.example.simpletasks.TaskGuideActivity;

public class NotificationManager {
    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "default_notification_channel";
    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";
    private final Context context;
    private final String notificationTitle;
    private final String notificationDescription;

    /**
     * constructor for a new notification manager
     *
     * @param context     the context where the notification comes from
     * @param title       the title of the notification
     * @param description the description of the notification
     */
    public NotificationManager(Context context, String title, String description) {
        this.context = context;
        this.notificationTitle = title;
        this.notificationDescription = description;
    }

    /**
     * schedules the notification at the given time
     */
    public void scheduleNotification(long notificationTimeMillis, String taskId) {
        Intent intentOnClick = new Intent(context, TaskGuideActivity.class);
        //TODO change to the constant in edit task guide
        intentOnClick.putExtra("task id", taskId);
        PendingIntent pendingIntentOnClick = PendingIntent.getActivity(context, 0, intentOnClick, 0);
        //get the notification
        Notification notification = getNotification(pendingIntentOnClick);

        //create intent with notification
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION, notification);
        notificationIntent.putExtra(NOTIFICATION_ID, 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set notification in alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationTimeMillis, pendingIntent);
    }

    /**
     * schedules the notification at the given time
     */
    public void cancelNotification() {
        //create intent with notification
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //cancel notification in alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    //builds a notification with the given properties
    private Notification getNotification(PendingIntent intentOnClick) {
        return new NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setContentIntent(intentOnClick)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();
    }
}
