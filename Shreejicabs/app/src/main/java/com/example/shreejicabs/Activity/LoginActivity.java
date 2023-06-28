package com.example.shreejicabs.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {



    TextView register_now,forgot_pass;
    private static final int REQUEST_PERMISSION = 1000;
    EditText mobile_number,password;
    String mobile_number_str,password_Str,type_str;
    RadioButton travel_partner,driver,pessenger;
    RadioGroup radioGroup;
    Button login;

    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private static final String TAG_MESSAGE = "error_msg";
    private ProgressDialog pDialog;
    int success;
    String reg_id,name_str,mobile_str,email_str,user_detail,city_pref_str;
    User user;
    UserSession userSession;
    String token,androidDeviceId;

    private static final int UPDATE_REQ = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register_now=(TextView)findViewById(R.id.register_now);
        mobile_number=(EditText)findViewById(R.id.number);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login_button);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        travel_partner=(RadioButton)findViewById(R.id.travelpartner);
        driver=(RadioButton)findViewById(R.id.driver);
        pessenger=(RadioButton)findViewById(R.id.pessenger);
        forgot_pass=(TextView)findViewById(R.id.forgot_pass);
        userSession=new UserSession(getApplicationContext());


        //Updateapp();
        register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(register);
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot=new Intent(getApplicationContext(),Phone_Verification.class);
                startActivity(forgot);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (travel_partner.isChecked())
                {
                    type_str=travel_partner.getText().toString();


                }else if (driver.isChecked())
                {
                    type_str=driver.getText().toString();
                }else if (pessenger.isChecked())
                {
                    type_str=pessenger.getText().toString();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
                if (mobile_number_str.isEmpty())
                {
                    mobile_number.setError("Enter Mobile Number");
                    mobile_number.requestFocus();
                }else if (password_Str.isEmpty())
                {
                    password.setError("Enter Password");
                    password.requestFocus();
                }else if (mobile_number.length() > 10 || mobile_number.length() < 10)
                {
                    mobile_number.setError("Enter 10 Digit Mobile Number");
                    mobile_number.requestFocus();
                }
                else {
                    new Login().execute();
                }

            }
        });
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }

    }
    public void getvalue()
    {
        mobile_number_str=mobile_number.getText().toString().trim();
        password_Str=password.getText().toString().trim();

    }
    private class Login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this,R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                token = FirebaseInstanceId.getInstance().getToken();
                Log.d("token",token);
                Log.d("device id",androidDeviceId);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params .add(new BasicNameValuePair("tag", "login"));
                params .add(new BasicNameValuePair("Mobile", mobile_number_str));
                params .add(new BasicNameValuePair("Password", password_Str));
                params .add(new BasicNameValuePair("type", type_str));
                params.add(new BasicNameValuePair("deviceuid",androidDeviceId));
                params.add(new BasicNameValuePair("devicetoken",token));

                JSONObject jsonObject =jParser.makeHttpRequest(Constants.url, "POST", params);

                // check your log for json response
                Log.d("Register Details", jsonObject.toString());
                success = jsonObject.getInt(TAG_SUCCESS);
                if (success==200)
                {
                    JSONObject jsonobject1=jsonObject.getJSONObject("data");
                    reg_id=jsonobject1.getString("Reg_id");
                    name_str=jsonobject1.getString("Name");
                    mobile_str=jsonobject1.getString("Mobile_no");
                    email_str=jsonobject1.getString("Email");
                    city_pref_str=jsonobject1.getString("City_preference");
                    user=new User(reg_id,name_str,mobile_str,email_str,type_str,city_pref_str);



                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(success == 200){
                //successfully created the product
                Gson gson=new Gson();
                user_detail=gson.toJson(user);
                userSession.createLoginSession(user_detail);
                userSession.settoken(false);
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this, "Mobile number and Password is wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                            ,LoginActivity.this,UPDATE_REQ);
                }catch (Exception e){

                }

            }
        });
    }

}
