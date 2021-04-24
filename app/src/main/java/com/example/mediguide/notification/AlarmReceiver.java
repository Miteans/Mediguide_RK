package com.example.mediguide.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mediguide.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private String medicineName, day, time, instruction, otherInstruction, dosage, image, endDateString;
    int id;
    Date endDate = null;
    Date currentDate = new Date();

    private NotificationManager notificationManager;
    public void onReceive (Context context , Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        System.out.println("Broadcast here .............................................");

        //get the medicine data and flag values
        if (intent.getExtras() != null) {
            medicineName =  intent.getStringExtra("medicineName");
            dosage = intent.getStringExtra("dosage");
            instruction =  intent.getStringExtra("instruction");
            otherInstruction = intent.getStringExtra("otherInstruction");
            day = intent.getStringExtra("day");
            time = intent.getStringExtra("time");
            image = intent.getStringExtra("image");
            id = Integer.parseInt(intent.getStringExtra("randomId"));

            endDateString = intent.getStringExtra("endDate");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            try{
                endDate = format.parse(endDateString);
            }
            catch (Exception e){}

        }

        //To cancel the repeating alarm after the end date is  crossed
        if(currentDate.compareTo(endDate) > 0){

            Intent intent1 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
        }

        //To show notification
        else{
            Intent intent_notification = new Intent(context.getApplicationContext(), AlarmDisplay.class);
            intent_notification.putExtra("time", time);
            intent_notification.putExtra("day", day);
            intent_notification.putExtra("medicineName", medicineName);
            intent_notification.putExtra("dosage", dosage);
            intent_notification.putExtra("instruction", instruction);
            intent_notification.putExtra("otherInstruction", otherInstruction);
            intent_notification.putExtra("image", image);
            PendingIntent pendingIntentNotification = PendingIntent.getActivity(context.getApplicationContext() , id, intent_notification, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                    .setContentTitle("Time to take your medicine")
                    .setContentText("Click here for more information")
                    .setContentIntent(pendingIntentNotification)
                    .setOngoing(true)
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
            System.out.println(builder);
            notificationManager.notify(id, builder.build());
            System.out.println(notificationManager);
        }



        System.out.println("Broadcast end................................");


    }

}