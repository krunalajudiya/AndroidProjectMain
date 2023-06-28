package com.technocometsolutions.salesdriver.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.MainActivity;
import com.technocometsolutions.salesdriver.utlity.SessionManager;


import java.util.Date;

@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        /*SessionManager sessionManager = new SessionManager(this);
        sessionManager.setFirebaseToken(s);*/


    }

    @Override
    public void onMessageReceived(@NonNull  RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("MsgNotification", "Message received ["+remoteMessage+"]");
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        Log.d("remotemessagedata",remoteMessage.getNotification().getBody());
        Log.d("remotemessagedata1",remoteMessage.getNotification().getTitle());
        Log.d("remotemessagedata2",remoteMessage.getMessageId());

    }


    private void showNotification(String title,String body) {
        Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        String channelId = getString(R.string.app_name);
        Integer NOTIFICATION_ID = randomNumber();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setChannelId(channelId);

        Notification notification = notificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId, channelId,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private Integer randomNumber() {
        Long time = new Date().getTime();
        String tmpStr = time.toString();
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        return Integer.valueOf(last4Str);
    }
}