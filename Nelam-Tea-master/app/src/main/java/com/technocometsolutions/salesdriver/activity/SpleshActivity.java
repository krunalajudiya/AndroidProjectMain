package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.utlity.SessionManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

public class SpleshActivity extends AppCompatActivity {
    Animation anim;
    SessionManager sessionManager;
    ImageView iv_splesh;
    private String androidDeviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        sessionManager=new SessionManager(this);
        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("ddDeviceId","DeviceId : "+androidDeviceId);


        /*iv_splesh=findViewById(R.id.iv_splesh);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              //  startActivity(new Intent(this,HomeActivity.class));
                // HomeActivity.class is the activity to go after showing the splash screen.





            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        iv_splesh.startAnimation(anim);*/
//permisssion();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sessionManager.checkLoggedIn())
                {
                    Intent intent=new Intent(SpleshActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(SpleshActivity.this,LoginScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);


    }

    public void permisssion() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        //requestPermissionOfLocation();
                      //  permisssion();

                       /* Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
*/
                        Toast.makeText(SpleshActivity.this, "You Must Have accept this location", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        permisssion();
                        token.continuePermissionRequest();
                    }


                }).check();

    }
}
