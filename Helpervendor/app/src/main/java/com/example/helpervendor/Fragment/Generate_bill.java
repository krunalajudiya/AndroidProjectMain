package com.example.helpervendor.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.helpervendor.Adapter.Categoryadapter;
import com.example.helpervendor.Model.Categorymodel;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.Model.Servicechargemodel;
import com.example.helpervendor.Model.Usermembershipmodel;
import com.example.helpervendor.Other.MySingleton;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Generate_bill#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Generate_bill extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputEditText service_charge, total_part_charge, quantity, yourcharge, discount;
    String service_charge_str, total_part_charge_str, quantity_str, yourcharge_str, client_name, client_id, b_id, total_charge, address, vendor_id, bill_date, discount_str,coupon_str,sub_total_str;
    TextView total_charge_txt;
    Button create_invoice, calculate_discount;
    RelativeLayout main_invoice_layout;
    TextView nameandaddress, invoice_date, service_charge_rate, service_charge_amount, part_quantity, part_charge_rate, part_charge_amount, sub_total, invoice_total, discount_txt,coupon_txt,coupon;
    UserSession userSession;
    Resultmodel.Data data;
    File bill_pdf_file;
    ProgressDialog pdialog;
    List<Servicechargemodel.Servicecharge> servicechargeList;
    Float service_percentage;
    float discount_p = 0;
    boolean value=false;

    List<Usermembershipmodel.UserMembership> membershipList = new ArrayList<>();
    String discount_membership;

    Usermembershipmodel.data membershipdata;

    boolean dis_enter = true;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAABMS_FlA:APA91bG-WRSzwo6soXltBYD8902ttAlAf_NrkhGekidnzqcp5AtKqO3BhOiZzGoArEY4rI2PBAvn5q3O6yH1YOkNBq10HKOgRZOyagokEwh1WFMyjEgr35zP8ZczvHOxQWf5LufpFH-0";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String TOPIC = "/topics/userABC";

    public Generate_bill() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Generate_bill.
     */
    // TODO: Rename and change types and number of parameters
    public static Generate_bill newInstance(String param1, String param2) {
        Generate_bill fragment = new Generate_bill();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generate_bill, container, false);
        userSession = new UserSession(getActivity());
        Gson gson = new Gson();
        data = gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        vendor_id = data.getVendor_id();
        main_invoice_layout = (RelativeLayout) view.findViewById(R.id.main_invoice_layout);
        nameandaddress = (TextView) view.findViewById(R.id.name_and_address);
        invoice_date = (TextView) view.findViewById(R.id.invoice_date);
        service_charge_rate = (TextView) view.findViewById(R.id.service_charge);
        service_charge_amount = (TextView) view.findViewById(R.id.service_charge_amount);
        part_quantity = (TextView) view.findViewById(R.id.partquantity);
        part_charge_rate = (TextView) view.findViewById(R.id.partquantity_charge);
        part_charge_amount = (TextView) view.findViewById(R.id.partquantity_charge_amount);
        sub_total = (TextView) view.findViewById(R.id.sub_total_txt);
        invoice_total = (TextView) view.findViewById(R.id.invoice_total_amount_txt);
        calculate_discount = (Button) view.findViewById(R.id.calculate_discount);
        discount_txt = (TextView) view.findViewById(R.id.discount_txt);
        service_charge = (TextInputEditText) view.findViewById(R.id.sub_cat_charges);
        total_part_charge = (TextInputEditText) view.findViewById(R.id.totalpartcharges);
        quantity = (TextInputEditText) view.findViewById(R.id.partquanity);
        yourcharge = (TextInputEditText) view.findViewById(R.id.yourcharge);
        discount = (TextInputEditText) view.findViewById(R.id.discount);
        total_charge_txt = (TextView) view.findViewById(R.id.total_charge_txt);
        create_invoice = (Button) view.findViewById(R.id.create_invoice);
        coupon_txt=view.findViewById(R.id.coupon_txt);
        coupon=view.findViewById(R.id.coupon);


        Bundle bundle = getArguments();
        b_id = bundle.getString("b_id");
        service_charge_str = bundle.getString("sub_price");
        client_id = bundle.getString("client_id");
        client_name = bundle.getString("client_name");
        address = bundle.getString("address");
        coupon_str=bundle.getString("coupon_amount");
        service_charge.setText(service_charge_str);
        total_charge = service_charge_str;
        total_charge_txt.setText("Total Charge is " + total_charge);
        if (Integer.parseInt(coupon_str)==0){
            coupon.setVisibility(View.GONE);
            coupon_txt.setVisibility(View.GONE);
        }else {
            coupon_txt.setText(coupon_str);
        }
        Log.d("coupon amount",coupon_str);



        getmembership();

        yourcharge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    value=true;
                    Float your_s = TextUtils.isEmpty(yourcharge.getText().toString().trim()) ? 0 : Float.parseFloat(yourcharge.getText().toString().trim());
                    ;
                    Float total_service_charge = (your_s * service_percentage) / 100;
                    //Log.d("total_charge", String.valueOf(your_s + total_service_charge));
                    //Float total_service_charge=your_s;
                    yourcharge.setText(String.valueOf(your_s + total_service_charge));
                    //yourcharge.setText(String.valueOf(total_service_charge));

                    Float firtValue = TextUtils.isEmpty(service_charge.getText().toString().trim()) ? 0 : Float.parseFloat(service_charge.getText().toString().trim());
                    Float secondValue = TextUtils.isEmpty(total_part_charge.getText().toString().trim()) ? 0 : Float.parseFloat(total_part_charge.getText().toString().trim());
                    Float thirdValue = TextUtils.isEmpty(yourcharge.getText().toString().trim()) ? 0 : Float.parseFloat(yourcharge.getText().toString().trim());
                    secondValue = secondValue * (TextUtils.isEmpty(quantity.getText().toString().trim()) ? 1 : Integer.parseInt(quantity.getText().toString().trim()));
                    total_charge = String.valueOf(firtValue + secondValue + thirdValue);
                    total_charge_txt.setText(total_charge);
                    Log.d("total_charge", String.valueOf(total_charge));
                }
            }
        });
        yourcharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                //Float thirdvalue=TextUtils.isEmpty(yourcharge.getText().toString().trim()) ? 0 : Float.parseFloat(yourcharge.getText().toString().trim());

                // Log.d("ssssssss", String.valueOf(thirdvalue));

            }


        });
        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dis_enter == false)
                    calculate_discount.setEnabled(true);
            }
        });
        calculate_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dis_enter = false;
                calculate_discount.setEnabled(false);

                float firtValue = TextUtils.isEmpty(service_charge.getText().toString().trim()) ? 0 : Float.parseFloat(service_charge.getText().toString().trim());
                float secondValue = TextUtils.isEmpty(total_part_charge.getText().toString().trim()) ? 0 : Float.parseFloat(total_part_charge.getText().toString().trim());
                float thirdValue = TextUtils.isEmpty(yourcharge.getText().toString().trim()) ? 0 : Float.parseFloat(yourcharge.getText().toString().trim());
                float discountValue = TextUtils.isEmpty(discount.getText().toString().trim()) ? 0 : Float.parseFloat(discount.getText().toString().trim());
                secondValue = secondValue * (TextUtils.isEmpty(quantity.getText().toString().trim()) ? 1 : Integer.parseInt(quantity.getText().toString().trim()));
                if (!TextUtils.isEmpty(discount.getText().toString().trim())) {
                    float discount_price = (firtValue * (discountValue / 100));
//                    service_charge.setText(String.valueOf(discount_price));
                    discount_p = discount_price;
                }
                firtValue = TextUtils.isEmpty(service_charge.getText().toString().trim()) ? 0 : Float.parseFloat(service_charge.getText().toString().trim());
                total_charge = String.valueOf(firtValue + secondValue + thirdValue);
                Log.d("ff_total", String.valueOf(Float.parseFloat(total_charge) - discount_p));
                Log.d("ff_firstvalue", String.valueOf(firtValue));
                Log.d("ff_discount", String.valueOf(discount_p));
                discount_str=String.valueOf(discount_p);
                total_charge=String.valueOf(Float.parseFloat(total_charge) - discount_p);
                //total_charge=String.valueOf(Float.parseFloat(total_charge)-Float.parseFloat(coupon_str));
                total_charge_txt.setText(total_charge);
                Log.d("total_charge", String.valueOf(total_charge));
            }
        });

        if (isStoragePermissionGranted()) {

        }
        getServicecharge();
        create_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (value==false){

                    Float your_s = TextUtils.isEmpty(yourcharge.getText().toString().trim()) ? 0 : Float.parseFloat(yourcharge.getText().toString().trim());

                    Float total_service_charge = (your_s * service_percentage) / 100;
                    //Log.d("total_charge", String.valueOf(your_s + total_service_charge));
                    //Float total_service_charge=your_s;
                    yourcharge.setText(String.valueOf(your_s + total_service_charge));

                    Float firtValue = TextUtils.isEmpty(service_charge.getText().toString().trim()) ? 0 : Float.parseFloat(service_charge.getText().toString().trim());
                    Float secondValue = TextUtils.isEmpty(total_part_charge.getText().toString().trim()) ? 0 : Float.parseFloat(total_part_charge.getText().toString().trim());
                    Float thirdValue = TextUtils.isEmpty(yourcharge.getText().toString().trim()) ? 0 : Float.parseFloat(yourcharge.getText().toString().trim());
                    secondValue = secondValue * (TextUtils.isEmpty(quantity.getText().toString().trim()) ? 1 : Integer.parseInt(quantity.getText().toString().trim()));
                    total_charge = String.valueOf(firtValue + secondValue + thirdValue);
                    //total_charge=String.valueOf(Float.parseFloat(total_charge)-Float.parseFloat(coupon_str));
                    total_charge_txt.setText(total_charge);
                    Log.d("total_charge", String.valueOf(total_charge));



                }
                total_charge=String.valueOf(Float.parseFloat(total_charge)-Float.parseFloat(coupon_str));
                total_charge_txt.setText(total_charge);
                getvalue();
                if (total_part_charge.getText().toString().trim().isEmpty()){
                    sub_total_str=String.valueOf(Float.parseFloat(service_charge_str)+Float.parseFloat(yourcharge.getText().toString()));
                }else {
                    sub_total_str=String.valueOf(Float.parseFloat(service_charge_str)+Float.parseFloat(total_part_charge.getText().toString())+Float.parseFloat(yourcharge.getText().toString()));
                }

                Log.d("ffsubtotoal",sub_total_str);
                nameandaddress.setText(client_name + "\n" + address);
                service_charge_rate.setText(service_charge_str);
                service_charge_amount.setText(service_charge_str);
                part_quantity.setText(quantity_str);
                part_charge_rate.setText(String.valueOf(Float.parseFloat(total_part_charge_str) + Float.parseFloat(yourcharge_str)));
                part_charge_amount.setText(String.valueOf(Float.parseFloat(total_part_charge_str) + Float.parseFloat(yourcharge_str)));
                sub_total.setText(sub_total_str);
                invoice_total.setText(total_charge_txt.getText().toString());
                discount_txt.setText(String.valueOf(discount_p));
                invoice_date.setText(bill_date);
                Log.d("address", nameandaddress.getText().toString());
                Random rand = new Random();
                int n = rand.nextInt(1000);
                PdfGenerator.getBuilder().setContext(getActivity()).fromViewSource().fromView(main_invoice_layout).setPageSize(PdfGenerator.PageSize.A4).setFileName(n + "helperfactory").setFolderName("helperfactory").openPDFafterGeneration(false).build(new PdfGeneratorListener() {
                    @Override
                    public void onStartPDFGeneration() {

                    }

                    @Override
                    public void onFinishPDFGeneration() {

                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        Log.d("sss", response.getPath());

                        bill_pdf_file = response.getFile();
                        Generate_invoice();

                    }

                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }
                });
            }
        });


        return view;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public void getServicecharge() {
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Servicechargemodel> call = Retrofitclient.getInstance().getMyApi().Service_charge("service_charge",data.getCity());
        call.enqueue(new Callback<Servicechargemodel>() {
            @Override
            public void onResponse(Call<Servicechargemodel> call, Response<Servicechargemodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Servicechargemodel servicechargemodel = response.body();
                    Log.d("fffff111", servicechargemodel.getError());
                    if (Integer.parseInt(servicechargemodel.getError()) == 200) {
                        servicechargeList = new ArrayList<>();
                        servicechargeList = servicechargemodel.getData().getServicecharges();
                        service_percentage = Float.parseFloat(servicechargeList.get(0).getS_charge());
                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Servicechargemodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff", t.getMessage());
                pdialog.dismiss();
            }
        });

    }

    public void Generate_invoice() {
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();


        RequestBody requesttag = RequestBody.create(MediaType.parse("text/plain"), "invoice");
        RequestBody request_b_id = RequestBody.create(MediaType.parse("text/plain"), b_id);
        RequestBody requestservicecharge = RequestBody.create(MediaType.parse("text/plain"), yourcharge_str);
        RequestBody requestpart_charge = RequestBody.create(MediaType.parse("text/plain"), total_part_charge_str);
        RequestBody requestsubcatcharge = RequestBody.create(MediaType.parse("text/plain"), service_charge_str);
        RequestBody requestvendor_id = RequestBody.create(MediaType.parse("text/plain"), vendor_id);
        RequestBody requestcustomer_id = RequestBody.create(MediaType.parse("text/plain"), client_id);
        RequestBody requesttotal_charges = RequestBody.create(MediaType.parse("text/plain"), total_charge);
        RequestBody requestbill_date = RequestBody.create(MediaType.parse("text/plain"), bill_date);

        MultipartBody.Part body;
        if (bill_pdf_file.exists()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), bill_pdf_file);
            body = MultipartBody.Part.createFormData("bill_pdf", bill_pdf_file.getName(), requestBody);
        } else {
            body = MultipartBody.Part.createFormData("bill_pdf", "");
        }

        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Generate_invoice(requesttag, request_b_id, requestservicecharge, requestpart_charge, requestsubcatcharge, requestvendor_id, requestcustomer_id, requesttotal_charges, body, requestbill_date,discount_str);
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
                        Toast.makeText(getActivity(), "Bill Generate Successfully", Toast.LENGTH_LONG).show();
                        JSONObject notification=new JSONObject();
                        JSONObject notificationbody=new JSONObject();

                        String msg="Bill generated";
                        try {
                            notificationbody.put("message",msg);
                            notificationbody.put("factory","true");
                            notificationbody.put("user_id",client_id);
                            notification.put("to",TOPIC);
                            notification.put("data",notificationbody);
                            Log.d("fff", String.valueOf(notification));
                            sendNotification(notification);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadFragment(new New_order());

                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff", t.getMessage());
                pdialog.dismiss();
            }
        });

    }

    public void getvalue() {
        if (total_part_charge.getText().toString().trim().length()>0){

            total_part_charge_str = total_part_charge.getText().toString().trim();
        }
        else {
            total_part_charge_str=String.valueOf(0);
        }
        if (yourcharge.getText().toString().trim().length()>0){

            yourcharge_str = yourcharge.getText().toString().trim();
        }else {
            yourcharge_str=String.valueOf(0);
        }
        service_charge_str = service_charge.getText().toString().trim();
        quantity_str = quantity.getText().toString().trim();
        //discount_str = discount.getText().toString().trim();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        bill_date = df.format(c);

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getmembership() {
//        pdialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
//        pdialog.setMessage("Please wait...");
//        pdialog.setIndeterminate(false);
//        pdialog.setCancelable(false);
//        pdialog.show();
        Call<Usermembershipmodel> call = Retrofitclient.getInstance().getMyApi().usermembership_list("get_user_membership", client_id);
        call.enqueue(new Callback<Usermembershipmodel>() {
            @Override
            public void onResponse(Call<Usermembershipmodel> call, Response<Usermembershipmodel> response) {
//                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Usermembershipmodel membershipmodel = response.body();

                    membershipList = membershipmodel.getData().getUserMembershipList();
                    Usermembershipmodel.UserMembership userMembership = membershipList.get(0);
                    float value = Integer.parseInt(userMembership.getM_discount());
                    float price = Integer.parseInt(service_charge_str);
                    Log.d("ff_price", String.valueOf(price));
                    Log.d("ff_value", String.valueOf(value / 100));
                    float discount = (price * (value / 100));
                    discount_membership = String.valueOf(price - discount);
                    Log.d("ff_dis", String.valueOf(discount));
                    service_charge.setText(discount_membership);

                }

                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Usermembershipmodel> call, Throwable t) {
//                pdialog.dismiss();
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff", t.getMessage());
            }
        });
    }
    public void sendNotification(JSONObject notification){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new com.android.volley.Response.Listener<JSONObject>()   {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
}