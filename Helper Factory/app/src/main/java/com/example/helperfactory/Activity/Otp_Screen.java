package com.example.helperfactory.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperfactory.Adapter.CategoryAdapter;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Otp_Screen extends AppCompatActivity implements OnOtpCompletionListener  {

    OtpView otpView;
    String mobile_number_str;
    private FirebaseAuth mAuth;
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    private ProgressDialog pDialog1;
    Toolbar toolbar;
    TextView verificationnumber;
    UserSession userSession;
    TextView resend;
    String androidDeviceId,token;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String refercode_str,city_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.loginsignup));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    onBackPressed();
                }
            });
        }

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("ddDeviceId","DeviceId : "+androidDeviceId);
        token = FirebaseInstanceId.getInstance().getToken();
        Log.i("ddDeviceId", "FCM Registration Token: " + token);

        userSession=new UserSession(Otp_Screen.this);
        resend=(TextView)findViewById(R.id.resend);
        otpView=(OtpView)findViewById(R.id.otp_view);
        verificationnumber=(TextView)findViewById(R.id.verficationnumber);
        otpView.setOtpCompletionListener(this);
        Bundle b=getIntent().getExtras();
        mobile_number_str=b.getString("mobile_number");
        city_str=b.getString("city");
        verificationnumber.setText("+91 "+mobile_number_str);
        if(b.getString("refercode")!=null){
            refercode_str=b.getString("refercode");
        }else {
            refercode_str="";
        }
        Log.d("ruffercode",refercode_str);
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(mobile_number_str);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mobile_number_str,mResendToken);
                resend.setVisibility(View.GONE);
                resendDisable();
            }
        });


    }
    private void resendDisable(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resend.setVisibility(View.VISIBLE);
            }
        },180000);
    }
    private void sendVerificationCode(String mobile) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //Log.d("helloss",code);
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpView.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }else{
                Log.d("ff_otp_else","else");
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Otp_Screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("sdksdk",e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d("helloss11","Sdsdsdsd");
            //storing the verification id that is sent to the user
            mVerificationId = s;
            mResendToken=forceResendingToken;
        }
    };
    private void verifyVerificationCode(String code) {
        pDialog1 = new ProgressDialog(Otp_Screen.this,R.style.MyAlertDialogStyle);
        pDialog1.setMessage("Please wait...");
        pDialog1.setIndeterminate(false);
        pDialog1.setCancelable(false);
        pDialog1.show();
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Otp_Screen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pDialog1.dismiss();
                            //verification successful we will start the profile activity
                            Log.d("Phone","Verification Successfully");
                            register_user();


                        } else {

                            pDialog1.dismiss();
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Log.d("something",message);


                        }
                    }
                });
    }
    public void register_user()
    {
        Call<Resultmodel> call = Retrofitclient.getInstance().getMyApi().Register("First_Step",mobile_number_str,androidDeviceId,token,refercode_str,city_str);
        call.enqueue(new Callback<Resultmodel>() {
            @Override
            public void onResponse(Call<Resultmodel> call, Response<Resultmodel> response) {

                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Resultmodel resultmodel = response.body();
                    Resultmodel.Data data=resultmodel.getData();
                    Log.d("fffff111", resultmodel.getError_msg());
                    if (Integer.parseInt(resultmodel.getError()) == 200) {
                        Gson gson=new Gson();
                        String user_detail=gson.toJson(data);
                        userSession.createLoginSession(user_detail);
                        userSession.setKeyToken(token);
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                        finish();
                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Resultmodel> call, Throwable t) {
                //handle error or failure cases here
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }

    @Override
    public void onOtpCompleted(String otp) {
        verifyVerificationCode(otp);
        //Toast.makeText(this, "OnOtpCompletionListener called", Toast.LENGTH_SHORT).show();
    }
}