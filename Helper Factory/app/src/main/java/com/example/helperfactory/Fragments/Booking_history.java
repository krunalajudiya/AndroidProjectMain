package com.example.helperfactory.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.helperfactory.Adapter.Historyadapter;
import com.example.helperfactory.Adapter.Ongoingadapter;
import com.example.helperfactory.Model.Historymodel;
import com.example.helperfactory.Model.Ongoingmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
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
    LinearLayout nobooking;
    ProgressDialog pdialog;
    List<Historymodel.History_data> history_dataArrayList=new ArrayList<>();
    UserSession userSession;
    Resultmodel.Data data;
    String appointment_id;

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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        history_rec.setLayoutManager(linearLayoutManager);
        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        Log.d("client_id",data.getClient_id());
        getbooking();
        return view;
    }
    public void getbooking()
    {
        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Historymodel> call = Retrofitclient.getInstance().getMyApi().Booking_history("Fetch_client_bookhistory",data.getClient_id());
        call.enqueue(new Callback<Historymodel>() {
            @Override
            public void onResponse(Call<Historymodel> call, Response<Historymodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Historymodel historymodel = response.body();
                    history_dataArrayList = historymodel.getData1().getHistory_data();
                    Historyadapter historyadapter = new Historyadapter(getActivity(), history_dataArrayList);
                    history_rec.setAdapter(historyadapter);
                    historyadapter.setOnItemClickListener(new Historyadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }

                        @Override
                        public void ReviewClick(int pos) {
                            String s_id=history_dataArrayList.get(pos).getSub_cat_id();
                            loadFragment(new Review(s_id));
                        }
                    });

                    Log.d("array", String.valueOf(history_dataArrayList.size()));
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
    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
    }
}