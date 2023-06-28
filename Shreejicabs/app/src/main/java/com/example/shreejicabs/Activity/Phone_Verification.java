package com.example.shreejicabs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Phone_Verification extends AppCompatActivity {

    EditText number,otp;
    Button sendotp,submit;
    LinearLayout otplayout,numberlayout;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private ProgressDialog pDialog,pDialog1;
    int success;
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;
    String number_str;
    String username = "Chhayal";
    //Your authentication key
    String authkey = "6a626ced68XX";
    String senderid="TxTrip";
    String tempid="1207161846127082401";
    String otp_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone__verification);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        otp=(EditText)findViewById(R.id.otp);
        number=(EditText)findViewById(R.id.number);
        submit=(Button)findViewById(R.id.verification);
        sendotp=(Button)findViewById(R.id.sendotp);
        otplayout=(LinearLayout)findViewById(R.id.otplayout);
        numberlayout=(LinearLayout)findViewById(R.id.numberlayout);
        mAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                if(number_str.isEmpty() || number_str.length() < 10){
                    number.setError("Enter a valid mobile");
                    number.requestFocus();
                    return;
                }
                new Check_number().execute();
            }
        });
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otp.getText().toString();
                //verifyVerificationCode(code);
                if (!TextUtils.isEmpty(code)) {
                    //verifyVerificationCode(code);
                    //verify_otp(code,mobile_number_str);
                    if (TextUtils.equals(code,otp_str))
                    {
                        Log.d("Phone","Verification Successfully");
                        otplayout.setVisibility(View.GONE);
                        numberlayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Verify successfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),Forgot_password.class);
                        intent.putExtra("number",number_str);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Otp Is Wrong",Toast.LENGTH_LONG).show();
                    }
                }else{
                    otp.setError("Please Enter Otp");
                }
            }
        });


    }
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    public void send_verify(String mobile_number)
    {
//Prepare Url
        otp_str=getRandomNumberString();
        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;
        //Send SMS API
        String mainUrl="http://mobizz.hginfosys.co.in/sendsms.jsp?";
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("user="+username);
        sbPostData.append("&password="+authkey);
        sbPostData.append("&senderid="+senderid);
        sbPostData.append("&tempid="+tempid);
        sbPostData.append("&mobiles="+"+91"+mobile_number);
        sbPostData.append("&sms="+"Taxitrip otp is "+otp_str+", please don't share with anyone. SHREEJI CABS 'N' TAXI SERVICES");

        mainUrl = sbPostData.toString();
        Log.d("aaaa",mainUrl);
        try {
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                System.out.println(response);
            //finally close connection
            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void getvalue()
    {
        number_str=number.getText().toString();
    }
    class Check_number extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Phone_Verification.this,R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "verify_mobile"));
                params.add(new BasicNameValuePair("mo_no", number_str));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));

                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (success==200) {
                numberlayout.setVisibility(View.GONE);
                otplayout.setVisibility(View.VISIBLE);
                //sendVerificationCode(number_str);
                send_verify(number_str);
                //Toast.makeText(getActivity(),"Add car SuccessFully",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(Phone_Verification.this,"Number Are Not Register",Toast.LENGTH_LONG).show();
            }

        }
    }
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Phone_Verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        pDialog1 = new ProgressDialog(Phone_Verification.this,R.style.MyAlertDialogStyle);
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
                .addOnCompleteListener(Phone_Verification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pDialog1.dismiss();
                            //verification successful we will start the profile activity
                            Intent intent=new Intent(getApplicationContext(),Forgot_password.class);
                            intent.putExtra("number",number_str);
                            startActivity(intent);
                            finish();
                            Log.d("Phone","Verification Successfully");
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
}