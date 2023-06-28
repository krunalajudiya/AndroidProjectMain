package com.example.helperfactory.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperfactory.Adapter.CategoryAdapter;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Generatetokenmodel;
import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Membershipmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
/*import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;*/

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Membership_plan extends AppCompatActivity implements PaymentResultListener {
    private String  TAG ="Membership_plan";
    Toolbar toolbar;
    TextView silver_title,silver_price,silver_discount,silver_list,gold_title,gold_price,gold_discount,gold_list,premium_title,premium_price,premium_discount,premium_list;
    List<Membershipmodel.Membership> membershipList=new ArrayList<>();
    ProgressDialog pdialog;
    String sub_cat_id,cat_id,sub_cat_name;
    TextView skip;
    Button silver_btn,gold_btn,premium_btn;
    UserSession userSession;
    Resultmodel.Data userdata;
    String m_id="QraXpZ48570169459881",o_id="",amount="50",txnTokenString="";
    String amount_str,u_id,me_id,from_date,to_date,txnid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_plan);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            skip=(TextView)toolbar.findViewById(R.id.skip);
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
        Long tsLong = System.currentTimeMillis()/1000;
        o_id = tsLong.toString();
        userSession=new UserSession(Membership_plan.this);
        Gson gson=new Gson();
        userdata=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
        u_id=userdata.getClient_id();
        Bundle b=getIntent().getExtras();
        sub_cat_id=b.getString("sub_cat_id");
        cat_id=b.getString("cat_id");
        sub_cat_name=b.getString("sub_cat_name");
        Log.d("sub_cat_name",sub_cat_name);
        silver_title=(TextView)findViewById(R.id.silver_tile);
        silver_price=(TextView)findViewById(R.id.silver_price);
        silver_list=(TextView)findViewById(R.id.silver_list);
        silver_discount=(TextView)findViewById(R.id.silver_discount);
        gold_title=(TextView)findViewById(R.id.gold_title);
        gold_price=(TextView)findViewById(R.id.gold_price);
        gold_discount=(TextView)findViewById(R.id.gold_discount);
        gold_list=(TextView)findViewById(R.id.gold_list);
        premium_title=(TextView)findViewById(R.id.premium_title);
        premium_price=(TextView)findViewById(R.id.premium_price);
        premium_discount=(TextView)findViewById(R.id.premium_discount);
        premium_list=(TextView)findViewById(R.id.premium_list);
        silver_btn=(Button)findViewById(R.id.sliver_btn);
        gold_btn=(Button)findViewById(R.id.gold_btn);
        premium_btn=(Button)findViewById(R.id.premium_btn);
        getmembership();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schedule=new Intent(getApplicationContext(),Schedule_booking.class);
                schedule.putExtra("sub_cat_id",sub_cat_id);
                schedule.putExtra("sub_cat_name",sub_cat_name);
                schedule.putExtra("cat_id",cat_id);
                startActivity(schedule);
            }
        });
        silver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_str=membershipList.get(0).getM_price();
                me_id=membershipList.get(0).getM_id();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                from_date = df.format(c);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, +Integer.parseInt(membershipList.get(0).getM_valid_upto()));
                Date date = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
                to_date = format.format(date);
                Log.d("date",from_date+"\n"+to_date);
                startPayment();

            }
        });
        gold_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_str=membershipList.get(1).getM_price();
                me_id=membershipList.get(0).getM_id();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                from_date = df.format(c);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, +Integer.parseInt(membershipList.get(0).getM_valid_upto()));
                Date date = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
                to_date = format.format(date);
                startPayment();
            }
        });
        premium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_str=membershipList.get(2).getM_price();
                me_id=membershipList.get(0).getM_id();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                from_date = df.format(c);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, +Integer.parseInt(membershipList.get(0).getM_valid_upto()));
                Date date = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
                to_date = format.format(date);
                startPayment();
            }
        });



    }
    public  void getmembership()
    {
        pdialog = new ProgressDialog(Membership_plan.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Membershipmodel> call = Retrofitclient.getInstance().getMyApi().Membership_list("membership_list");
        call.enqueue(new Callback<Membershipmodel>() {
            @Override
            public void onResponse(Call<Membershipmodel> call, Response<Membershipmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Membershipmodel membershipmodel = response.body();

                    membershipList = membershipmodel.getData().getMemberships();
                    Log.d("array", String.valueOf(membershipList.size()));
                    silver_title.setText(membershipList.get(0).getM_name());
                    gold_title.setText(membershipList.get(1).getM_name());
                    premium_title.setText(membershipList.get(2).getM_name());
                    silver_price.setText("\u20B9 "+membershipList.get(0).getM_price());
                    gold_price.setText("\u20B9 "+membershipList.get(1).getM_price());
                    premium_price.setText("\u20B9 "+membershipList.get(2).getM_price());
                    silver_discount.setText(membershipList.get(0).getM_discount()+" % Discount");
                    gold_discount.setText(membershipList.get(1).getM_discount()+" % Discount");
                    premium_discount.setText(membershipList.get(2).getM_discount()+" % Discount");
                    List<String> include_list=Arrays.asList(membershipList.get(0).getInclude_list().split(","));
                    for (int i=0;i< include_list.size();i++)
                    {
                        if (i==0) {
                            silver_list.setText("\u2022"+include_list.get(i));
                        }else{
                            silver_list.append("\n\u2022"+include_list.get(i));
                        }
                    }
                    List<String> include_list1=Arrays.asList(membershipList.get(1).getInclude_list().split(","));
                    for (int i=0;i< include_list1.size();i++)
                    {
                        if (i==0) {
                            gold_list.setText("\u2022"+include_list1.get(i));
                        }else{
                            gold_list.append("\n\u2022"+include_list1.get(i));
                        }
                    }
                    List<String> include_list2=Arrays.asList(membershipList.get(2).getInclude_list().split(","));
                    for (int i=0;i< include_list2.size();i++)
                    {
                        if (i==0) {
                            premium_list.setText("\u2022"+include_list2.get(i));
                        }else{
                            premium_list.append("\n\u2022"+include_list2.get(i));
                        }
                    }


                }

                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Membershipmodel> call, Throwable t) {
                pdialog.dismiss();
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */


        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Helper Factory");
            options.put("description", "DVC");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount",amount_str+"00");

            co.open(Membership_plan.this, options);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        txnid=s;
        Log.d("razorid",s);
        Add_Payment();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
    public  void Add_Payment()
    {
        pdialog = new ProgressDialog(Membership_plan.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();

        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Add_membership("Add_payment",u_id,me_id,from_date,to_date,txnid,amount_str);
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
                        Toast.makeText(getApplicationContext(),"Your are Now our Member",Toast.LENGTH_LONG).show();
                        userdata.setMembership_status("1");
                        String user_detail;
                        Gson gson=new Gson();
                        user_detail=gson.toJson(userdata);
                        userSession.createLoginSession(user_detail);

                        Intent schedule=new Intent(getApplicationContext(),Schedule_booking.class);
                        schedule.putExtra("sub_cat_id",sub_cat_id);
                        schedule.putExtra("sub_cat_name",sub_cat_name);
                        schedule.putExtra("cat_id",cat_id);
                        startActivity(schedule);
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

    /*public  void Gettoken()
    {
        pdialog = new ProgressDialog(Membership_plan.this,R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();

        Call<Generatetokenmodel> call = Retrofitclient.getInstance().getMyApi().generateTokenCall(Constant.M_ID,o_id,amount,userdata.getClient_id());
        call.enqueue(new Callback<Generatetokenmodel>() {
            @Override
            public void onResponse(Call<Generatetokenmodel> call, Response<Generatetokenmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Generatetokenmodel generatetokenmodel=response.body();
                    Log.d("signature", response.body().getHead().getSignature());
                    startpaytmpayment(generatetokenmodel.getHead().getSignature());
//                    Log.d("txntoken", response.body().getBody().getTxnToken());
                    //startPaytmPayment(generatetokenmodel.getBody().getTxnToken());

                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Generatetokenmodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
                pdialog.dismiss();
            }
        });
    }*/


    /*public void startPaytmPayment (String token){

        txnTokenString = token;
        // for test mode use it
        // String host = "https://securegw-stage.paytm.in/";
        // for production mode use it
        String host = "https://securegw-stage.paytm.in/";
        String orderDetails = "MID: " + m_id + ", OrderId: " + o_id + ", TxnToken: " + txnTokenString
                + ", Amount: " + amount;
        //Log.e(TAG, "order details "+ orderDetails);
        String callBackUrl = host + "theia/paytmCallback?ORDER_ID="+o_id;
        Log.e(TAG, " callback URL "+callBackUrl);
        PaytmOrder paytmOrder = new PaytmOrder(o_id, Constant.M_ID, txnTokenString, amount, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){
            @Override
            public void onTransactionResponse(Bundle bundle) {
                Log.e(TAG, "Response (onTransactionResponse) : "+bundle.toString());
            }
            @Override
            public void networkNotAvailable() {
                Log.e(TAG, "network not available ");
            }
            @Override
            public void onErrorProceed(String s) {
                Log.e(TAG, " onErrorProcess "+s.toString());
            }
            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e(TAG, "Clientauth "+s);
            }
            @Override
            public void someUIErrorOccurred(String s) {
                Log.e(TAG, " UI error "+s);
            }
            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e(TAG, " error loading web "+s+"--"+s1);
            }
            @Override
            public void onBackPressedCancelTransaction() {
                Log.e(TAG, "backPress ");
            }
            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e(TAG, " transaction cancel "+s);
            }
        });
        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, ActivityRequestCode);
    }*/
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG ," result code "+resultCode);
        // -1 means successful  // 0 means failed
        // one error is - nativeSdkForMerchantMessage : networkError
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e(TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
            Log.e(TAG, " data "+  data.getStringExtra("nativeSdkForMerchantMessage"));
            Log.e(TAG, " data response - "+data.getStringExtra("response"));
*//*
 data response - {"BANKNAME":"WALLET","BANKTXNID":"1394221115",
 "CHECKSUMHASH":"7jRCFIk6eRmrep+IhnmQrlrL43KSCSXrmM+VHP5pH0ekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
 "CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAcP3138556","ORDERID":"100620202152",
 "PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
 "TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"2020061011121280011018328631290118"}
  *//*
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage")
                    + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }else{
            Log.e(TAG, " payment failed");
        }
    }*/


}
