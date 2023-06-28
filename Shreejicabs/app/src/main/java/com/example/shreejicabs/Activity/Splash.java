package com.example.shreejicabs.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class Splash extends AppCompatActivity {

    TextView logoImageViewRight;
    TextView logoImageViewLeft;
    TextView appTitleTextView;

    Animation logoFromRight;
    Animation logoFromLeft;
    Animation appTitleFade;
    private static final String SUBSCRIBE_TO = "userABC";

    private static final int UPDATE_REQ = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoImageViewLeft = (TextView) findViewById(R.id.ewsSplashLogoImgLeftImgv);
        logoImageViewRight = (TextView) findViewById(R.id.ewsSplashLogoImgRightImgv);
        appTitleTextView = (TextView) findViewById(R.id.ewsSplashLogoTitleTv);

        logoFromLeft = AnimationUtils.loadAnimation(this, R.anim.fromtopcorner);
        logoFromRight = AnimationUtils.loadAnimation(this, R.anim.frombottomcorner);
        // appTitleFade = AnimationUtils.loadAnimation(this, R.anim.textfadeout);

        logoImageViewLeft.setAnimation(logoFromLeft);
        logoImageViewRight.setAnimation(logoFromRight);
        appTitleTextView.setAnimation(appTitleFade);
        /*FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("subscribe","aasa");
            }
        });*/
        //Updateapp();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQ){
            Toast.makeText(this,"Downloading Start",Toast.LENGTH_LONG).show();
            if (requestCode != RESULT_OK){
                Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Updateapp(){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE
                            ,Splash.this,UPDATE_REQ);
                }catch (Exception e){

                }

            }
        });
    }
}
