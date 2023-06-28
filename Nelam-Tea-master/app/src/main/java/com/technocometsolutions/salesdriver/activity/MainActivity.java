package com.technocometsolutions.salesdriver.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.NotificationAdapter;
import com.technocometsolutions.salesdriver.model.LoginResponsModel;
import com.technocometsolutions.salesdriver.model.NotificationModel;
import com.technocometsolutions.salesdriver.model.TotalNotificationModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.LocationReceiverServices;
import com.technocometsolutions.salesdriver.utlity.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public CardView btnAttendManagent,btnOrder,btnMySchedule/*,btnSaveExpense*/,btnLeave,btnDealerPayment,btnadddealerorder,btnretailerorder,btndealeropeningstock,btnadddailyexpense,btnDealerPaymentCollection,btnDailydsr,btnViewSalesReport,btnSalesReport,btnViewExpenses;
    Intent intent;
    private LocationReceiverServices locationReceiverServices;
    private static final int NOTIFICATION_PERMISSION_CODE = 12313;
    Context ctx;
    SessionManager sessionManager;
    TextView textCartItemCount;
    public List<LoginResponsModel.Item> itemList = new ArrayList<>();
    int mCartItemCount = 0;
    LoadingView loadingView;
    ErrorView errorView;
    public Context getCtx() {
        return ctx;
    }
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("firebase_toen",""+ FirebaseInstanceId.getInstance().getToken());
        FloatingActionButton fab = findViewById(R.id.fab);
        GpsProvider.onGPS(this);

        fab.setVisibility(View.GONE);
        mCartItemCount= 0;
        sessionManager=new SessionManager(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        init();
        enableUserStoreData(this);
        requestNotificationPermission();
        Log.d("emp_id",sessionManager.getId());
    }
    public void init() {
        btnAttendManagent=findViewById(R.id.btnAttendManagent);
//        btnMySchedule=findViewById(R.id.btnMySchedule);
//        btndealeropeningstock=findViewById(R.id.btnAddDealerStock);
        btnretailerorder=findViewById(R.id.btnretailerorder);
        btnadddealerorder=findViewById(R.id.btnadddealerorder);
        btnOrder=findViewById(R.id.btnOrder);
        btnadddailyexpense=findViewById(R.id.btnadddailyexpense);
        btnLeave=findViewById(R.id.btnLeave);
        //btnSaveExpense=findViewById(R.id.btnSaveExpense);
//        btnDealerPayment=findViewById(R.id.btnDealerPayment);
//        btnDailydsr=findViewById(R.id.btndailydsdownload);
        btnViewSalesReport=findViewById(R.id.btnViewSalesReport);
        btnSalesReport=findViewById(R.id.btnSalesReport);
        btnViewExpenses=findViewById(R.id.btnViewExpenses);


        btnViewSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ViewDailyReport.class);
                startActivity(intent);
            }
        });
        btnSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SalesReport.class);
                startActivity(intent);
            }
        });
        btnViewExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ViewExpenses.class);
                startActivity(intent);
            }
        });

        btnAttendManagent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AttendanceMangementActivity.class);
                startActivity(intent);
            }
        });

//        btnMySchedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,MyScheduleActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btndealeropeningstock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,DealerStockActivity.class);
//                startActivity(intent);
//            }
//        });


        btnretailerorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RetailerActivity.class);
                startActivity(intent);
            }
        });

        btnadddealerorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddOrderActivity.class);
                startActivity(intent);
            }
        });


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,YourHistryActivity.class);
                startActivity(intent);
            }
        });

        btnadddailyexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ExpensesDetail.class);
                startActivity(intent);
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LeaveReportGenerateActivity.class);
                startActivity(intent);

            }
        });



        /*btnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SaveexActivity.class);
                startActivity(intent);
            }
        });*/
