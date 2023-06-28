package com.technocometsolutions.salesdriver.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.technocometsolutions.salesdriver.model.LoginResponsModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.technocometsolutions.salesdriver.R;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginScreenActivity extends AppCompatActivity {
    Button btn_login_login;
    EditText et_userName, et_password;
    private String TAG = "LoginScreenActivity";
    public LoadingView loadingView;
    public ErrorView errorView;
    public List<LoginResponsModel.Item> itemList = new ArrayList<>();
    public SessionManager sessionManager;
    private String androidDeviceId;
    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_password = (EditText) findViewById(R.id.et_password);
        String ts = Context.TELEPHONY_SERVICE;

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("ddDeviceId","DeviceId : "+androidDeviceId);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("ddDeviceId", "FCM Registration Token: " + token);
        sessionManager.setFirebaseToken(token);
        sessionManager.setDeviceId(androidDeviceId);


        FloatingActionButton fab = findViewById(R.id.fab);
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        permisssion();
        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=et_userName.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                if (userName.equalsIgnoreCase(""))
                {
                    Toast.makeText(LoginScreenActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                }
                else if (password.equalsIgnoreCase(""))
                {
                    Toast.makeText(LoginScreenActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getDataLogin_apns_device(sessionManager.getKeyDeviceId(),sessionManager.getKEY_firebase_token(),"Android",userName,password);
                    Log.d("DEviceId",""+sessionManager.getKeyDeviceId()+""+sessionManager.getKEY_firebase_token());
                   // permisssion(userName,password);

                }
                /*Intent intent=new Intent(LoginScreenActivity.this,MainActivity.class);
                startActivity(intent);*/
            }
        });

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

                        /* Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
*/
                        Toast.makeText(LoginScreenActivity.this, "You Must Have accept this location", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();
                    }


                }).check();

    }


    public void getDataLogin(String userName, String password, String deviceuid) {

        loadingView = new LoadingView(this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_login);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Dexter.withActivity(LoginScreenActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response1) {
                                        Gson gson = new Gson();
                                        LoginResponsModel loginResponse = gson.fromJson(response, LoginResponsModel.class);
                                        if (loginResponse.getSuccess())
                                        {
                                            if (loginResponse.getItems() != null) {
                                                itemList.addAll(loginResponse.getItems());

                                                sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());
                                                sessionManager.setUsername(itemList.get(0).getFirstName()+itemList.get(0).getLastName());
                                                sessionManager.setUserID(itemList.get(0).getEmpCode());
                                                sessionManager.setKeyChargePerKm(itemList.get(0).getCharge_per_km());
                                                Intent intent=new Intent(LoginScreenActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                //dixit
                                                // categoriesListAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(LoginScreenActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        //requestPermissionOfLocation();
                                        //getDataLogin(userName,password,deviceuid);
                                        //finish();
                        /* Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
*/
                                        Toast.makeText(LoginScreenActivity.this, "You Must Have accept this location", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                                        token.continuePermissionRequest();
                                    }


                                }).check();






                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(LoginScreenActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        getDataLogin(userName,password,deviceuid);
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userName);
                params.put("password", password);
                params.put("deviceuid", deviceuid);
                return params;
            };
        };

        /*StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", storeName);
                params.put("type", couponType);
                return params;
            };
        };*/


// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    public void getDataLogin_apns_device(String deviceuid, String devicetoken, String devicename, String userName, String password) {
        loadingView = new LoadingView(this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_login_apns_device);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        loadingView.hideLoadingView();
                        getDataLogin(userName,password,deviceuid);

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(LoginScreenActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                      //  getDataLogin(userName,password);
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceuid", deviceuid);
                params.put("devicetoken", devicetoken);
                params.put("devicename", devicename);
                return params;
            };
        };

        /*StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", storeName);
                params.put("type", couponType);
                return params;
            };
        };*/


// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //setButtonsEnabledState();
                    //startLocationUpdates();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                       // Toast.makeText(MainActivity.this, "このアプリの機能を有効にするには端末の設定画面からアプリの位置情報パーミッションを有効にして下さい。", Toast.LENGTH_SHORT).show();
                    } else {
                        //showRationaleDialog();
                        permisssion();
                    }
                }
                break;
            }
        }
    }

}