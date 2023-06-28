package com.example.helpervendor.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helpervendor.Fragment.All_service;
import com.example.helpervendor.Fragment.Booking_history;
import com.example.helpervendor.Fragment.Myorder;
import com.example.helpervendor.Fragment.New_order;
import com.example.helpervendor.Fragment.Update_profile;
import com.example.helpervendor.MainActivity;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Session.UserSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    UserSession userSession;
    Resultmodel.Data data;

    private static final int UPDATE_REQ = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Updateapp();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.neworder));
            setSupportActionBar(toolbar);

        }
        userSession =new UserSession(Home.this);
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        toolbar.setTitle(getString(R.string.neworder));
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        load(new New_order());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch  (item.getItemId())
        {
            case R.id.logout:
                userSession.logoutUser();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            toolbar.setTitle(getString(R.string.neworder));
            loadFragment(new New_order());
            return true;
        }
        else if (item.getItemId() == R.id.booking) {
            toolbar.setTitle("History");
            loadFragment(new Myorder());
            return true;
        }
        else if (item.getItemId() == R.id.profile)
        {
            toolbar.setTitle("Profile");
            loadFragment(new Update_profile());
            return true;
        }
        else if (item.getItemId() == R.id.services)
        {
            toolbar.setTitle("Services");
            loadFragment(new All_service());
            return true;
        }
        return false;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if(f instanceof Booking_history)
                {
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                }
                if(f instanceof Update_profile)
                {
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                }
                if(f instanceof New_order)
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
                if(f instanceof All_service)
                {
                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
                }





            }
        });
        FragmentTransaction transaction = fragmentManager.beginTransaction();
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
                            , Home.this,UPDATE_REQ);
                }catch (Exception e){

                }

            }
        });
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


}