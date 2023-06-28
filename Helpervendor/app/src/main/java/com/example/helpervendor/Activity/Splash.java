package com.example.helpervendor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helpervendor.MainActivity;
import com.example.helpervendor.R;
import com.example.helpervendor.Session.UserSession;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;


public class Splash extends AppCompatActivity {

    UserSession userSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userSession=new UserSession(Splash.this);
        // appTitleFade = AnimationUtils.loadAnimation(this, R.anim.textfadeout);


        /*FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("subscribe","aasa");
            }
        });*/


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userSession.isLoggedIn()) {
                    Intent intent = new Intent(Splash.this, Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

}
