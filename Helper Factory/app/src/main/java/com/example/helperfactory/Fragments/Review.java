package com.example.helperfactory.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.helperfactory.Model.Insertresultmodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.Model.Reviewmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Review extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText review_text;
    RatingBar reating;
    Button review_button;
    ProgressDialog pdialog;
    UserSession userSession;
    Resultmodel.Data userdata;

    String review_str=null,rating_str=null,s_id,user_id;

    public Review(String s_id) {
        // Required empty public constructor
        this.s_id=s_id;
    }


//    public static Review newInstance(String param1, String param2) {
//        Review fragment = new Review();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        View view=inflater.inflate(R.layout.fragment_review, container, false);

        review_text=view.findViewById(R.id.review_txt);
        reating=view.findViewById(R.id.ratingBar);
        review_button=view.findViewById(R.id.review_button);
        userSession=new UserSession(getActivity());
        Gson gson=new Gson();
        userdata=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
        user_id=userdata.getClient_id();
        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReview();
                review();
            }
        });



        return view;
    }
    public void getReview(){
        review_str=review_text.getText().toString();
        rating_str= String.valueOf(reating.getRating());
    }
    public void review(){
            pdialog=new ProgressDialog(getActivity());
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.setIndeterminate(false);
            pdialog.show();

        Call<Insertresultmodel> call= Retrofitclient.getInstance().getMyApi().Add_Review("add_review",s_id,user_id,review_str,rating_str);
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                if (response.isSuccessful()){
                    Insertresultmodel reviewmodel= (Insertresultmodel) response.body();
                    Log.d("review update",reviewmodel.getError());
                    Log.d("review update",reviewmodel.getError_msg());
                    Toast.makeText(getContext(),"Review added",Toast.LENGTH_LONG).show();
                    pdialog.dismiss();
                    loadFragment(new MyBooking());
                }
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                Toast.makeText(getContext(),"somthing went wrong",Toast.LENGTH_LONG).show();
                pdialog.dismiss();

            }
        });

    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
}