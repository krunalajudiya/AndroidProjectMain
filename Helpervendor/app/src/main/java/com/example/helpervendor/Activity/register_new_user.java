package com.example.helpervendor.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.helpervendor.MainActivity;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register_new_user extends AppCompatActivity {

    ImageView image_profile,image_id_proof;
    Spinner city;
    Button choose_file,Submit_button,choose_id_file;
    TextInputEditText name,password,confirmpassword,address,mobilenumber,pincode,visiting_rate;
    String city_name[]={"Jamnagar","Rajkot"};
    String name_str,password_str,confirm_password_str,address_str,mobilenumber_str,pincode_str,city_str,visiting_rate_str;
    File profile_file,id_proof_file;
    Uri profile_uri,id_proof_uri;

    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    private static final int PICK_FILE_REQUEST = 1001;
    private static final int PICK_FILE_REQUEST1 = 1002;
    int pos=0;
    ProgressDialog pdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirmpassword);
        address=findViewById(R.id.address);
        mobilenumber=findViewById(R.id.mobilenumber);
        pincode=findViewById(R.id.pincode);
        visiting_rate=findViewById(R.id.visiting_rate);

        image_profile=findViewById(R.id.image_profile);
        image_id_proof=findViewById(R.id.image_id_proof);
        city=findViewById(R.id.city_spinner);
        choose_file=findViewById(R.id.chose_file);
        choose_id_file=findViewById(R.id.chose_id_file);
        Submit_button=findViewById(R.id.Submit_button);


        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,city_name);
        city.setAdapter(arrayAdapter);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_str=city_name[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=1;
                Log.d("ff","click");
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile();
                    }
                }else {
                    ChooseFile();
                }
            }
        });
        choose_id_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=2;
                Log.d("ff","click");
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile1();
                    }
                }else {
                    ChooseFile1();
                }
            }
        });

        Submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                if (!TextUtils.equals(password_str,confirm_password_str))
                {
                    password.setError("Please Enter Check Password and confirm Password");
                    password.requestFocus();
                }else{
                    Register_user_detail();
                }


            }
        });

    }

    public void getvalue()
    {
        name_str=name.getText().toString().trim();
        mobilenumber_str=mobilenumber.getText().toString().trim();
        password_str=password.getText().toString().trim();
        confirm_password_str=confirmpassword.getText().toString();
        address_str=address.getText().toString().trim();
        pincode_str=pincode.getText().toString().trim();
        visiting_rate_str=visiting_rate.getText().toString().trim();
        if (profile_uri!=null)
        {
            profile_file= FileUtils.getFile(this,profile_uri);
        }
        if (id_proof_uri!=null)
        {
            id_proof_file= FileUtils.getFile(this,id_proof_uri);
        }

    }
    public  void Register_user_detail()
    {
        pdialog = new ProgressDialog(register_new_user.this);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();


        RequestBody requesttag=RequestBody.create(MediaType.parse("text/plain"), "Vendor_register");
        RequestBody requestname=RequestBody.create(MediaType.parse("text/plain"), name_str);
        RequestBody requestmobile=RequestBody.create(MediaType.parse("text/plain"), mobilenumber_str);
        RequestBody requestpassword=RequestBody.create(MediaType.parse("text/plain"), password_str);
        RequestBody requestaddress=RequestBody.create(MediaType.parse("text/plain"), address_str);
        RequestBody requestcity=RequestBody.create(MediaType.parse("text/plain"), city_str);
        RequestBody requestpincode=RequestBody.create(MediaType.parse("text/plain"), pincode_str);
        RequestBody requestvisitingrate=RequestBody.create(MediaType.parse("text/plain"), visiting_rate_str);


        MultipartBody.Part body;
        if (profile_uri!=null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_file);
            body = MultipartBody.Part.createFormData("photo", profile_file.getName(), requestBody);
        }else {
            body = MultipartBody.Part.createFormData("photo", "");
        }
        MultipartBody.Part id_body;
        if (id_proof_uri!=null) {
            RequestBody request_id_Body = RequestBody.create(MediaType.parse("multipart/form-data"), id_proof_file);
            id_body = MultipartBody.Part.createFormData("id_proof", id_proof_file.getName(), request_id_Body);
        }else {
            id_body = MultipartBody.Part.createFormData("id_proof", "");
        }
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Register_user(requesttag,requestname,requestmobile,requestpassword,body,requestaddress,requestcity,requestpincode,id_body,requestvisitingrate);
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Insertresultmodel insertresultmodel = response.body();
                    Log.d("fffff111", insertresultmodel.getError());
                    if (Integer.parseInt(insertresultmodel.getError()) == 200) {
                        Toast.makeText(getApplicationContext(),"Register Successfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                    if (Integer.parseInt(insertresultmodel.getError()) == 201) {
                        Toast.makeText(getApplicationContext(),"Already registered",Toast.LENGTH_LONG).show();


                    }
                    if (Integer.parseInt(insertresultmodel.getError()) == 202) {
                        Toast.makeText(getApplicationContext(),"Error,Please Check Details",Toast.LENGTH_LONG).show();


                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
                pdialog.dismiss();
            }
        });
    }
    private void ChooseFile() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST);

    }
    private void ChooseFile1() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST1);

    }
    private void pickImageFromGallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    ChooseFile();
                }else {
                    Toast.makeText(getApplicationContext(),"Permission denied....",Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST ){
            image_profile.setImageURI(data.getData());
            profile_uri=data.getData();
            Log.d("hello", String.valueOf(pos));
        }else if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST1 )
        {
            image_id_proof.setImageURI(data.getData());
            id_proof_uri=data.getData();
        }
    }

}