package com.example.shreejicabs.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Forgot_password extends AppCompatActivity {

    EditText newpassword,confirmpassword;
    Button forgotpassword;
    String number_str,newpassword_str,confirmpassword_str;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    private ProgressDialog pDialog;
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        newpassword=(EditText)findViewById(R.id.newpassword);
        confirmpassword=(EditText)findViewById(R.id.confirmpassword);
        forgotpassword=(Button)findViewById(R.id.forgotpassword);
        Bundle b=getIntent().getExtras();
        number_str=b.getString("number");
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                if (newpassword_str.isEmpty())
                {
                    newpassword.setError("Enter New Password");
                    newpassword.requestFocus();
                }else if (confirmpassword_str.isEmpty())
                {
                    confirmpassword.setError("Enter Confirm Password");
                    confirmpassword.requestFocus();
                }else if (!newpassword_str.equals(confirmpassword_str))
                {
                    newpassword.setError("Password Does Not Match");
                    newpassword.requestFocus();
                }else {
                    new Change_Password().execute();
                }
            }
        });
    }
    public void getvalue()
    {
        newpassword_str=newpassword.getText().toString();
        confirmpassword_str=confirmpassword.getText().toString();
    }
    class Change_Password extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Forgot_password.this,R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "set_new_password"));
                params.add(new BasicNameValuePair("password", newpassword_str));
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
                Toast.makeText(Forgot_password.this,"Change Password SuccessFully",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(Forgot_password.this,"Change Password Not Successfully",Toast.LENGTH_LONG).show();
            }

        }
    }

}