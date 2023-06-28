package com.example.helpervendor.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.helpervendor.Adapter.Ongoingadapter;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.Model.Servicemodel;
import com.example.helpervendor.Model.Vendordetailmodel;
import com.example.helpervendor.Other.MySingleton;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.google.gson.Gson;

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
    LinearLayout nobooking,noservice;
    ProgressDialog pdialog;
    List<Ongoingmodel.Ongoing> ongoing_dataArrayList=new ArrayList<>();
    UserSession userSession;
    Resultmodel.Data data;
    String appointment_id;
    Button add_service;
    String service_status;

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
        View view =inflater.inflate(R.layout.fragment_ongoing, container, false);
        ongoing_rec=(RecyclerView)view.findViewById(R.id.ongoingrec);
        nobooking=(LinearLayout)view.findViewById(R.id.nobookinglayout);
        noservice=(LinearLayout)view.findViewById(R.id.noservicelayout);
        add_service=(Button)view.findViewById(R.id.add_service);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        ongoing_rec.setLayoutManager(linearLayoutManager);
        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        Log.d("vendor_id",data.getVendor_id());
        Checkstatus();
//        if (data.getService_status()==null && TextUtils.isEmpty(data.getService_status()))
//        {
//            noservice.setVisibility(View.VISIBLE);
//        }else if (Integer.parseInt(data.getService_status())==1) {
//            getbooking();
//        }
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
    public void getbooking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Ongoingmodel> call = Retrofitclient.getInstance().getMyApi().Ongoing_booking("Fetch_confirm_appointment",data.getVendor_id());
        call.enqueue(new Callback<Ongoingmodel>() {
            @Override
            public void onResponse(Call<Ongoingmodel> call, Response<Ongoingmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Ongoingmodel ongoingmodel = response.body();
                    ongoing_dataArrayList = ongoingmodel.getData().getOngoings();
                    Ongoingadapter ongoingadapter = new Ongoingadapter(getActivity(), ongoing_dataArrayList);
                    ongoing_rec.setAdapter(ongoingadapter);
                    ongoingadapter.setOnItemClickListener(new Ongoingadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }
                    });
                    ongoingadapter.setOnBillClickListener(new Ongoingadapter.OnBillClickListener() {
                        @Override
                        public void onBillClick(int pos) {
                            Generate_bill generate_bill=new Generate_bill();
                            Bundle b=new Bundle();
                            Log.d("price",ongoing_dataArrayList.get(pos).getPrice());
                            b.putString("b_id",ongoing_dataArrayList.get(pos).getId());
                            b.putString("sub_price",ongoing_dataArrayList.get(pos).getPrice());
                            b.putString("client_id",ongoing_dataArrayList.get(pos).getClient_id());
                            b.putString("client_name",ongoing_dataArrayList.get(pos).getClient_name());
                            b.putString("address",ongoing_dataArrayList.get(pos).getAddress());
                            b.putString("coupon_amount",ongoing_dataArrayList.get(pos).getCoupon_amount());
                            generate_bill.setArguments(b);
                            loadFragment(generate_bill);

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
                        Log.d("ff", String.valueOf(vendor.get(0).getService_status()));
                        Log.d("service_status",service_status);

                            if (service_status==null)
                            {
                                noservice.setVisibility(View.VISIBLE);
                            }else if (Integer.parseInt(service_status)==1) {
                                getbooking();
                                getservice();

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