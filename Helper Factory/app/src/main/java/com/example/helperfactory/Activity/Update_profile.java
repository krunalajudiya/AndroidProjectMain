package com.example.helperfactory.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_profile extends AppCompatActivity {

    CircleImageView profile_image,profile_btn;
    EditText name,address,city,pincode;
    String name_str,address_str,city_str,pincode_str,profile_img_str,client_id;
    private static final int REQUEST_PERMISSION = 1000;
    private static final int PICK_FILE_REQUEST = 1001;
    Uri profile_uri;
    File profile_file;
    Button update_profile;
    UserSession userSession;
    Resultmodel.Data userdata;
    ProgressDialog pdialog;
    Spinner city_spinner;
    int pos=0;
    ArrayList<String> city_ar=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        city_ar.add("Please select city");
        city_ar.add("Rajkot");
        city_ar.add("Jamnagar");
        userSession=new UserSession(Update_profile.this);
        profile_image=(CircleImageView)findViewById(R.id.profile_img);
        profile_btn=(CircleImageView)findViewById(R.id.profile_btn);
        name=(EditText)findViewById(R.id.profile_name);
        address=(EditText)findViewById(R.id.address);
        city=(EditText)findViewById(R.id.city);
        pincode=(EditText)findViewById(R.id.pincode);
        update_profile=(Button)findViewById(R.id.update_profile);
        city_spinner=(Spinner)findViewById(R.id.city_spinner);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,city_ar);
        city_spinner.setAdapter(arrayAdapter);
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                city_str=city_ar.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Gson gson=new Gson();
        userdata=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
        if (userSession.isLoggedIn()) {
            if (userdata.getClient_name() != null) {
                name.setText(userdata.getClient_name());
            }
            if (userdata.getAddress() != null) {
                address.setText(userdata.getAddress());
            }
            if (userdata.getCity() != null) {
                city.setText(userdata.getCity());

            }
            if (userdata.getPincode() != null) {
                pincode.setText(userdata.getPincode());
            }
//            Log.d("city",userdata.getCity());
            if (city_ar.contains(userdata.getCity()))
            {
                city_spinner.setSelection(city_ar.indexOf(userdata.getCity()));
                Log.d("city",userdata.getCity());
            }
            //Log.d("profile_img",userdata.getPhoto());
            if (userdata.getPhoto()!=null) {
                Glide.with(getApplicationContext()).load(Constant.IMAGE_URL + userdata.getPhoto()).into(profile_image);
            }
        }
        //check Permission
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSION);
        }
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(Update_profile.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Update_profile.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_PERMISSION);
                }else{
                    ChooseFile();
                }
            }
        });
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getvalue();
                if (!name_str.matches("[a-zA-Z ]+"))
                {
                    name.setError("Please Enter Only Character");
                    name.requestFocus();
                }else if (TextUtils.isEmpty(pincode_str))
                {
                    pincode.setError("Please Enter Pincode");
                    pincode.requestFocus();
                }
                else if (TextUtils.isEmpty(address_str))
                {
                    address.setError("Please Enter address");
                    address.requestFocus();
                }
                else if (pos==0)
                {
                    Toast.makeText(getApplicationContext(),"Please select city",Toast.LENGTH_LONG).show();
                }else {
                    Update_profile_detail();
                }

            }
        });


    }
    public  void getvalue()
    {
        name_str=name.getText().toString();
        address_str=address.getText().toString();
        //city_str=city.getText().toString();
        pincode_str=pincode.getText().toString();
        if (profile_uri!=null)
        {
            profile_file=FileUtils.getFile(this,profile_uri);
        }else{

        }

    }
    private void ChooseFile() {
        Intent intent = Intent.createChooser(
                FileUtils.createGetContentIntent("image/*"), "Select a file");
        startActivityForResult(intent, PICK_FILE_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ChooseFile();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                profile_uri = data.getData();
                profile_image.setImageURI(profile_uri);

            }
        }
    }
    public  void Update_profile_detail()
    {
        pdialog = new ProgressDialog(Update_profile.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Log.d("sdsdsd",userdata.getClient_id());

        RequestBody requesttag=RequestBody.create(MediaType.parse("text/plain"), "Update_profile");
        RequestBody requestclient_id=RequestBody.create(MediaType.parse("text/plain"), userdata.getClient_id());
        RequestBody requestname=RequestBody.create(MediaType.parse("text/plain"), name_str);
        RequestBody requestemail=RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody requestaddress=RequestBody.create(MediaType.parse("text/plain"), address_str);
        RequestBody requestcity=RequestBody.create(MediaType.parse("text/plain"), city_str);
        RequestBody requestpincode=RequestBody.create(MediaType.parse("text/plain"), pincode_str);
        MultipartBody.Part body;
        if (profile_uri!=null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_file);
            body = MultipartBody.Part.createFormData("Photo", profile_file.getName(), requestBody);
        }else {
            body = MultipartBody.Part.createFormData("Photo", "");
        }
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Update_Profile_detail(requesttag,requestclient_id,requestname,body,requestaddress,requestcity,requestpincode);
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
                        userdata.setAddress(address_str);
                        userdata.setCity(city_str);
                        userdata.setPincode(pincode_str);
                        userdata.setClient_name(name_str);
                        if (profile_uri!=null)
                        {
                            userdata.setPhoto(profile_file.getName());
                        }
                        String user_detail;
                        Gson gson=new Gson();
                        user_detail=gson.toJson(userdata);
                        userSession.createLoginSession(user_detail);
                        Toast.makeText(getApplicationContext(),"Update Profile SuccessFully",Toast.LENGTH_LONG).show();
                        Intent update=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(update);
                        finish();
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
}