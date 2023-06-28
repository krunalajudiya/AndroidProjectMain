package com.technocometsolutions.salesdriver.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.DealerComplaintPagerAdapter;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

public class ViewComplaintActivity extends AppCompatActivity {

    public SessionManager sessionManager;
    public LoadingView loadingView;
    public ErrorView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager=new SessionManager(this);
        GpsProvider.onGPS(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        DealerComplaintPagerAdapter adapter = new DealerComplaintPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }








    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }
}
