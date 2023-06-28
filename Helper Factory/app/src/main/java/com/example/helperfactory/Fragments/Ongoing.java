package com.example.helperfactory.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.helperfactory.Activity.Membership_plan;
import com.example.helperfactory.Activity.Subcategory;
import com.example.helperfactory.Activity.Subcategory_details;
import com.example.helperfactory.Adapter.Ongoingadapter;
import com.example.helperfactory.Adapter.SubcategoryAdapter;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Ongoingmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.Model.Subcategorymodel;
import com.example.helperfactory.Other.MySingleton;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ongoing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ongoing extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView ongoing_rec;
    LinearLayout nobooking;
    ProgressDialog pdialog;
    List<Ongoingmodel.Ongoing_data> ongoing_dataArrayList=new ArrayList<>();
    UserSession userSession;
    Resultmodel.Data data;
    String appointment_id,amount_str,b_id,u_id,payment_type,payment_id;
    ImageView close;
    RadioButton online,cash;
    Button pay;
    String client_id;
    String vendor_id;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAABMS_FlA:APA91bG-WRSzwo6soXltBYD8902ttAlAf_NrkhGekidnzqcp5AtKqO3BhOiZzGoArEY4rI2PBAvn5q3O6yH1YOkNBq10HKOgRZOyagokEwh1WFMyjEgr35zP8ZczvHOxQWf5LufpFH-0";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String TOPIC = "/topics/userABC";



    public Ongoing() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ongoing.
     */
    // TODO: Rename and change types and number of parameters
    public static Ongoing newInstance(String param1, String param2) {
        Ongoing fragment = new Ongoing();
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
        View view= inflater.inflate(R.layout.fragment_ongoing, container, false);
        ongoing_rec=(RecyclerView)view.findViewById(R.id.ongoingrec);
        nobooking=(LinearLayout)view.findViewById(R.id.nobookinglayout);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        ongoing_rec.setLayoutManager(linearLayoutManager);
        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        client_id=data.getClient_id();
        Log.d("client_id",data.getClient_id());
        getbooking();
        ((MainActivity) getActivity()).passVal(new MainActivity.OnHeadlineSelectedListener() {
            @Override
            public void onArticleSelected(String id) {
                payment_id=id;
                pay_booking();


            }
        });
        return view;
    }
    public void getbooking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Ongoingmodel> call = Retrofitclient.getInstance().getMyApi().Ongoing_detail("Fetch_book",data.getClient_id());
        call.enqueue(new Callback<Ongoingmodel>() {
            @Override
            public void onResponse(Call<Ongoingmodel> call, Response<Ongoingmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Ongoingmodel ongoingmodel = response.body();
                    ongoing_dataArrayList = ongoingmodel.getData1().getOngoing_data();
                    Ongoingadapter ongoingadapter = new Ongoingadapter(getActivity(), ongoing_dataArrayList);
                    ongoing_rec.setAdapter(ongoingadapter);
                    ongoingadapter.setOnCancelClickListener(new Ongoingadapter.OnCancelClickListener() {
                        @Override
                        public void onCancelClick(int pos) {
                            appointment_id=ongoing_dataArrayList.get(pos).getId();
                            Cancel_booking();
                        }
                    });
                    ongoingadapter.setOnItemClickListener(new Ongoingadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.payment_dialog, viewGroup, false);
                    close=dialogView.findViewById(R.id.close);
                    pay=dialogView.findViewById(R.id.pay);
                    online=dialogView.findViewById(R.id.online);
                    cash=dialogView.findViewById(R.id.cash);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ongoingadapter.setOnPaymentClickListener(new Ongoingadapter.OnPaymentClickListener() {
                        @Override
                        public void onPaymentClick(int pos) {
                            amount_str=String.valueOf(ongoing_dataArrayList.get(pos).getBill_pdf().get(0).getTotal_charges());
                            b_id=ongoing_dataArrayList.get(pos).getId();
                            vendor_id=ongoing_dataArrayList.get(pos).getVendor_detail().get(0).getVendor_id();
//                            Log.d("1_id",);
                            u_id=data.getClient_id();
                            Log.d("2_id",u_id);
                            alertDialog.show();

                        }
                    });
                    pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (online.isChecked())
                            {
                                payment_type=online.getText().toString();
                                startPayment();
                                alertDialog.dismiss();
                            }else if (cash.isChecked()) {
                                payment_id="";
                                payment_type=cash.getText().toString();
                                pay_booking();
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(getActivity(),"Please Choose Any one method",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    Log.d("array", String.valueOf(ongoing_dataArrayList.size()));
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Ongoingmodel> call, Throwable t) {
                pdialog.dismiss();
                ongoing_rec.setVisibility(View.GONE);
                nobooking.setVisibility(View.VISIBLE);
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

            co.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void Cancel_booking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().CancelBooking("delete_booking",appointment_id);
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(),"Cancel Booking SuccessFully",Toast.LENGTH_LONG).show();
                    Intent home = new Intent(getActivity(), MainActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    home.putExtra("success",1);
                    startActivity(home);
                    getActivity().finish();
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
    public void pay_booking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Pay_Booking("add_booking_payment",u_id,b_id,payment_type,payment_id);
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(),"Pay  SuccessFully",Toast.LENGTH_LONG).show();
                    Intent home = new Intent(getActivity(), MainActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    home.putExtra("success",1);
                    JSONObject notification=new JSONObject();
                    JSONObject notificationbody=new JSONObject();
                    String msg="Payment Succefull";
                    try {
                        notificationbody.put("message",msg);
                        notificationbody.put("vendor","true");
                        notificationbody.put("factory","true");
                        notificationbody.put("user_id",data.getClient_id());
                        notificationbody.put("client_id",vendor_id);
                        notificationbody.put("sub_cat_id",null);
                        notification.put("to",TOPIC);
                        notification.put("data",notificationbody);
                        Log.d("fff", String.valueOf(notification));
                        sendNotification(notification);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(home);
                    getActivity().finish();
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
    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }


}