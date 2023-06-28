package com.example.helperfactory.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.helperfactory.Fragments.MyBooking;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Session.UserSession;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    UserSession userSession;
    Resultmodel.Data data;
    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";

    private final String ADMIN_CHANNEL_ID ="admin_channel";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        userSession =new UserSession(this);
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
//        final Intent intent = new Intent(this, MainActivity.class);
        Intent openbillwindow=new Intent(this, MainActivity.class);
        openbillwindow.putExtra("billnotification",true);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        Log.d("remotemessage",remoteMessage.getData().toString());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        openbillwindow.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent openbillpendingintent = PendingIntent.getActivity(this , 0, openbillwindow,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                .setSmallIcon(R.drawable.logo)
//                .setLargeIcon(largeIcon)
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getData().get("message"))
////                .setAutoCancel(true)
//                .setSound(notificationSoundUri)
//                .setContentIntent(pendingIntent);

        NotificationCompat.Builder openwindownotificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
//                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(openbillpendingintent);

        String value=remoteMessage.getData().get("factory");
        boolean factory=Boolean.valueOf(value);
        String user_id=remoteMessage.getData().get("user_id");
        Log.d("value of vender", String.valueOf(factory));
//        Log.d("user_id",remoteMessage.getData().get("user_id"));
//        Log.d("user_id_ses",data.getClient_id());
        if (factory==true){
            if (user_id==null){
                notificationManager.notify(notificationID,openwindownotificationBuilder.build());
            }
            else  if (Integer.parseInt(data.getClient_id())==Integer.parseInt(user_id))
            {
                notificationManager.notify(notificationID,openwindownotificationBuilder.build());
            }

        }

//        if (Integer.parseInt(remoteMessage.getData().get("user_id"))==Integer.parseInt(data.getClient_id())){
//
//
//        }

//        List<String> notif_city= Arrays.asList(remoteMessage.getData().get("title").split("To"));
//        List<String> user_city=Arrays.asList(user.getCity_pref().split(","));
//
//        //Set notification color to match your app color template
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
//        }
//        if(Integer.parseInt(remoteMessage.getData().get("type"))==0) {
//            Log.d("city_pref", notif_city.get(0) + notif_city.get(1));
//        }else{
//
//        }
//        /*if ((!TextUtils.equals(remoteMessage.getData().get("userid"),user.getId())) && user_city.contains(notif_city.get(0)) || user_city.contains(notif_city.get(1))) {
//            notificationManager.notify(notificationID, notificationBuilder.build());
//        }*/
//        if(Integer.parseInt(remoteMessage.getData().get("type"))==0) {
//            if (!TextUtils.equals(remoteMessage.getData().get("userid"), user.getId()) && (user_city.contains(notif_city.get(0).trim()) || user_city.contains(notif_city.get(1).trim()))) {
//                Log.d("user_id", user.getId());
//                notificationManager.notify(notificationID, notificationBuilder.build());
//            } else {
//
//            }
//        }else if (Integer.parseInt(remoteMessage.getData().get("type"))==1)
//        {
//            if (!TextUtils.equals(remoteMessage.getData().get("userid"), user.getId())) {
//                Log.d("user_id", user.getId());
//                notificationManager.notify(notificationID, notificationBuilder.build());
//            }
//        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null ) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
    @Override
    public void onNewToken(@NonNull String s) {

        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("subscribe","aasa");
            }
        });
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }

}

