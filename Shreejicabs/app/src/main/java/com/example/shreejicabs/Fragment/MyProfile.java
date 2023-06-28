package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.BuildConfig;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.shreejicabs.Fragment.add_vehicle.TAG_SUCCESS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout userinfo,myrequredvehicle,myavailability,mypackage,citypref,addvehicle,share;
    TextView username;
    User user;
    UserSession userSession;
    String user_name;
    String referralcode_str;
    TextView referralcode,totalpoint;
    int success;
    LinearLayout layout_main;
    LottieAnimationView loading_screen;
    String totalpoint_str;

    JSONParser jParser = new JSONParser();

    public MyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfile newInstance(String param1, String param2) {
        MyProfile fragment = new MyProfile();
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
        View view= inflater.inflate(R.layout.fragment_my_profile, container, false);
        userinfo=(LinearLayout)view.findViewById(R.id.userinfo);
        myrequredvehicle=(LinearLayout)view.findViewById(R.id.myrequiredvehicle);
        myavailability=(LinearLayout)view.findViewById(R.id.myavailability);
        mypackage=(LinearLayout)view.findViewById(R.id.mypackage);
        citypref=(LinearLayout) view.findViewById(R.id.citypreference);
        username=(TextView)view.findViewById(R.id.user_name);
        addvehicle=(LinearLayout)view.findViewById(R.id.addvehicle);
        referralcode=(TextView)view.findViewById(R.id.referralcode);
        share=(LinearLayout)view.findViewById(R.id.share);
        layout_main=(LinearLayout) view.findViewById(R.id.layoutmain);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        totalpoint=view.findViewById(R.id.yourpoint);

        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_name=user.getName();
//        username.setText("Hii I'm "+user_name);

        //new LoadUserDetails().execute();
        referralcode.setText(MainActivity.referralcode_str);
        totalpoint.setText(MainActivity.totalpoint_str);
        userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Edit_Profile());

            }
        });
        myrequredvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new My_required_vehicle());
            }
        });
        myavailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MyAvaliability());

            }
        });
        mypackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Service_pack());
            }
        });
        citypref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new City_preference());
            }
        });
        addvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new add_vehicle());
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    if (userSession.isLoggedIn()){
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"+"Refer Code: "+MainActivity.referralcode_str;
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



        return view;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class LoadUserDetails extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
//            pDialog.setMessage("Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "clientdetail"));
                params.add(new BasicNameValuePair("client_id", user.getId()));
                Log.d("id",user.getId());
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("UserDetail: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    referralcode_str=c1.getString("referral_code");
                    totalpoint_str=c1.getString("total_earn");




                    Log.d("referral_code",referralcode_str);

                        referralcode.setText(referralcode_str);

                        totalpoint.setText(totalpoint_str);



                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
            layout_main.setVisibility(View.VISIBLE);
            loading_screen.setVisibility(View.GONE);

        }
    }

}