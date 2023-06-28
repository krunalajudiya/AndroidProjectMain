package com.technocometsolutions.salesdriver.utlity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technocometsolutions.salesdriver.R;

public class LocationReceiverServices extends Service implements LocationListener {
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    String battery_per;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    SessionManager sessionManager;


    public int counter = 0;

    public LocationReceiverServices(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");

    }

    public LocationReceiverServices() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sessionManager = new SessionManager(this);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");

        fn_getlocation();
        /*Intent intent1 = new Intent(this, LocationReceiver.class);
        sendBroadcast(intent1);*/
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();
        fn_getlocation();
        timer.schedule(timerTask, (5*60)*1000, (5*60)*1000);
        //schedule the timer, to wake up every 1 second
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                fn_getlocation();
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
            }

            if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){

                        Log.e("latitude123",location.getLatitude()+"");
                        Log.e("longitude123",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("hjhjhjhj",""+latitude);
                        Log.d("hjhjhjhj1",""+longitude);
     //                   fn_update(location);
                        //SessionManager sessionManager=new SessionManager(this);
                        //getDataAttendanceManagement(sessionManager.getId(),""+latitude,""+longitude);

                    }
                }

            }



                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);

                if (locationManager!=null){

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){
                        Log.d("dsdds", String.valueOf(isGPSEnable));
                        Log.e("latitude1234",location.getLatitude()+"");
                        Log.e("longitude1234",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("hjhjhjhj",""+latitude);
                        Log.d("hjhjhjhj1",""+longitude);
                        Log.d("hjhjhjhj2",""+sessionManager.getId());
                        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                            battery_per=String.valueOf(percentage);
                        }
                        getDataAttendanceManagement(sessionManager.getId(),""+latitude,""+longitude,battery_per);
   //                     fn_update(location);


                        /*RequestQueue queue = Volley.newRequestQueue(this);
                        String url;
                        url = getString(R.string.json_punch_lat_long);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("onResponse", "onResponse: "+response);

                                        Gson gson = new Gson();
                                        AttendanceManagementModel loginResponse = gson.fromJson(response, AttendanceManagementModel.class);
                                        if (loginResponse.getSuccess())
                                        {
                                            if (loginResponse.getItems() != null) {

                                                //       itemArrayList.addAll(loginResponse.getItems());
                                                //   attendanceMangementModelList.add((PunchInModel.Item) loginResponse.getItems());
                                                //     addAttendanceAdapter.notifyDataSetChanged();

                                                //itemList.addAll(loginResponse.getItems());

                                                //sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());

                            *//*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*//*
                                                //dixit
                                                // categoriesListAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        else
                                        {
                                            //Toast.makeText(this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        // Display the first 500 characters of the response string.
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //    loadingView.hideLoadingView();
                                Log.d("onResponse", "onErrorResponse: "+error.getMessage());
                *//*errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        getDataPunchIn(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        getActivity().onBackPressed();
                    }
                });
                errorView.showLoadingView();*//*

                            }
                        }){
                            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("emp_id", sessionManager.getId());
                                params.put("latitude", ""+latitude);
                                params.put("longitude", ""+longitude);
                                //params.put("password", password);
                                return params;
                            };
                        };

// Add the request to the RequestQueue.
                        stringRequest.setShouldCache(false);
                        queue.add(stringRequest);*/






                    }
                }



        }






    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void getDataAttendanceManagement(String id, String latitudeid, String longitudeid,String battery_percentage) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_punch_lat_long);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);

//                        Gson gson = new Gson();
                      //  AttendanceManagementModel1 loginResponse = gson.fromJson(response, AttendanceManagementModel1.class);
                       // if (loginResponse.getSuccess())
                       // {

//                            if (loginResponse.getItems() != null) {
//
//                         //       itemArrayList.addAll(loginResponse.getItems());
//                                //   attendanceMangementModelList.add((PunchInModel.Item) loginResponse.getItems());
//                           //     addAttendanceAdapter.notifyDataSetChanged();
//
//                                //itemList.addAll(loginResponse.getItems());
//
//                                //sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());
//
//                            /*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();*/
//                                //dixit
//                                // categoriesListAdapter.notifyDataSetChanged();
//                            }
                       // }
                       // else
                       // {
                            //Toast.makeText(this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        //}

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //    loadingView.hideLoadingView();
                Log.d("onResponse", "onErrorResponse: "+error.getMessage());
                /*errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        getDataPunchIn(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        getActivity().onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", id);
                params.put("latitude", latitudeid);
                params.put("longitude", longitudeid);
                params.put("charging", battery_percentage);

                Log.d("ddemp_id",id);
                Log.d("ddlatitude",latitudeid);
                Log.d("ddlongitude",latitudeid);
                Log.d("charging",battery_percentage);

                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }





}
