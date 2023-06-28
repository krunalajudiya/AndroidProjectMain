package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Service_pack#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Service_pack extends Fragment implements CompoundButton.OnCheckedChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button airport_btn,local_btn,local8_btn,local12_btn,oneway_btn,round_btn;
    CheckBox hatchback,sedan,suv,l_hatchback,l_sedan,l_suv,l8_hatchback,l8_sedan,l8_suv,l12_hatchback,l12_sedan,l12_suv,o_hatchback,o_sedan,o_suv,r_hatchback,r_sedan,r_suv;
    EditText hatchback_edt,sedan_edt,suv_edt,l_hatchback_edt,l_sedan_edt,l_suv_edt,l8_hatchback_edt,l8_sedan_edt,l8_suv_edt,l12_hatchback_edt,l12_sedan_edt,l12_suv_edt,o_hatchback_edt,o_sedan_edt,o_suv_edt,r_hatchback_edt,r_sedan_edt,r_suv_edt;
    RadioGroup communication,l_communication,l8_communication,l12_communication,o_communication,r_communication;
    RadioButton chat,call,l_chat,l_call,l8_chat,l8_call,l12_chat,l12_call,o_chat,o_call,r_chat,r_call;
    LottieAnimationView loading_screen;
    LinearLayout layout_main;
    String service_type_str,car_type_str,rate_str,communication_str,from_city_str="",to_city_str="",user_id;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    User user;
    UserSession userSession;
    AutoCompleteTextView from_city,to_city;
    ArrayList<String> city=new ArrayList<>();

    public Service_pack() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Service_pack.
     */
    // TODO: Rename and change types and number of parameters
    public static Service_pack newInstance(String param1, String param2) {
        Service_pack fragment = new Service_pack();
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
        View view= inflater.inflate(R.layout.fragment_service_pack, container, false);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
        airport_btn=(Button)view.findViewById(R.id.addairport_service);
        local_btn=(Button)view.findViewById(R.id.addlocal_service);
        local8_btn=(Button)view.findViewById(R.id.addlocal_service8);
        local12_btn=(Button)view.findViewById(R.id.addlocal_service12);
        oneway_btn=(Button)view.findViewById(R.id.addoneway_service);
        round_btn=(Button)view.findViewById(R.id.addroundtrip_service);
        hatchback=(CheckBox)view.findViewById(R.id.hatchback_check);
        sedan=(CheckBox)view.findViewById(R.id.sedan_check);
        suv=(CheckBox)view.findViewById(R.id.suv_check);
        l_hatchback=(CheckBox)view.findViewById(R.id.l_hatchback_check);
        l_sedan=(CheckBox)view.findViewById(R.id.l_sedan_check);
        l_suv=(CheckBox)view.findViewById(R.id.l_suv_check);
        l8_hatchback=(CheckBox)view.findViewById(R.id.l8_hatchback_check);
        l8_sedan=(CheckBox)view.findViewById(R.id.l8_sedan_check);
        l8_suv=(CheckBox)view.findViewById(R.id.l8_suv_check);
        l12_hatchback=(CheckBox)view.findViewById(R.id.l12_hatchback_check);
        l12_sedan=(CheckBox)view.findViewById(R.id.l12_sedan_check);
        l12_suv=(CheckBox)view.findViewById(R.id.l12_suv_check);
        o_hatchback=(CheckBox)view.findViewById(R.id.o_hatchback_check);
        o_sedan=(CheckBox)view.findViewById(R.id.o_sedan_check);
        o_suv=(CheckBox)view.findViewById(R.id.o_suv_check);
        r_hatchback=(CheckBox)view.findViewById(R.id.r_hatchback_check);
        r_sedan=(CheckBox)view.findViewById(R.id.r_sedan_check);
        r_suv=(CheckBox)view.findViewById(R.id.r_suv_check);
        hatchback_edt=(EditText)view.findViewById(R.id.hatchback_edt);
        sedan_edt=(EditText)view.findViewById(R.id.sedan_edt);
        suv_edt=(EditText)view.findViewById(R.id.suv_edt);
        l_hatchback_edt=(EditText)view.findViewById(R.id.l_hatchback_edt);
        l_sedan_edt=(EditText)view.findViewById(R.id.l_sedan_edt);
        l_suv_edt=(EditText)view.findViewById(R.id.l_suv_edt);
        l8_hatchback_edt=(EditText)view.findViewById(R.id.l8_hatchback_edt);
        l8_sedan_edt=(EditText)view.findViewById(R.id.l8_sedan_edt);
        l8_suv_edt=(EditText)view.findViewById(R.id.l8_suv_edt);
        l12_hatchback_edt=(EditText)view.findViewById(R.id.l12_hatchback_edt);
        l12_sedan_edt=(EditText)view.findViewById(R.id.l12_sedan_edt);
        l12_suv_edt=(EditText)view.findViewById(R.id.l12_suv_edt);
        o_hatchback_edt=(EditText)view.findViewById(R.id.o_hatchback_edt);
        o_sedan_edt=(EditText)view.findViewById(R.id.o_sedan_edt);
        o_suv_edt=(EditText)view.findViewById(R.id.o_suv_edt);
        r_hatchback_edt=(EditText)view.findViewById(R.id.r_hatchback_edt);
        r_sedan_edt=(EditText)view.findViewById(R.id.r_sedan_edt);
        r_suv_edt=(EditText)view.findViewById(R.id.r_suv_edt);
        from_city=(AutoCompleteTextView)view.findViewById(R.id.from_city);
        to_city=(AutoCompleteTextView)view.findViewById(R.id.to_city);
        communication=(RadioGroup)view.findViewById(R.id.communicationgroup);
        l_communication=(RadioGroup)view.findViewById(R.id.l_communicationgroup);
        l8_communication=(RadioGroup)view.findViewById(R.id.l8_communicationgroup);
        l12_communication=(RadioGroup)view.findViewById(R.id.l12_communicationgroup);
        o_communication=(RadioGroup)view.findViewById(R.id.o_communicationgroup);
        r_communication=(RadioGroup)view.findViewById(R.id.r_communicationgroup);
        chat=(RadioButton)view.findViewById(R.id.chat);
        call=(RadioButton)view.findViewById(R.id.call);
        l_chat=(RadioButton)view.findViewById(R.id.l_chat);
        l_call=(RadioButton)view.findViewById(R.id.l_call);
        l8_chat=(RadioButton)view.findViewById(R.id.l8_chat);
        l8_call=(RadioButton)view.findViewById(R.id.l8_call);
        l12_chat=(RadioButton)view.findViewById(R.id.l12_chat);
        l12_call=(RadioButton)view.findViewById(R.id.l12_call);
        o_chat=(RadioButton)view.findViewById(R.id.o_chat);
        o_call=(RadioButton)view.findViewById(R.id.o_call);
        r_chat=(RadioButton)view.findViewById(R.id.r_chat);
        r_call=(RadioButton)view.findViewById(R.id.r_call);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        layout_main=(LinearLayout)view.findViewById(R.id.layout_main);
        hatchback.setOnCheckedChangeListener(this);
        sedan.setOnCheckedChangeListener(this);
        suv.setOnCheckedChangeListener(this);
        l_hatchback.setOnCheckedChangeListener(this);
        l_sedan.setOnCheckedChangeListener(this);
        l_suv.setOnCheckedChangeListener(this);
        l8_hatchback.setOnCheckedChangeListener(this);
        l8_sedan.setOnCheckedChangeListener(this);
        l8_suv.setOnCheckedChangeListener(this);
        l12_hatchback.setOnCheckedChangeListener(this);
        l12_sedan.setOnCheckedChangeListener(this);
        l12_suv.setOnCheckedChangeListener(this);
        o_hatchback.setOnCheckedChangeListener(this);
        o_sedan.setOnCheckedChangeListener(this);
        o_suv.setOnCheckedChangeListener(this);
        r_hatchback.setOnCheckedChangeListener(this);
        r_sedan.setOnCheckedChangeListener(this);
        r_suv.setOnCheckedChangeListener(this);
        communication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                communication_str="";
                if (chat.isChecked())
                {
                    communication_str="Chat";

                }else if (call.isChecked())
                {
                    communication_str="Call";
                }

            }
        });
        l_communication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                communication_str="";
                if (l_chat.isChecked())
                {
                    communication_str="Chat";

                }else if (l_call.isChecked())
                {
                    communication_str="Call";
                }
            }
        });
        l8_communication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                communication_str="";
                if (l8_chat.isChecked())
                {
                    communication_str="Chat";

                }else if (l8_call.isChecked())
                {
                    communication_str="Call";
                }
            }
        });
        l12_communication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                communication_str="";
                if (l12_chat.isChecked())
                {
                    communication_str="Chat";

                }else if (l12_call.isChecked())
                {
                    communication_str="Call";
                }
            }
        });
        o_communication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                communication_str="";
                if (o_chat.isChecked())
                {
                    communication_str="Chat";

                }else if (o_call.isChecked())
                {
                    communication_str="Call";
                }
            }
        });
        r_communication.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                communication_str="";
                if (r_chat.isChecked())
                {
                    communication_str="Chat";

                }else if (r_call.isChecked())
                {
                    communication_str="Call";
                }
            }
        });
        airport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_type_str="";
                car_type_str="";
                rate_str="";
                service_type_str="Airport Transfer";
                if (hatchback.isChecked())
                {
                    rate_str=hatchback_edt.getText().toString();
                    car_type_str="HatchBack";
                }
                if (sedan.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+sedan_edt.getText().toString();
                        car_type_str=car_type_str+","+"Sedan";
                    }else{
                        rate_str=sedan_edt.getText().toString();
                        car_type_str="Sedan";
                    }
                }
                if (suv.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+suv_edt.getText().toString();
                        car_type_str=car_type_str+","+"SUV";
                    }else{
                        rate_str=suv_edt.getText().toString();
                        car_type_str="SUV";
                    }
                }
                Log.d("cartype",car_type_str);
                new Add_service_detail().execute();

            }
        });
        local_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_type_str="";
                car_type_str="";
                rate_str="";
                service_type_str="Local 4hr/40km";
                if (l_hatchback.isChecked())
                {
                    rate_str=l_hatchback_edt.getText().toString();
                    car_type_str="HatchBack";
                }
                if (l_sedan.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+l_sedan_edt.getText().toString();
                        car_type_str=car_type_str+","+"Sedan";
                    }else{
                        rate_str=l_sedan_edt.getText().toString();
                        car_type_str="Sedan";
                    }
                }
                if (l_suv.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+l_suv_edt.getText().toString();
                        car_type_str=car_type_str+","+"SUV";
                    }else{
                        rate_str=l_suv_edt.getText().toString();
                        car_type_str="SUV";
                    }
                }
                new Add_service_detail().execute();

            }
        });
        local8_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_type_str="";
                car_type_str="";
                rate_str="";
                service_type_str="Local 8hr/80km";
                if (l8_hatchback.isChecked())
                {
                    rate_str=l8_hatchback_edt.getText().toString();
                    car_type_str="HatchBack";
                }
                if (l8_sedan.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+l8_sedan_edt.getText().toString();
                        car_type_str=car_type_str+","+"Sedan";
                    }else{
                        rate_str=l8_sedan_edt.getText().toString();
                        car_type_str="Sedan";
                    }
                }
                if (l8_suv.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+l8_suv_edt.getText().toString();
                        car_type_str=car_type_str+","+"SUV";
                    }else{
                        rate_str=l8_suv_edt.getText().toString();
                        car_type_str="SUV";
                    }
                }
                new Add_service_detail().execute();

            }
        });
        local12_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_type_str="";
                car_type_str="";
                rate_str="";
                service_type_str="Local 12hr/120km";
                if (l12_hatchback.isChecked())
                {
                    rate_str=l12_hatchback_edt.getText().toString();
                    car_type_str="HatchBack";
                }
                if (l12_sedan.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+l12_sedan_edt.getText().toString();
                        car_type_str=car_type_str+","+"Sedan";
                    }else{
                        rate_str=l12_sedan_edt.getText().toString();
                        car_type_str="Sedan";
                    }
                }
                if (l12_suv.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+l12_suv_edt.getText().toString();
                        car_type_str=car_type_str+","+"SUV";
                    }else{
                        rate_str=l12_suv_edt.getText().toString();
                        car_type_str="SUV";
                    }
                }
                Log.d("communication",communication_str);
                new Add_service_detail().execute();

            }
        });
        oneway_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_type_str="";
                car_type_str="";
                rate_str="";
                service_type_str="One way";
                from_city_str=from_city.getText().toString().trim();
                to_city_str=to_city.getText().toString().trim();
                if (o_hatchback.isChecked())
                {
                    rate_str=o_hatchback_edt.getText().toString();
                    car_type_str="HatchBack";
                }
                if (o_sedan.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+o_sedan_edt.getText().toString();
                        car_type_str=car_type_str+","+"Sedan";
                    }else{
                        rate_str=o_sedan_edt.getText().toString();
                        car_type_str="Sedan";
                    }
                }
                if (o_suv.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+o_suv_edt.getText().toString();
                        car_type_str=car_type_str+","+"SUV";
                    }else{
                        rate_str=o_suv_edt.getText().toString();
                        car_type_str="SUV";
                    }
                }
                new Add_service_detail().execute();
            }
        });
        round_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service_type_str="";
                car_type_str="";
                rate_str="";
                service_type_str="Round Trip";
                if (r_hatchback.isChecked())
                {
                    rate_str=r_hatchback_edt.getText().toString();
                    car_type_str="HatchBack";
                }
                if (r_sedan.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+r_sedan_edt.getText().toString();
                        car_type_str=car_type_str+","+"Sedan";
                    }else{
                        rate_str=r_sedan_edt.getText().toString();
                        car_type_str="Sedan";
                    }
                }
                if (r_suv.isChecked())
                {
                    if (!TextUtils.isEmpty(car_type_str))
                    {
                        rate_str=rate_str+","+r_suv_edt.getText().toString();
                        car_type_str=car_type_str+","+"SUV";
                    }else{
                        rate_str=r_suv_edt.getText().toString();
                        car_type_str="SUV";
                    }
                }
                new Add_service_detail().execute();

            }
        });
        new LoadAllcity().execute();

        return view;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.hatchback_check:
                if (hatchback.isChecked())
                {
                    hatchback_edt.setVisibility(View.VISIBLE);
                }else{
                    hatchback_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.sedan_check:
                if (sedan.isChecked())
                {
                    sedan_edt.setVisibility(View.VISIBLE);
                }else{
                    sedan_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.suv_check:
                if (suv.isChecked())
                {
                    suv_edt.setVisibility(View.VISIBLE);
                }else{
                    suv_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l_hatchback_check:
                if (l_hatchback.isChecked())
                {
                    l_hatchback_edt.setVisibility(View.VISIBLE);
                }else{
                    l_hatchback_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l_sedan_check:
                if (l_sedan.isChecked())
                {
                    l_sedan_edt.setVisibility(View.VISIBLE);
                }else{
                    l_sedan_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l_suv_check:
                if (l_suv.isChecked())
                {
                    l_suv_edt.setVisibility(View.VISIBLE);
                }else{
                    l_suv_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l8_hatchback_check:
                if (l8_hatchback.isChecked())
                {
                    l8_hatchback_edt.setVisibility(View.VISIBLE);
                }else{
                    l8_hatchback_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l8_sedan_check:
                if (l8_sedan.isChecked())
                {
                    l8_sedan_edt.setVisibility(View.VISIBLE);
                }else{
                    l8_sedan_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l8_suv_check:
                if (l8_suv.isChecked())
                {
                    l8_suv_edt.setVisibility(View.VISIBLE);
                }else{
                    l8_suv_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l12_hatchback_check:
                if (l12_hatchback.isChecked())
                {
                    l12_hatchback_edt.setVisibility(View.VISIBLE);
                }else{
                    l12_hatchback_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l12_sedan_check:
                if (l12_sedan.isChecked())
                {
                    l12_sedan_edt.setVisibility(View.VISIBLE);
                }else{
                    l12_sedan_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.l12_suv_check:
                if (l12_suv.isChecked())
                {
                    l12_suv_edt.setVisibility(View.VISIBLE);
                }else{
                    l12_suv_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.o_hatchback_check:
                if (o_hatchback.isChecked())
                {
                    o_hatchback_edt.setVisibility(View.VISIBLE);
                }else{
                    o_hatchback_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.o_sedan_check:
                if (o_sedan.isChecked())
                {
                    o_sedan_edt.setVisibility(View.VISIBLE);
                }else{
                    o_sedan_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.o_suv_check:
                if (o_suv.isChecked())
                {
                    o_suv_edt.setVisibility(View.VISIBLE);
                }else{
                    o_suv_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.r_hatchback_check:
                if (r_hatchback.isChecked())
                {
                    r_hatchback_edt.setVisibility(View.VISIBLE);
                }else{
                    r_hatchback_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.r_sedan_check:
                if (r_sedan.isChecked())
                {
                    r_sedan_edt.setVisibility(View.VISIBLE);
                }else{
                    r_sedan_edt.setVisibility(View.GONE);
                }
                break;
            case R.id.r_suv_check:
                if (r_suv.isChecked())
                {
                    r_suv_edt.setVisibility(View.VISIBLE);
                }else{
                    r_suv_edt.setVisibility(View.GONE);
                }
                break;

        }
    }
    class Add_service_detail extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "Add_services"));
                params.add(new BasicNameValuePair("Reg_id", user_id));
                params.add(new BasicNameValuePair("Service_type", service_type_str));
                params.add(new BasicNameValuePair("Car_type", car_type_str));
                params.add(new BasicNameValuePair("Rate", rate_str));
                params.add(new BasicNameValuePair("Communication", communication_str));
                params.add(new BasicNameValuePair("From_city", from_city_str));
                params.add(new BasicNameValuePair("To_city", to_city_str));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));

                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (success==200) {
                load(new MyProfile());
                Toast.makeText(getActivity(),"Add Service SuccessFully",Toast.LENGTH_LONG).show();


            }else{
                Toast.makeText(getActivity(),"Add Service  Not SuccessFully",Toast.LENGTH_LONG).show();
            }

        }
    }
    class LoadAllcity extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "carservicecity"));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("city");
                    // looping through All Products
                    city.add("Select City");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String city_name = c.getString("city_name");

                        city.add(city_name);

                    }



                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            layout_main.setVisibility(View.VISIBLE);
            loading_screen.setVisibility(View.GONE);
            if (success==200) {
                if (getActivity()!=null) {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city);
                    from_city.setAdapter(adapter);
                    to_city.setAdapter(adapter);
                }



            }

        }
    }
    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}