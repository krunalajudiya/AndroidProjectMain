package com.example.helperfactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.helperfactory.Activity.Splash;
import com.example.helperfactory.Fragments.Home;
import com.example.helperfactory.Fragments.MyBooking;
import com.example.helperfactory.Fragments.Ongoing;
import com.example.helperfactory.Fragments.Profile;
import com.example.helperfactory.Model.Slidermodel;
import com.example.helperfactory.Receiver.ConnectionReceiver;
import com.example.helperfactory.Remote.Retrofitclient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.razorpay.PaymentResultListener;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PaymentResultListener {


    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ConnectionReceiver connectionReceiver;
    int success=0;
    OnHeadlineSelectedListener mCallback;
    private static final String TAG = Ongoing.class.getSimpleName();
    private static final int UPDATE_REQ = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.home));
            Updateapp();
        }

        toolbar.setTitle(getString(R.string.home));


        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Bundle b=getIntent().getExtras();

        boolean b1=getIntent().getBooleanExtra("billnotification",false);
        Log.d("notificatin true value", String.valueOf(b1));
        if(getIntent().getBooleanExtra("billnotification",false)){
            load(new MyBooking());
            Log.d("fff","notitfication_act");
        }

        if (getIntent().getExtras()!=null)
        {
            success=b.getInt("success");
            Log.d("dsdsd", String.valueOf(success));
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
            toolbar.setTitle("My Booking");
            load(new MyBooking());
        }else {
            load(new Home());
        }




    }
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(String id);
    }
    //Here is new method
    public void passVal(OnHeadlineSelectedListener onHeadlineSelectedListener) {
        this.mCallback = onHeadlineSelectedListener;

    }
    @Override
    public void onAttachFragment(androidx.fragment.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Ongoing)
        {
            //mCallback=(OnHeadlineSelectedListener)fragment;
        }

    }
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            mCallback.onArticleSelected(razorpayPaymentID);
            //Toast.makeText(getApplicationContext(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }


    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(getApplicationContext(), "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.connectionReceiver = new ConnectionReceiver();
        this.connectionReceiver.setOnCheckListener(new ConnectionReceiver.OnCheckListener() {
            @Override
            public void onCheck(Boolean value) {
                if (value)
                {
                    //Toast.makeText(getApplicationContext(),"Internet Is Connected",Toast.LENGTH_LONG).show();
                }else{
                    //Toast.makeText(getApplicationContext(),"Internet Is not Connected",Toast.LENGTH_LONG).show();
                }
            }
        });
        registerReceiver(this.connectionReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectionReceiver);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            toolbar.setTitle(getString(R.string.home));
            loadFragment(new Home());
            return true;
        }
        else if (item.getItemId() == R.id.booking) {
            toolbar.setTitle("My Booking");
            loadFragment(new MyBooking());

            return true;
        }
        else if (item.getItemId() == R.id.profile)
        {
            toolbar.setTitle(getString(R.string.myprofile));
            loadFragment(new Profile());
            return true;
        }
        return false;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
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
                            ,MainActivity.this,UPDATE_REQ);
                }catch (Exception e){

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            Log.d("data","value null");
            return;
        }
        if (requestCode == UPDATE_REQ){
            Toast.makeText(this,"Downloading Start",Toast.LENGTH_LONG).show();
            if (requestCode != RESULT_OK){
                Toast.makeText(this,"Update Failed",Toast.LENGTH_LONG).show();
            }
        }
    }
    //    public void UpdateApp(){
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(result -> {
//
//            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
////                requestUpdate(result);
////                android.view.ContextThemeWrapper ctw = new android.view.ContextThemeWrapper(this,R.style.Theme_AlertDialog);
////                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctw);
//                AlertDialog.Builder  alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
//                alertDialogBuilder.setTitle("Update Helper Factory");
//                alertDialogBuilder.setCancelable(false);
//                alertDialogBuilder.setIcon(R.drawable.logo);
//                alertDialogBuilder.setMessage("A new version of Helper Factory is available. Please update to version");
//                alertDialogBuilder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        try{
//                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
////                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
//                        }
//                        catch (ActivityNotFoundException e){
//
//                        }
//                    }
//                });
//                alertDialogBuilder.show();
//
//            } else {
//
//            }
//        });
//    }
}