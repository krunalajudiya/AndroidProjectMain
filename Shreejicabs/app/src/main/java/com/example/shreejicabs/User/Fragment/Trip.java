package com.example.shreejicabs.User.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.shreejicabs.Adapter.PackagelistAdapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.User.Adapter.TabAdapter;
import com.example.shreejicabs.User.Utlity.LoadingView;
import com.google.android.material.tabs.TabLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Trip extends Fragment {


    Calendar myCalendar;
    EditText from_city,to_city,date,time;
    RadioGroup car_type,communication_type;
    RadioButton suvradiobutton,sedanradiobutton,hatchbackradiobutton,smsradiobutton,callradiobutton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trip, container, false);

        from_city=view.findViewById(R.id.from_city);
        to_city=view.findViewById(R.id.to_city);
        date=view.findViewById(R.id.date);
        time=view.findViewById(R.id.time);
        car_type=view.findViewById(R.id.car_type);
        communication_type=view.findViewById(R.id.communication_type);
        suvradiobutton=view.findViewById(R.id.suv);
        hatchbackradiobutton=view.findViewById(R.id.hatchback);
        sedanradiobutton=view.findViewById(R.id.sedan);
        callradiobutton=view.findViewById(R.id.call);
        smsradiobutton=view.findViewById(R.id.sms);


        





        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("date","click");
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),R.style.MyAlertDialogStyle,datelistener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                /*new DatePickerDialog(getActivity(),R.style.MyAlertDialogStyle, datelistener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(timeset(selectedHour,selectedMinute));
                        Log.d("ffmin", String.valueOf(selectedMinute));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        time.setText(pad(selectedHour) + ":" + pad(selectedMinute));
//                    }
//                }, hour, minute, false);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();

            }
        });

        callradiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (smsradiobutton.isChecked()){
                        smsradiobutton.setChecked(false);
                    }
                }
            }
        });
        smsradiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (callradiobutton.isChecked()){
                        callradiobutton.setChecked(false);
                    }
                }
            }
        });
        suvradiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (hatchbackradiobutton.isChecked()){
                        hatchbackradiobutton.setChecked(false);
                    }
                    if (sedanradiobutton.isChecked()){
                        sedanradiobutton.setChecked(false);
                    }
                }
            }
        });
        hatchbackradiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (suvradiobutton.isChecked()){
                        suvradiobutton.setChecked(false);
                    }
                    if (sedanradiobutton.isChecked()){
                        sedanradiobutton.setChecked(false);
                    }
                }
            }
        });
        sedanradiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (suvradiobutton.isChecked()){
                        suvradiobutton.setChecked(false);
                    }
                    if (hatchbackradiobutton.isChecked()){
                        hatchbackradiobutton.setChecked(false);
                    }
                }
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date.setText(sdf.format(myCalendar.getTime()));
    }

    public String timeset(int hour,int minute){
        String strhr="";
        String strmn="";
        String ampm="";
        int hr=hour;
        int mn=minute;
        if(hr<12){
            ampm="AM";
        }
        else {
            ampm="PM";
        }
        if (hr==0){

            strhr="12";

        }else {
            if (hr>12){
                hr-=12;
                if (hr>=10){
                    strhr=Integer.toString(hr);
                }
                else {
                    strhr="0"+Integer.toString(hr);
                }
                Log.d("ffhr", String.valueOf(hr));
            }else {
                if (hr>=10){
                    strhr=Integer.toString(hr);
                }
                else {
                    strhr="0"+Integer.toString(hr);
                }
            }
        }

        if (mn>=10){
            strmn=Integer.toString(mn);
        }
        else {
            strmn="0"+Integer.toString(mn);
        }

        Log.d("fftime",strhr+":"+strmn+" "+ampm);
        return strhr+":"+strmn+" "+ampm;
    }
}