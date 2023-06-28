package com.example.helpervendor.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.helpervendor.Activity.Home;
import com.example.helpervendor.Adapter.Neworderadapter;
import com.example.helpervendor.Adapter.Ongoingadapter;
import com.example.helpervendor.Constant.Constant;
import com.example.helpervendor.MainActivity;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.Model.Newordermodel;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.Model.Servicemodel;
import com.example.helpervendor.Model.Vendordetailmodel;
import com.example.helpervendor.Other.MySingleton;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link New_order#newInstance} factory method to
 * create an instance of this fragment.
 */
public class New_order extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView neworder_rec;
    LinearLayout nobooking,noservice;
    ProgressDialog pdialog;
    List<Newordermodel.Neworder> neworderArrayList=new ArrayList<>();
    UserSession userSession;
    Resultmodel.Data data;
    String booking_id;
    Button add_service;
    ImageView issue_photo;
    LinearLayout not_available_txt;
    String user_id;
    String service_status;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAABMS_FlA:APA91bG-WRSzwo6soXltBYD8902ttAlAf_NrkhGekidnzqcp5AtKqO3BhOiZzGoArEY4rI2PBAvn5q3O6yH1YOkNBq10HKOgRZOyagokEwh1WFMyjEgr35zP8ZczvHOxQWf5LufpFH-0";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String TOPIC = "/topics/userABC";

    SharedPreferences available_status;
    public New_order() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment New_order.
     */
    // TODO: Rename and change types and number of parameters
    public static New_order newInstance(String param1, String param2) {
        New_order fragment = new New_order();
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
        View view= inflater.inflate(R.layout.fragment_new_order, container, false);
        neworder_rec=(RecyclerView)view.findViewById(R.id.neworderrec);
        nobooking=(LinearLayout)view.findViewById(R.id.nobookinglayout);
        noservice=(LinearLayout)view.findViewById(R.id.noservicelayout);
        add_service=(Button)view.findViewById(R.id.add_service);
        not_available_txt=view.findViewById(R.id.notavailbletxt);

        available_status= PreferenceManager.getDefaultSharedPreferences(getActivity());


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        neworder_rec.setLayoutManager(linearLayoutManager);
        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        Log.d("vendor_id",data.getVendor_id());
        Checkstatus();


        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new All_service());
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void getnewbooking()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.issue_photo_dialog, viewGroup, false);
        issue_photo=dialogView.findViewById(R.id.issue_photo);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Newordermodel> call = Retrofitclient.getInstance().getMyApi().New_Booking("Fetch_new_appointment",data.getVendor_id(),data.getCity());
        call.enqueue(new Callback<Newordermodel>() {
            @Override
            public void onResponse(Call<Newordermodel> call, Response<Newordermodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Newordermodel newordermodel = response.body();
                    neworderArrayList = newordermodel.getData().getNeworderList();
                    Neworderadapter neworderadapter = new Neworderadapter(getActivity(),neworderArrayList);
                    neworder_rec.setAdapter(neworderadapter);
                    neworderadapter.setOnItemClickListener(new Neworderadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {
                            if (neworderArrayList.get(pos).getIssue_photo()!=null)
                            {
                                Picasso.get().load(Constant.IMAGE_URL+neworderArrayList.get(pos).getIssue_photo()).into(issue_photo);
                                alertDialog.show();
                            }

                        }
                    });
                    neworderadapter.setOnConfirmationClickListener(new Neworderadapter.OnConfirmationClickListener() {
                        @Override
                        public void onConfirmationClick(int pos) {
                            booking_id=neworderArrayList.get(pos).getId();
                            user_id=neworderArrayList.get(pos).getClient_id();
                            Log.d("ffid",user_id);
                            Confirm_booking();
                        }

                    });
                    neworderadapter.setOnRejectClickListener(new Neworderadapter.OnRejectClickListener() {
                        @Override
                        public void onRejectClick(int pos) {
                            booking_id=neworderArrayList.get(pos).getId();
                            Reject_booking();
                        }
                    });


                    Log.d("array", String.valueOf(neworderArrayList.size()));
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Newordermodel> call, Throwable t) {
                pdialog.dismiss();
                neworder_rec.setVisibility(View.GONE);
                nobooking.setVisibility(View.VISIBLE);
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });
    }
    public void Confirm_booking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Confirm_Booking("confirm_service_details",booking_id,data.getVendor_id());
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    JSONObject notification=new JSONObject();
                    JSONObject notificationbody=new JSONObject();

                    String msg="Order is Accepted by :"+data.getVendor_name();
                    try {
                        notificationbody.put("title","Accepted");
                        notificationbody.put("message",msg);
                        notificationbody.put("factory","true");
                        notificationbody.put("user_id",user_id);
                        notification.put("to",TOPIC);
                        notification.put("data",notificationbody);
                        Log.d("fff", String.valueOf(notification));
                        sendNotification(notification);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(),"Confirm Booking",Toast.LENGTH_LONG).show();
                    getActivity().recreate();
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                pdialog.dismiss();

                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });

    }

    public void Reject_booking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Reject_service("Reject_booking",booking_id,data.getVendor_id());
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {

                    Toast.makeText(getActivity(),"Reject Booking",Toast.LENGTH_LONG).show();
                    getActivity().recreate();

                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                pdialog.dismiss();

                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
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

    public void Checkstatus()
    {
        Call<Vendordetailmodel> call = Retrofitclient.getInstance().getMyApi().Chaeckstatus("vendor_detail",data.getVendor_id());
        call.enqueue(new Callback<Vendordetailmodel>() {
            @Override
            public void onResponse(Call<Vendordetailmodel> call, Response<Vendordetailmodel> response) {
//                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Vendordetailmodel vendordetailmodel = response.body();

                    if (Integer.parseInt(vendordetailmodel.getError()) == 200) {
                        List<Vendordetailmodel.Data> vendor=vendordetailmodel.getData();
                        service_status=vendor.get(0).getService_status();
                     //Log.d("ff", String.valueOf(vendor.get(0).getService_status()));
                        //Log.d("service_status",service_status);
                        if (available_status.getBoolean("availableStatus",true)){
                            neworder_rec.setVisibility(View.VISIBLE);
                            if (service_status==null)
                            {
                                noservice.setVisibility(View.VISIBLE);
                            }else if (Integer.parseInt(service_status)==1) {
                                getnewbooking();
                                getservice();
                            }

                        }else {
                            neworder_rec.setVisibility(View.GONE);
                            not_available_txt.setVisibility(View.VISIBLE);
                        }

                    }

                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Vendordetailmodel> call, Throwable t) {
//                pdialog.dismiss();
                //handle error or failure cases here
                Log.d("fffff", t.getMessage());
            }
        });
    }
    public void getservice()
    {
        Call<Servicemodel> call = Retrofitclient.getInstance().getMyApi().all_service("vendor_service",data.getVendor_id());
        call.enqueue(new Callback<Servicemodel>() {
            @Override
            public void onResponse(Call<Servicemodel> call, Response<Servicemodel> response) {
//                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                Log.d("response", String.valueOf(response.message()));
                if (response.isSuccessful()) {
                    Servicemodel servicemodel = response.body();

                    Log.d("error", String.valueOf(servicemodel.getError()));
                    if (servicemodel.getError()==200){
                        ArrayList<Servicemodel.Data> servicemodelData=servicemodel.getData();
                        Log.d("datasize", String.valueOf(servicemodelData.size()));
                        if(servicemodelData.get(0).getCategory()!=null){
                            String service=servicemodelData.get(0).getCategory();
                            Log.d("service", service);
                            data.setSub_cat(service);
                            String detail;
                            Gson gson=new Gson();
                            detail=gson.toJson(data);
                            userSession.createLoginSession(detail);
                        }
                        else {
                            Log.d("service", "no");
                        }

                    }

                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Servicemodel> call, Throwable t) {
//                pdialog.dismiss();
                //handle error or failure cases here
                Log.d("fffff", t.getMessage());
            }
        });
    }
}