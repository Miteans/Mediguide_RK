package com.example.mediguide.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mediguide.R;

public class AppointmentNotification extends BroadcastReceiver {

    private String appointmentName, day, time, hospitalName;
    int id;
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        System.out.println("Broadcast here .............................................");

        //get the appointment data and flag values
        if (intent.getExtras() != null) {
            appointmentName =  intent.getStringExtra("appointmentName");
            hospitalName = intent.getStringExtra("hospitalName");
            day = intent.getStringExtra("day");
            time = intent.getStringExtra("time");
            id = Integer.parseInt(intent.getStringExtra("randomId"));

        }

        //To show notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle("Appointment with the doctor")
                .setContentText(appointmentName + "\n Hospital Name : " + hospitalName + "\n Day : " + day + "\n Time : " + time)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);//to show content in lock screen


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    String.valueOf(id),
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(String.valueOf(id));
        }

        notificationManager.notify(id, builder.build());

        System.out.println("Broadcast end................................");


    }
}