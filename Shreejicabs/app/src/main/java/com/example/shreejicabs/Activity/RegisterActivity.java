package com.example.shreejicabs.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Reciever.SmsBroadcastReceiver;
import com.example.shreejicabs.Remote.IUploadAPI;
import com.example.shreejicabs.Remote.RetrofitClient;
import com.example.shreejicabs.Utils.ProgressRequestBody;
import com.example.shreejicabs.Utils.UploadCallBacks;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements UploadCallBacks {

    EditText name,mobile_number,email,company_name,address,city,website,experience,password,confimpassword,otpedt,referralcode;
    String name_str,mobile_number_str,email_str,company_name_str,address_str,city_str,website_str,experience_str,licence_str,password_str,confirm_passwordstr,type_str,referral_str;
    Button register,choose_licence,sendotp,verfiy;
    ImageView licence_img;
    RadioButton travelpartner,driver,pessenger;
    LinearLayout driverlayout;
    RadioGroup radioGroup;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private static final String TAG_MESSAGE = "error_msg";
    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    int success;
    Uri selectlicence;
    private static final int PICK_FILE_REQUEST = 1001;
    //Retrofit
    public static final String BASE_URL = Constants.BASE_URL;
    IUploadAPI mService;
    private ProgressDialog progressDialog;
    LinearLayout otplayout;
    TextView login_now;

    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;
    String username = "Chhayal";
    //Your authentication key
    String authkey = "6a626ced68XX";
    String senderid="TxTrip";
    String tempid="1207161846127082401";
    String otp;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    String token,androidDeviceId;


    private IUploadAPI getAPIUpload()
    {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        mService = getAPIUpload();
        login_now=(TextView)findViewById(R.id.login_now);
        name=(EditText)findViewById(R.id.name);
        mobile_number=(EditText)findViewById(R.id.number);
        email=(EditText)findViewById(R.id.email);
        company_name=(EditText)findViewById(R.id.companyname);
        address=(EditText)findViewById(R.id.address);
        city=(EditText)findViewById(R.id.city);
        website=(EditText)findViewById(R.id.website);
        experience=(EditText)findViewById(R.id.experience);
        password=(EditText)findViewById(R.id.password);
        confimpassword=(EditText)findViewById(R.id.confirmpassword);
        register=(Button) findViewById(R.id.register);
        choose_licence=(Button) findViewById(R.id.chooselicence);
        licence_img=(ImageView) findViewById(R.id.driving_license_img);
        travelpartner=(RadioButton)findViewById(R.id.travelpartner);
        driver=(RadioButton)findViewById(R.id.driver);
        pessenger=(RadioButton)findViewById(R.id.pessenger);
        driverlayout=(LinearLayout)findViewById(R.id.driverlayout);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        otplayout=(LinearLayout)findViewById(R.id.otplayout);
        otpedt=(EditText)findViewById(R.id.otpedt);
        sendotp=(Button)findViewById(R.id.send);
        verfiy=(Button)findViewById(R.id.verify);
        referralcode=(EditText) findViewById(R.id.referralcode);
        register.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_number_str=mobile_number.getText().toString().trim();
                send_verify(mobile_number_str);
                otplayout.setVisibility(View.VISIBLE);
                //sendVerificationCode(mobile_number_str);
            }
        });
        verfiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otpedt.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    //verifyVerificationCode(code);
                    //verify_otp(code,mobile_number_str);
                    if (TextUtils.equals(code,otp))
                    {
                        register.setEnabled(true);
                        Log.d("Phone","Verification Successfully");
                        otplayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Verify successfully",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Otp Is Wrong",Toast.LENGTH_LONG).show();
                    }
                }else{
                    otpedt.setError("Please Enter Otp");
                }
            }
        });

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("token",token);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (pessenger.isChecked())
                {
                    driverlayout.setVisibility(View.GONE);
                }else if (travelpartner.isChecked())
                {
                    experience.setVisibility(View.GONE);
                    choose_licence.setVisibility(View.GONE);
                    licence_img.setVisibility(View.GONE);
                    company_name.setVisibility(View.VISIBLE);
                    website.setVisibility(View.VISIBLE);
                    driverlayout.setVisibility(View.VISIBLE);

                }else if (driver.isChecked())
                {
                    experience.setVisibility(View.VISIBLE);
                    choose_licence.setVisibility(View.VISIBLE);
                    licence_img.setVisibility(View.VISIBLE);
                    company_name.setVisibility(View.GONE);
                    website.setVisibility(View.GONE);
                    driverlayout.setVisibility(View.VISIBLE);
                }
            }
        });
        login_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login);
            }
        });
        choose_licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseFile();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
                if (!TextUtils.equals(password_str,confirm_passwordstr))
                {
                    password.setError("Please Check Password");
                }else if (TextUtils.isEmpty(name_str))
                {
                    name.setError("Please Enter Name");

                }else if (TextUtils.isEmpty(mobile_number_str))
                {
                    mobile_number.setError("Please Enter Mobile Number");
                }else if (TextUtils.isEmpty(email_str))
                {
                    email.setError("Please Enter Email");

                }else if (TextUtils.isEmpty(password_str)){
                    password.setError("Please Enter Password");

                }else if (TextUtils.isEmpty(confirm_passwordstr)) {
                    confimpassword.setError("Please Enter Confirm Password");

                }
                else {
                    if (driver.isChecked()) {
                        uploadFile(selectlicence, licence_str);
                        new Register().execute();
                    } else {
                        new Register().execute();
                    }
                }
            }
        });
        startSmsUserConsent();
    }
    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            otpedt.setText(matcher.group(0));
            String code=otpedt.getText().toString();
            if (TextUtils.equals(code,otp))
            {
                register.setEnabled(true);
                Log.d("Phone","Verification Successfully");
                otplayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Verify successfully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Otp Is Wrong",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
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
        otp=getRandomNumberString();
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
        sbPostData.append("&sms="+"Taxitrip otp is "+otp+", please don't share with anyone. SHREEJI CABS 'N' TAXI SERVICES");

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
    public void verify_otp(String otp,String mobile_number)
    {
        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;
        //Send SMS API
        String mainUrl="http://mobizz.hginfosys.co.in/validateOtpApi.jsp?";
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("mobileno="+"+91"+mobile_number);
        sbPostData.append("&otp="+otp);
        mainUrl = sbPostData.toString();
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
                otpedt.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }else{
                Log.d("ssss",phoneAuthCredential.getSignInMethod());
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        pDialog1 = new ProgressDialog(RegisterActivity.this,R.style.MyAlertDialogStyle);
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
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pDialog1.dismiss();
                            //verification successful we will start the profile activity
                            register.setEnabled(true);
                            Log.d("Phone","Verification Successfully");
                            otplayout.setVisibility(View.GONE);
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
    private void ChooseFile() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }
    private void uploadFile(Uri selectUri, String name) {
        if(selectUri != null){
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading.....");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
//            progressDialog.show();

            File file = FileUtils.getFile(this, selectUri);
            ProgressRequestBody requestFile = new ProgressRequestBody(file,this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "uploaded_file", name, requestFile);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    mService.uploadFile(body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
//                                    progressDialog.dismiss();

                                    //Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
//                                    progressDialog.dismiss();
                                    //Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }
    }
    public void getvalue()
    {
        name_str=name.getText().toString().trim();
        mobile_number_str=mobile_number.getText().toString().trim();
        email_str=email.getText().toString().trim();
        company_name_str=company_name.getText().toString().trim();
        address_str=address.getText().toString().trim();
        city_str=city.getText().toString().trim();
        website_str=website.getText().toString().trim();
        experience_str=experience.getText().toString().trim();
        password_str=password.getText().toString().trim();
        confirm_passwordstr=confimpassword.getText().toString().trim();
        if (travelpartner.isChecked())
        {
            type_str=travelpartner.getText().toString();

        }else if (driver.isChecked())
        {
            type_str=driver.getText().toString();
        }
        else if (pessenger.isChecked())
        {
            type_str=pessenger.getText().toString();
        }
        if(selectlicence != null){
            File file = FileUtils.getFile(getApplicationContext(), selectlicence);
            licence_str = getSaltString() + file.getName();
        }
        if (referralcode.getText().toString()!=null){
            referral_str=referralcode.getText().toString().trim();
        }else {
            referral_str="";
        }

    }
    private class Register extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this,R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params .add(new BasicNameValuePair("tag", "register"));
                params .add(new BasicNameValuePair("Name", name_str));
                params .add(new BasicNameValuePair("Mobile", mobile_number_str));
                params .add(new BasicNameValuePair("Email", email_str));
                params .add(new BasicNameValuePair("Password", password_str));
                params .add(new BasicNameValuePair("Company_name", company_name_str));
                params .add(new BasicNameValuePair("Address", address_str));
                params .add(new BasicNameValuePair("City", city_str));
                params .add(new BasicNameValuePair("Website", website_str));
                params .add(new BasicNameValuePair("Experience", experience_str));
                params .add(new BasicNameValuePair("Policy", ""));
                params .add(new BasicNameValuePair("Licence", licence_str));
                params .add(new BasicNameValuePair("Proof_id", ""));
                params .add(new BasicNameValuePair("User_type", type_str));
                params.add(new BasicNameValuePair("referral_code",referral_str));

                JSONObject jsonObject =jParser.makeHttpRequest(Constants.url, "POST", params);

                // check your log for json response
                Log.d("Register Details", jsonObject.toString());
                success = jsonObject.getInt(TAG_SUCCESS);

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
                Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(RegisterActivity.this, "Mobile Number already register", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }
    protected String getSaltString() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILE_REQUEST:
                if (resultCode == RESULT_OK) {
                    selectlicence = data.getData();
                    licence_img.setImageURI(selectlicence);
                }
                break;


        }
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
             //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                //textViewMessage.setText(String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message);
            }
        }

    }


}
