package com.example.helperfactory.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Location_screen extends AppCompatActivity {

    EditText name,address,city,pincode;
    Button continue_btn;
    Toolbar toolbar;
    UserSession userSession;
    Resultmodel.Data userdata;
    String name_str,address_str,city_str,pincode_str,sub_cat_id,cat_id,sub_cat_name;
    ProgressDialog pdialog;
    TextView price;
    Spinner city_spinner;
    int pos=0;
    ArrayList<String> city_ar=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(getString(R.string.app_name));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    onBackPressed();
                }
            });
        }
        city_ar.add("Please select city");
        city_ar.add("Rajkot");
        city_ar.add("Jamnagar");
        userSession=new UserSession(Location_screen.this);
        continue_btn=(Button)findViewById(R.id.continue_btn);
        name=(EditText)findViewById(R.id.profile_name);
        address=(EditText)findViewById(R.id.address);
        city=(EditText)findViewById(R.id.city);
        pincode=(EditText)findViewById(R.id.pincode);
        price=(TextView)findViewById(R.id.pricing);
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
        Bundle b=getIntent().getExtras();
        sub_cat_id=b.getString("sub_cat_id");
        cat_id=b.getString("cat_id");
        sub_cat_name=b.getString("sub_cat_name");
        Gson gson=new Gson();
        price.setText(b.getString("pricing"));
        userdata=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
        name.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z ]+")){
                            return cs;
                        }
                        else{
                            name.setError("Please Enter Only Character");
                        }
                        return cs;
                    }
                }
        });
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
            if (city_ar.contains(userdata.getCity()))
            {
                city_spinner.setSelection(city_ar.indexOf(userdata.getCity()));
            }
        }


        continue_btn.setOnClickListener(new View.OnClickListener() {
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
                }
                else {

                    Update_location();
                }

            }
        });


    }
    public void getvalue()
    {
        name_str=name.getText().toString().trim();
        address_str=address.getText().toString().trim();
        //city_str=city.getText().toString().trim();
        pincode_str=pincode.getText().toString().trim();
    }
    public  void Update_location()
    {
        pdialog = new ProgressDialog(Location_screen.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Log.d("sdsdsd",userdata.getClient_id());
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().UpdateProfile("Update_profile",userdata.getClient_id(),name_str,userdata.getPhoto(),"",address_str,city_str,pincode_str);
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
                        String user_detail;
                        Gson gson=new Gson();
                        user_detail=gson.toJson(userdata);
                        userSession.createLoginSession(user_detail);
                        if (Integer.parseInt(userdata.getMembership_status())==0) {
                            Toast.makeText(getApplicationContext(), "Update Location SuccessFully", Toast.LENGTH_LONG).show();
                            Intent schedule = new Intent(getApplicationContext(), Membership_plan.class);
                            schedule.putExtra("sub_cat_id", sub_cat_id);
                            schedule.putExtra("sub_cat_name",sub_cat_name);
                            schedule.putExtra("cat_id", cat_id);
                            startActivity(schedule);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Update Location SuccessFully", Toast.LENGTH_LONG).show();
                            Intent schedule = new Intent(getApplicationContext(), Schedule_booking.class);
                            schedule.putExtra("sub_cat_id", sub_cat_id);
                            schedule.putExtra("sub_cat_name",sub_cat_name);
                            schedule.putExtra("cat_id", cat_id);
                            startActivity(schedule);
                            finish();
                        }
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