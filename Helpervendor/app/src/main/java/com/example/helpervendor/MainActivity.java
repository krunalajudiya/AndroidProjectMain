package com.example.helpervendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpervendor.Activity.Home;
import com.example.helpervendor.Activity.PrivacyPolicy;
import com.example.helpervendor.Activity.all_work_category;
import com.example.helpervendor.Activity.register_new_user;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView create_account;
    TextInputEditText mobilenumber, password;
    String mobilenumber_str, password_str;
    Button Login_button;
    UserSession userSession;
    ProgressDialog pdialog;
    CheckBox privacycheck;
    TextView readmore;
    String androidDeviceId,token;
    private static final int UPDATE_REQ = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Updateapp();

        Log.d("main","mainactivity");
        userSession = new UserSession(getApplicationContext());
        mobilenumber = findViewById(R.id.mobilenumber);
        password = findViewById(R.id.password);
        create_account = findViewById(R.id.create_account);
        Login_button = findViewById(R.id.login_buton);
        privacycheck = (CheckBox) findViewById(R.id.privacycheck);
        readmore = (TextView) findViewById(R.id.readmore);
        privacycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(getApplicationContext(),all_work_category.class);
                startActivity(intent);*/
                getvalue();
                if (privacycheck.isChecked()) {
                    if (TextUtils.isEmpty(mobilenumber_str)) {
                        mobilenumber.setError("Please enter mobile number");
                        mobilenumber.requestFocus();
                    } else if (TextUtils.isEmpty(password_str)) {
                        password.setError("Please enter password");
                        password.requestFocus();
                    } else {
                        Log.d("fffff111", "sdd");
                        Login_user();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Privacy Policy consent is required",Toast.LENGTH_LONG).show();
                }
            }
        });
        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("ddDeviceId","DeviceId : "+androidDeviceId);
        token = FirebaseInstanceId.getInstance().getToken();
        Log.i("ddDeviceId", "FCM Registration Token: " + token);


        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register_new_user.class);
                startActivity(intent);
            }
        });
        readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent readmoreactivity=new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(readmoreactivity);
            }
        });
    }


    public void getvalue() {
        mobilenumber_str = mobilenumber.getText().toString().trim();
        password_str = password.getText().toString().trim();
    }

    public void Login_user() {
        pdialog = new ProgressDialog(MainActivity.this);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();

        Call<Resultmodel> call = Retrofitclient.getInstance().getMyApi().Login("login", mobilenumber_str, password_str,androidDeviceId,token);
        call.enqueue(new Callback<Resultmodel>() {
            @Override
            public void onResponse(Call<Resultmodel> call, Response<Resultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Resultmodel resultmodel = response.body();
                    Resultmodel.Data data = resultmodel.getData();

                    if (Integer.parseInt(resultmodel.getError()) == 200) {
                        Gson gson = new Gson();
                        String user_detail = gson.toJson(data);
                        Log.d("fffff111", user_detail);
                        userSession.setKeyToken(token);
                        Toast.makeText(getApplicationContext(), "Login Successfuly", Toast.LENGTH_LONG).show();
                        userSession.createLoginSession(user_detail);
                        Intent home = new Intent(getApplicationContext(), Home.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                        finish();
                    }
                    if (Integer.parseInt(resultmodel.getError()) == 201) {

                       // Log.d("fffff111", user_detail);
                        Toast.makeText(getApplicationContext(), "Approval Pending try again later", Toast.LENGTH_LONG).show();


                    }
                    if (Integer.parseInt(resultmodel.getError()) == 202) {

                        // Log.d("fffff111", user_detail);
                        Toast.makeText(getApplicationContext(), "PhoneNo or Password Wrong", Toast.LENGTH_LONG).show();


                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Resultmodel> call, Throwable t) {
                pdialog.dismiss();
                //handle error or failure cases here
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff", t.getMessage());
            }
        });
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

}