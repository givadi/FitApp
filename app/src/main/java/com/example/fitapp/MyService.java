package com.example.fitapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    final Timer timer = new Timer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final int[] timeremain = {intent.getIntExtra("TimeValue",2000)};

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Intent intent1local = new Intent();
                intent1local.setAction("Counter");
                timeremain[0]-=1000;
                notificationUpdate(timeremain[0]);
                if (timeremain[0] <= 0){
                    notificationStartPractice();
                    timer.cancel();
                }
                intent1local.putExtra("TimeRemaining", timeremain[0]);
                sendBroadcast(intent1local);
            }
        }, 0,1000);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificationUpdate(int t) {
        try {
            Intent notificationIntent = new Intent(this, TimerWork.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification[] notification = {new
                    NotificationCompat.Builder(this, "work")
                    .setContentTitle("Не забывайте пить воду")
                    .setContentText("До начала перерыва осталось: "+String.format(Locale.getDefault(), "%02d:%02d",t/1000/60,t/1000%60))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .build()};
            startForeground(1, notification[0]);
            NotificationChannel notificationChannel = new NotificationChannel("work", "My Counter Service", NotificationManager.IMPORTANCE_NONE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificationStartPractice() {
        try {
            Intent notificationIntent = new Intent(this, Transition.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            final Notification[] notification = {new
                    NotificationCompat.Builder(this, "startPractice")
                    .setContentTitle("Время вышло")
                    .setContentText("Настало время сделать перерыв")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .build()};
            startForeground(1, notification[0]);
            NotificationChannel notificationChannel = new NotificationChannel("startPractice", "My Counter Service", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

