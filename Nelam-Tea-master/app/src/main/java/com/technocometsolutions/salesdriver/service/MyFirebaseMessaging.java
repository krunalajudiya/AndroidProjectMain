package com.technocometsolutions.salesdriver.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

//import com.example.rcaccedmy.activity.MessageActivity;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.MainActivity;

import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //  FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Log.e("token_new",""+s);
        // if(firebaseUser !=null)
        //  {
        //   updateToken(s);
        //  }
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendOreoNotification(remoteMessage);

//            Log.e("oreo_notification", "" + remoteMessage.getNotification().getBody().toString());
        }
        else {
            sendNotification(remoteMessage);
  //          Log.e("normal_notification", "" + remoteMessage.getNotification().getBody().toString());
        }
/*
        if(firebaseUser != null && sented.equals(firebaseUser.getUid())) {
            if (!currentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                    Log.e("oreo_notification", "" + remoteMessage.getNotification().toString());
                }
                 else {
                    sendNotification(remoteMessage);
                    Log.e("normal_notification", "" + remoteMessage.getNotification().toString());
                }
             }
        }
*/
    }
    private void sendOreoNotification(RemoteMessage remoteMessage) {
        //   String user=remoteMessage.getData().get("user");
        //   String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        try
        {
            int i=1;
            // int j=Integer.parseInt(user.replaceAll("[a-zA-Z]",""));
            Intent intent=new Intent(this, MainActivity.class);
            Bundle bundle=new Bundle();
            //  bundle.putString("userid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri dafaultsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            OreoNotification oreoNotification=new OreoNotification(this);
            Notification.Builder builder=oreoNotification.getOrieoNotification(title,body,pendingIntent,dafaultsound);

           /* if(j>0)
            {
                i=j;
            }*/
           
            ShortcutBadger.applyNotification(this, builder.build(), i);
            oreoNotification.getManager().notify(i,builder.build());
            i++;
        }
        catch (NumberFormatException e)
        {
            Log.e("numberformat",""+e);
        }
    }
    private void sendNotification(RemoteMessage remoteMessage) {
/*
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
*/
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        String channelId = getString(R.string.app_name);
        //Integer NOTIFICATION_ID = 1;
        int unOpenCount=1;
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("NotificationMessage", remoteMessage);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,unOpenCount,notificationIntent,PendingIntent.FLAG_ONE_SHOT);
        Uri dafaultsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(dafaultsound)
                .setChannelId(channelId)
                .setContentIntent(pendingIntent);

        Notification notification1=builder.build();



        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        ShortcutBadger.applyNotification(getApplicationContext(), notification1, unOpenCount);
        unOpenCount=unOpenCount+1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId, channelId,
                    NotificationManager.IMPORTANCE_HIGH
            );
            noti.createNotificationChannel(notificationChannel);
        }

        noti.notify(unOpenCount, notification1);
    }
    /* private void updateToken(String refreshtoken) {
         FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
         DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
         Token token=new Token(refreshtoken);
         reference.child(firebaseUser.getUid()).setValue(token);
     }*/
    private Integer randomNumber() {
        Long time = new Date().getTime();
        String tmpStr = time.toString();
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        return Integer.valueOf(last4Str);
    }
}
