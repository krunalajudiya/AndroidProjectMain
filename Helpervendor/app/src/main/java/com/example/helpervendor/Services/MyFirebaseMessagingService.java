package com.example.helpervendor.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.helpervendor.Activity.Home;
import com.example.helpervendor.MainActivity;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Session.UserSession;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="admin_channel";
    UserSession userSession;
    Resultmodel.Data data;
    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        userSession =new UserSession(this);
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        final Intent intent = new Intent(this, Home.class);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        Log.d("remotemessage",remoteMessage.getData().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.hf4);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.hf4)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
//                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);
        String valuevendor=remoteMessage.getData().get("vendor");
        boolean vendor=Boolean.valueOf(valuevendor);
        Log.d("value of vender", String.valueOf(vendor));
        String sub_cat_id=remoteMessage.getData().get("sub_cat_id");
        String client_id;
//        if (!remoteMessage.getData().get("client_id").isEmpty()){
//            client_id=remoteMessage.getData().get("client_id");
//        }

        List<String> sub_cat=Arrays.asList(data.getSub_cat().split(","));
        Log.d("sub_cat",sub_cat.get(0));
        SharedPreferences availablity_sharedpreferences= PreferenceManager.getDefaultSharedPreferences(this);
//        Log.d("vendervalue",valuevendor);

        if (vendor){
            if (availablity_sharedpreferences.getBoolean("availableStatus",false)){
                if (remoteMessage.getData().get("sub_cat_id")!=null){
                    if (remoteMessage.getData().get("city")!=null){
                        String city=remoteMessage.getData().get("city");
                        if (TextUtils.equals(data.getCity(),city) && sub_cat.contains(remoteMessage.getData().get("sub_cat_id"))){

                            notificationManager.notify(notificationID, notificationBuilder.build());
                        }
                    }
                }
//                else{
//                    notificationManager.notify(notificationID,notificationBuilder.build());
//                }
                if (remoteMessage.getData().get("client_id")!=null){
                    if (Integer.parseInt(remoteMessage.getData().get("client_id"))==Integer.parseInt(data.getVendor_id())){
                        notificationManager.notify(notificationID, notificationBuilder.build());
                    }
                }
            }
        }

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