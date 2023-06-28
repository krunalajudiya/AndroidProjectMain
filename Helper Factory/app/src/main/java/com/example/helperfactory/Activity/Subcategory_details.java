package com.example.helperfactory.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Membershipmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.Model.Subcategorymodel;
import com.example.helperfactory.Model.Usermembershipmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subcategory_details extends AppCompatActivity {

    TextView sub_cat_name,sub_cat_desc,duration,turnaroundtime,pricing,includetxt,discount_price_show;
    LinearLayout includelayout;
    Button book_now;
    Subcategorymodel.SubCategory subCategory;
    String sub_cat_detail,cat_id;
    List<String> include_list=new ArrayList<>();
    Toolbar toolbar;
    ImageView subcategoryimg;
    UserSession userSession;
    List<Usermembershipmodel.UserMembership> membershipList=new ArrayList<>();
    ProgressDialog pdialog;
    Resultmodel.Data userdata;

    String discount_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_details);
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
        Gson gson=new Gson();
        userSession=new UserSession(Subcategory_details.this);
        userdata=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
        book_now=(Button)findViewById(R.id.book_now);
        subcategoryimg=(ImageView)findViewById(R.id.subcategoryimg);
        sub_cat_name=(TextView)findViewById(R.id.sub_cat_name);
        sub_cat_desc=(TextView)findViewById(R.id.sub_cat_desc);
        duration=(TextView)findViewById(R.id.duration);
        turnaroundtime=(TextView)findViewById(R.id.turnaroundtime);
        pricing=(TextView)findViewById(R.id.pricing);
        discount_price_show=(TextView)findViewById(R.id.discountprice);
        includetxt=(TextView)findViewById(R.id.includedtxt);
        includelayout=(LinearLayout)findViewById(R.id.includedetaillayout);
        Bundle b=getIntent().getExtras();
        sub_cat_detail=b.getString("details");
        cat_id=b.getString("cat_id");
        subCategory=gson.fromJson(sub_cat_detail,Subcategorymodel.SubCategory.class);
        sub_cat_name.setText(subCategory.getSub_name());
        sub_cat_desc.setText(subCategory.getSub_des());
        duration.setText(subCategory.getDuration());
        turnaroundtime.setText(subCategory.getTurnaroundtime());
        pricing.setText(subCategory.getPrice());
        pricing.setPaintFlags(pricing.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        float dis_price=discountfind(Integer.parseInt(subCategory.getPrice()),Integer.parseInt(subCategory.getDiscount_price()));
        discount_price_show.setText(String.valueOf(dis_price));
        Log.d("fffdiscount",subCategory.getDiscount_price());
        Glide.with(getApplicationContext()).load(Constant.IMAGE_URL+subCategory.getSub_img()).into(subcategoryimg);
        includetxt.setText("What`s included in "+subCategory.getSub_name());
        include_list= Arrays.asList(subCategory.getInclude().split(","));


        for (int i=0; i<include_list.size();i++)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView=new TextView(Subcategory_details.this);
            textView.setLayoutParams(layoutParams);
            Typeface typeface= null;
            textView.setTextColor(getResources().getColor(R.color.text_black_color));
            textView.setTextSize(15);
            textView.setText("\u2022 "+include_list.get(i));
            includelayout.addView(textView);

        }
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSession.isLoggedIn()) {
                    Intent location = new Intent(getApplicationContext(), Location_screen.class);
                    location.putExtra("sub_cat_id", subCategory.getSub_id());
                    location.putExtra("sub_cat_name",subCategory.getSub_name());
                    location.putExtra("cat_id", cat_id);
                    location.putExtra("pricing",discount_price);
                    startActivity(location);
                }else{
                    Toast.makeText(getApplicationContext(),"Please login first then book service",Toast.LENGTH_LONG).show();
                    Intent location = new Intent(getApplicationContext(), Number_Verification.class);
                    startActivity(location);
                }
            }
        });
        if (userSession.isLoggedIn()){
            if (Integer.parseInt(userdata.getMembership_status())==1)
            {
                getmembership();
            }else{

            }
        }



    }
    public float discountfind(int price,int discountprice){
        float s=100-discountprice;
        float amount= (s*price)/100;
        return amount;
    }
    public  void getmembership()
    {
        pdialog = new ProgressDialog(Subcategory_details.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Usermembershipmodel> call = Retrofitclient.getInstance().getMyApi().usermembership_list("get_user_membership",userdata.getClient_id());
        call.enqueue(new Callback<Usermembershipmodel>() {
            @Override
            public void onResponse(Call<Usermembershipmodel> call, Response<Usermembershipmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                    Usermembershipmodel membershipmodel = response.body();

                    membershipList = membershipmodel.getData().getUserMembershipList();
                    Usermembershipmodel.UserMembership userMembership=membershipList.get(0);
                    float value=Integer.parseInt(userMembership.getM_discount());
                    float price= Integer.parseInt(subCategory.getPrice());
                    Log.d("ff_price", String.valueOf(price));
                    Log.d("ff_value", String.valueOf(value/100));
                    float discount= (price*(value/100));
                    discount_price=String.valueOf(Integer.parseInt(subCategory.getPrice())-discount);
                    Log.d("ff_dis", String.valueOf(discount));
//                    Log.d("ff",discount_price);
                    pricing.append(" ("+userMembership.getM_discount()+" % discount for member)");
                    Log.d("array", userMembership.getM_discount());



                }

                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Usermembershipmodel> call, Throwable t) {
                pdialog.dismiss();
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }


}