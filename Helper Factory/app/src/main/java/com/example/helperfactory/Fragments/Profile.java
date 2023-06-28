package com.example.helperfactory.Fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Activity.Number_Verification;
import com.example.helperfactory.Activity.Update_profile;
import com.example.helperfactory.BuildConfig;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.MainActivity;
import com.example.helperfactory.Model.Couponmodel;
import com.example.helperfactory.Model.Refermodel;
import com.example.helperfactory.Model.Resultmodel;
import com.example.helperfactory.R;
import com.example.helperfactory.Remote.Retrofitclient;
import com.example.helperfactory.Session.UserSession;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView logout,user_number,login,welcome_msg;
    ImageView edit_profile,user_profile_img;
    UserSession userSession;
    Resultmodel.Data data;
    LinearLayout verfiylayout,shareapp,rateapp,contactus,privacypolicy;
    TextView refercode,yourrefer,couponcode,couponamount,coupondate;
    RelativeLayout coupon;
    ProgressDialog pdialog;



    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        logout=(TextView)view.findViewById(R.id.logout);
        user_number=(TextView)view.findViewById(R.id.usernumber);
        edit_profile=(ImageView)view.findViewById(R.id.edit_profile);
        verfiylayout=(LinearLayout)view.findViewById(R.id.verifylayout);
        login=(TextView)view.findViewById(R.id.login);
        welcome_msg=(TextView)view.findViewById(R.id.welcomemsg_txt);
        user_profile_img=(ImageView)view.findViewById(R.id.user_profile_img);
        shareapp=(LinearLayout)view.findViewById(R.id.shareapp);
        rateapp=(LinearLayout)view.findViewById(R.id.rateapp);
        contactus=(LinearLayout)view.findViewById(R.id.contactus);
        privacypolicy=(LinearLayout)view.findViewById(R.id.privacypolicy);
        refercode=view.findViewById(R.id.refercode);
        yourrefer=view.findViewById(R.id.yourrefer);
        couponcode=view.findViewById(R.id.couponcode);
        couponamount=view.findViewById(R.id.couponamount);
        coupondate=view.findViewById(R.id.coupondate);
        coupon=view.findViewById(R.id.coupon);

        userSession=new UserSession(getActivity());


        pdialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();


        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    if (userSession.isLoggedIn()){
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"+"Refer Code: "+data.getReferral_code();
                    }else {
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    }

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update=new Intent(getActivity(), Update_profile.class);
                startActivity(update);
            }
        });
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(new Contact_Us());
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(new privacypolicy());
            }
        });


        if (userSession.isLoggedIn())
        {
            Gson gson=new Gson();
            data=gson.fromJson(userSession.getUserDetails(),Resultmodel.Data.class);
            user_number.append(data.getMobile());
            if (data.getPhoto()!=null)
            {
                Glide.with(getActivity()).load(Constant.IMAGE_URL+data.getPhoto()).placeholder(R.drawable.user).error(R.drawable.user).into(user_profile_img);
            }
            if (data.getClient_name()!=null || !TextUtils.isEmpty(data.getClient_name()))
            {
                welcome_msg.append(" "+data.getClient_name());
            }
            logout.setVisibility(View.VISIBLE);
            verfiylayout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            if (data.getReferral_code()!=null){
                refercode.setText(data.getReferral_code());
            }
            referdata();
            coupondata();
        }else{
            logout.setVisibility(View.GONE);
            verfiylayout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Number_Verification.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.logoutUser();
                getActivity().finish();
            }
        });

        pdialog.dismiss();

        return view;
    }

    private void referdata(){


        Call<Refermodel> refermodelCall= Retrofitclient.getInstance().getMyApi().Refer("get_referral_data",data.getReferral_code());
        refermodelCall.enqueue(new Callback<Refermodel>() {
            @Override
            public void onResponse(Call<Refermodel> call, Response<Refermodel> response) {

                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Refermodel refermodel = response.body();
                    Refermodel.refer data=refermodel.getData();
                    Log.d("fffff111", refermodel.getError_msg());
                    if (refermodel.getError() == 200) {
                        if (refermodel.getData().getReferal_client().toString()!=null && refermodel.getData().getTotal_referral().toString()!=null){
                            yourrefer.setText("Friends "+refermodel.getData().getReferal_client()+" out of "+refermodel.getData().getTotal_referral());
                        }

                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Refermodel> call, Throwable t) {
                //handle error or failure cases here
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
            }
        });


    }
    public void coupondata(){
        Call<Couponmodel> call= Retrofitclient.getInstance().getMyApi().Coupon("fetch_coupon",data.getClient_id());
        call.enqueue(new Callback<Couponmodel>() {
            @Override
            public void onResponse(Call<Couponmodel> call, Response<Couponmodel> response) {

                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Couponmodel couponmodel = response.body();
                    Couponmodel.coupon data=couponmodel.getData();
                    Log.d("fffff111", couponmodel.getError_msg());
                    if (couponmodel.getError() == 200) {
                        if (couponmodel.getData().getCoupon_code()!=null){
                            coupon.setVisibility(View.VISIBLE);
                            couponcode.setText(couponmodel.getData().getCoupon_code());
                            couponamount.setText(couponmodel.getData().getCoupon_amount());
                            coupondate.setText(couponmodel.getData().getExpire_date());
                        }

                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Couponmodel> call, Throwable t) {
                //handle error or failure cases here
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());

            }
        });


    }

    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}