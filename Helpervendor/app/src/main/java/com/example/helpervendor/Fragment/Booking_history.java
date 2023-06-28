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

import com.example.helpervendor.Adapter.Historyadapter;
import com.example.helpervendor.Adapter.Ongoingadapter;
import com.example.helpervendor.Model.Historymodel;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.Model.Vendordetailmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Booking_history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Booking_history extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView history_rec;
    LinearLayout nobooking,noservice;
    ProgressDialog pdialog;
    List<Historymodel.History> historyArrayList=new ArrayList<>();
    UserSession userSession;
    Resultmodel.Data data;
    Button add_service;
    String service_status;

    public Booking_history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Booking_history.
     */
    // TODO: Rename and change types and number of parameters
    public static Booking_history newInstance(String param1, String param2) {
        Booking_history fragment = new Booking_history();
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
        View view= inflater.inflate(R.layout.fragment_booking_history, container, false);
        history_rec=(RecyclerView)view.findViewById(R.id.historyrec);
        nobooking=(LinearLayout)view.findViewById(R.id.nobookinglayout);
        noservice=(LinearLayout)view.findViewById(R.id.noservicelayout);
        add_service=(Button)view.findViewById(R.id.add_service);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        history_rec.setLayoutManager(linearLayoutManager);
        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        Log.d("vendor_id",data.getVendor_id());
        Checkstatus();
//        if (data.getService_status()==null && TextUtils.isEmpty(data.getService_status()))
//        {
//            noservice.setVisibility(View.VISIBLE);
//        }else if (Integer.parseInt(data.getService_status())==1) {
//            getbookinghistory();
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
    public void getbookinghistory()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Historymodel> call = Retrofitclient.getInstance().getMyApi().booking_history("vendor_booking_history",data.getVendor_id());
        call.enqueue(new Callback<Historymodel>() {
            @Override
            public void onResponse(Call<Historymodel> call, Response<Historymodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Historymodel historymodel = response.body();
                    historyArrayList = historymodel.getData().getHistories();
                    Historyadapter historyadapter = new Historyadapter(getActivity(), historyArrayList);
                    history_rec.setAdapter(historyadapter);


                    Log.d("array", String.valueOf(historyArrayList.size()));
                }
                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Historymodel> call, Throwable t) {
                pdialog.dismiss();
                history_rec.setVisibility(View.GONE);
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
                            getbookinghistory();
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
}