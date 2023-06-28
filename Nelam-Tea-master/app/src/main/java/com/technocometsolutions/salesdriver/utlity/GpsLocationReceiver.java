package com.technocometsolutions.salesdriver.utlity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technocometsolutions.salesdriver.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GpsLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.requireNonNull(intent.getAction()).matches("android.location.PROVIDERS_CHANGED")) {

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                SessionManager sessionManager=new SessionManager(context);
                sessionManager.getId();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url;
                url = context.getString(R.string.json_gps_disabled);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("onResponseGPS", "onResponse: "+response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onResponseGPS", "onErrorResponse: "+error.getMessage());
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
            }else{
                SessionManager sessionManager=new SessionManager(context);
                sessionManager.getId();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url;
                url = context.getString(R.string.json_gps_enabled);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("onResponseGPS0", "onResponse: "+response);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onResponseGPS0", "onErrorResponse: "+error.getMessage());
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
        }
    }




}