//        btnDealerPayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,DealerPaymentActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnDailydsr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,DailySalesReportActivity.class);
//                startActivity(intent);
//            }
//        });


        //oncreate
        ctx = this;
        getAppconrol();
    }
    public void getAppconrol() {
        locationReceiverServices = new LocationReceiverServices(getCtx());
        intent = new Intent(getCtx(), locationReceiverServices.getClass());
        if (!isMyServiceRunning(locationReceiverServices.getClass())) {
            startService(intent);
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onPause() {

        //dd1();


        super.onPause();
    }

    private void dd1() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(intent);
                // locationReceiverServices = new LocationReceiverServices(getCtx());
                //intent = new Intent(getCtx(), locationReceiverServices.getClass());
                //getAppconrol();
                //dd2();
            }
        },(5*60)*1000);
    }
    private void dd2() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(intent);
                // locationReceiverServices = new LocationReceiverServices(getCtx());
                //intent = new Intent(getCtx(), locationReceiverServices.getClass());
                //getAppconrol();
                //dd1();
            }
        },(5*60)*1000);
    }

    @Override
    public void onBackPressed() {
 //       getAppconrol();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
       // Menu nav_Menu = menu.getMenu();


        NavigationView navigationView = findViewById(R.id.nav_view);
        View view=navigationView.getHeaderView(0);

        TextView tvusername=view.findViewById(R.id.tvUserName);
        TextView tvuserid=view.findViewById(R.id.tvUserId);

        tvusername.setText(sessionManager.getKEY_user_name());
        tvuserid.setText(sessionManager.getKEY_user_id());


        Menu nav_Menu = navigationView.getMenu();
       // nav_Menu.findItem(R.id.nav_order).setVisible(false);


        final MenuItem menuItem = menu.findItem(R.id.notification);


        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);



        //setupBadge();
        textCartItemCount.setVisibility(View.GONE);
        getstepbage(sessionManager.getId());

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    private void getstepbage(String emp_id) {


//        loadingView = new LoadingView(MainActivity.this);
//        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url;
        url = getString(R.string.json_total_notification);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
//                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        TotalNotificationModel loginResponse = gson.fromJson(response, TotalNotificationModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getTotal_notification()!=null){
                                String total_notification=loginResponse.getTotal_notification();
                                Log.d("total_notification",total_notification);
                                mCartItemCount=Integer.parseInt(total_notification);
                                setupBadge();
                            }

                        }
                        else
                        {
                            setupBadge();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
//                errorView = new ErrorView(MainActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
//                    @Override
//                    public void onRetryButtonClicked() {
//                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
//                    }
//
//                    @Override
//                    public void onCancelButtonClicked() {
//                        onBackPressed();
//                    }
//                });
//                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }


    private void setupBadge() {

        if (textCartItemCount != null) {
            Log.d("mCartItemCount", String.valueOf(mCartItemCount));
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id== R.id.notification)
        {
            Intent intent=new Intent(MainActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(MainActivity.this,ExpensesDetail.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_order) {
//           // Intent intent=new Intent(MainActivity.this,OrderScNewActivity.class);
//            Intent intent=new Intent(MainActivity.this,AddOrderActivity.class);
//            startActivity(intent);
//
//        }
        else if (id == R.id.nav_livereport) {
            Intent intent=new Intent(MainActivity.this, LeaveReportActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_yourhistry) {
            Intent intent=new Intent(MainActivity.this,YourHistryActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_dealer_serve_report) {
            Intent intent=new Intent(MainActivity.this,DealerServeReportActivity.class);
            startActivity(intent);

        }
//        else if (id == R.id.nav_my_schedule) {
//            Intent intent=new Intent(MainActivity.this,MyScheduleActivity.class);
//            startActivity(intent);
//delete
//        }
//        else if (id == R.id.nav_dealer_payment) {
//            Intent intent=new Intent(MainActivity.this,DealerPaymentActivity.class);
//            startActivity(intent);
//delete
//        }
        else if (id == R.id.nav_leave_report) {
            Intent intent=new Intent(MainActivity.this,LeaveReportGenerateActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_daily_activity_report) {
            Intent intent=new Intent(MainActivity.this,DailyActivityReportActivity.class);
            startActivity(intent);


        }else if (id == R.id.nav_dealer_visit) {
            Intent intent=new Intent(MainActivity.this,DealerVisitActivity.class);
            startActivity(intent);

        }
//        else if (id == R.id.nav_retailer) {
//            Intent intent=new Intent(MainActivity.this,RetailerActivity.class);
//            startActivity(intent);
//
//        }
//        else if (id == R.id.nav_dealer_stock) {
//            Intent intent=new Intent(MainActivity.this,DealerStockActivity.class);
//            startActivity(intent);
//delete
//        }
//        else if (id == R.id.nav_add_opening_stock) {
//            Intent intent=new Intent(MainActivity.this,DealerStockActivity.class);
//            startActivity(intent);
//
//        }
        else if (id == R.id.nav_view_complaint) {
            Intent intent=new Intent(MainActivity.this,ViewComplaintActivity.class);
            startActivity(intent);

        }

//        else if (id == R.id.nav_add_retailer) {
//            Intent intent=new Intent(MainActivity.this,AddRetailerActivity.class);
//            startActivity(intent);
//delete
//        }
//        else if (id == R.id.nav_orders_retailer) {
//            Intent intent=new Intent(MainActivity.this,RetailerActivity.class);
//            startActivity(intent);
//
//        }
//        else if (id == R.id.nav_add_route) {
//            Intent intent=new Intent(MainActivity.this,AddRouteActivity.class);
//            startActivity(intent);
//delete
//        }
//        else if (id == R.id.nav_daily_dsr) {
//            Intent intent=new Intent(MainActivity.this,DailySalesReportActivity.class);
//            startActivity(intent);
//delete
//        }
        else if(id==R.id.nav_sales_report){
            Intent intent=new Intent(MainActivity.this,SalesReport.class);
            startActivity(intent);

        }else if(id==R.id.nav_add_new_dealer){
            Intent intent=new Intent(MainActivity.this,AddNewDealer.class);
            startActivity(intent);
        }else if (id==R.id.nav_expenses){
            Intent intent=new Intent(MainActivity.this,ViewExpenses.class);
            startActivity(intent);
        }
        else if (id==R.id.nav_daily_report){
            Intent intent=new Intent(MainActivity.this,ViewDailyReport.class);
            startActivity(intent);
        }
        else if (id==R.id.nav_catalouge){
            Intent intent=new Intent(MainActivity.this,ViewCatalouge.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_logout) {
            sessionManager.clear();
            Intent intent=new Intent(MainActivity.this,LoginScreenActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE ) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public  void enableUserStoreData(Context context)
    {
        SessionManager sessionManager=new SessionManager(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url;
        url = context.getString(R.string.json_get_emp);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        LoginResponsModel loginResponse = gson.fromJson(response, LoginResponsModel.class);
                        if (loginResponse.getSuccess()) {
                            if (loginResponse.getItems() != null) {
                                itemList.addAll(loginResponse.getItems());

                                sessionManager.isLoggedIn(itemList.get(0).getEmpId(), itemList.get(0).getFirstName(), itemList.get(0).getMiddleName(), itemList.get(0).getLastName(), itemList.get(0).getEmpCode(), itemList.get(0).getPassword(), itemList.get(0).getBirthDate(), itemList.get(0).getGender(), itemList.get(0).getBloodGroup(), itemList.get(0).getGenderType(), itemList.get(0).getReportingTo(), itemList.get(0).getDesignation(), itemList.get(0).getJoiningDate(), itemList.get(0).getQualification(), itemList.get(0).getOfficialEmail(), itemList.get(0).getPersonalEmail(), itemList.get(0).getMobileNo());
                                sessionManager.setUsername(itemList.get(0).getFirstName() + itemList.get(0).getLastName());
                                sessionManager.setUserID(itemList.get(0).getEmpCode());
                                sessionManager.setKeyChargePerKm(itemList.get(0).getCharge_per_km());


                            }
                        } else {
                            Toast.makeText(MainActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", sessionManager.getId());

                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
        getstepbage(sessionManager.getId());
    }
}