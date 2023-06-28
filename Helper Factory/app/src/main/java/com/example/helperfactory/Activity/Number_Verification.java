package com.example.helperfactory.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperfactory.MainActivity;
import com.example.helperfactory.R;
import com.example.helperfactory.Session.UserSession;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;

public class Number_Verification extends AppCompatActivity {

    EditText mobilenumber;
    Button login,skip;
    UserSession userSession;
    CheckBox privacycheck;
    TextView readmore,refercode;
    String refercode_str=null,city_str;
    Spinner city_spinner;
    int pos=0;
    ArrayList<String> city_ar=new ArrayList<>();
    private static final int UPDATE_REQ = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number__verification);

        Updateapp();
        userSession=new UserSession(Number_Verification.this);
        if (userSession.isLoggedIn())
        {
            Intent main=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(main);
            finish();
            return;
        }
        mobilenumber=(EditText)findViewById(R.id.mobilenumber);
        privacycheck=(CheckBox)findViewById(R.id.privacycheck);
        readmore=(TextView)findViewById(R.id.readmore);
        login=(Button)findViewById(R.id.login);
        skip=(Button)findViewById(R.id.skip);
        refercode=(TextView)findViewById(R.id.ruffercode);
        city_spinner=(Spinner)findViewById(R.id.city_spinner);

        city_ar.add("Please select city");
        city_ar.add("Rajkot");
        city_ar.add("Jamnagar");

        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,city_ar);
        city_spinner.setAdapter(arrayAdapter);
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                city_str=city_ar.get(position);
                Log.d("city", String.valueOf(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mobilenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobile_number_str=mobilenumber.getText().toString();
//                if (mobile_number_str.length()==10)
//                {
//                    login.setVisibility(View.VISIBLE);
//                }else{
//                    login.setVisibility(View.GONE);
//                }


            }
        });
        privacycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobilenumber.getText().toString().trim().length()>0 && privacycheck.isChecked() && pos!=0){
                    String mobile_number_str = mobilenumber.getText().toString();
                    refercode_str=refercode.getText().toString();
                    Intent verify = new Intent(getApplicationContext(), Otp_Screen.class);
                    verify.putExtra("mobile_number", mobile_number_str);
                    verify.putExtra("refercode",refercode_str);
                    verify.putExtra("city",city_str);
                    startActivity(verify);
                }

                if (mobilenumber.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_LONG).show();
                }
                else if (pos==0){
                    Toast.makeText(getApplicationContext(),"Please select city",Toast.LENGTH_LONG).show();
                }else if (!privacycheck.isChecked()){
                    Toast.makeText(getApplicationContext(),"Privacy Policy consent is required",Toast.LENGTH_LONG).show();
                }

            }
        });
        readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Privacy_policy.class);
                startActivity(intent);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
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
                            ,Number_Verification.this,UPDATE_REQ);
                }catch (Exception e){

                }

            }
        });
    }

}