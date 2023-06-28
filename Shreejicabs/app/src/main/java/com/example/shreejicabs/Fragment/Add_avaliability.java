package com.example.shreejicabs.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.Other.MySingleton;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Add_avaliability extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText date,time,comment;
    AutoCompleteTextView from,to;
    RadioButton chat,call;
    RadioGroup radioGroup;
    Spinner car_type,service;
    String user_id,date_str,time_str,comment_str,commucation_str="Chat",service_str,car_str,from_str,to_str;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<String>  service_ar=new ArrayList<>();
    ArrayList<String> cartype_ar=new ArrayList<>();
    ArrayList<String> city=new ArrayList<>();
    Calendar myCalendar;
    Button add_avaliability;
    User user;
    UserSession userSession;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA0GlGw6A:APA91bFQAJPgsf8J1JZ0MEaDDSAy4gNBu1hkjmi08TlrzSd2OGT6ajTB14rcooZdeBoWpNb7q4Q5aB9BdDKpApLQ6Djb_yMkRpNkusI9WYcODy9tEkjl1diI10DzKbVZNKevCtTRm6hA";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String TOPIC = "/topics/userABC";


    public Add_avaliability() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_avaliability.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_avaliability newInstance(String param1, String param2) {
        Add_avaliability fragment = new Add_avaliability();
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
        city.clear();
        service_ar.clear();
        cartype_ar.clear();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_avaliability, container, false);
        from=(AutoCompleteTextView)view.findViewById(R.id.from);
        to=(AutoCompleteTextView)view.findViewById(R.id.to);
        date=(EditText)view.findViewById(R.id.date);
        time=(EditText)view.findViewById(R.id.time);
        car_type=(Spinner) view.findViewById(R.id.cartype);
        service=(Spinner) view.findViewById(R.id.service);
        chat=(RadioButton)view.findViewById(R.id.chat);
        call=(RadioButton)view.findViewById(R.id.call);
        comment=(EditText)view.findViewById(R.id.comment);
        radioGroup=(RadioGroup)view.findViewById(R.id.communicationgroup);
        add_avaliability=(Button)view.findViewById(R.id.addavaliability);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
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
                mTimePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( timeset(selectedHour,selectedMinute));
                        Log.d("fftime",selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");


                mTimePicker.show();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (chat.isChecked())
                {
                    commucation_str=chat.getText().toString().trim();
                }else if (call.isChecked())
                {
                    commucation_str=call.getText().toString().trim();
                }
            }
        });
        add_avaliability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getvalue()){
                    new Add_avaliability_detail().execute();
                }


            }
        });
        new LoadAllservices().execute();
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                service_str=service_ar.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        car_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                car_str=cartype_ar.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date.setText(sdf.format(myCalendar.getTime()));
    }
    public boolean getvalue()
    {
        if(from.getText().toString().trim().length()>0 && to.getText().toString().trim().length()>0
                && date.getText().toString().trim().length()>0 && time.getText().toString().trim().length()>0){
        from_str=from.getText().toString().trim();
        to_str=to.getText().toString().trim();
        date_str=date.getText().toString().trim();
        time_str=time.getText().toString().trim();
        comment_str=comment.getText().toString().trim();
        return true;
        }
        else {
            Toast.makeText(getContext(),"Fill details",Toast.LENGTH_LONG).show();
        }
        comment_str=comment.getText().toString().trim();
        return false;

    }
    class LoadAllservices extends AsyncTask<String, String, String> {
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
                    products=c1.getJSONArray("cartype");
                    cartype_ar.add("Select Car");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String cartype = c.getString("cartype");
                        cartype_ar.add(cartype);

                    }
                    products=c1.getJSONArray("service");
                    service_ar.add("Select Service");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String service_type = c.getString("service_type");
                        service_ar.add(service_type);

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
            if (success==200) {
                if (getActivity()!=null) {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city);
                    from.setAdapter(adapter);
                    to.setAdapter(adapter);
                    ArrayAdapter caradapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, cartype_ar);
                    car_type.setAdapter(caradapter);
                    ArrayAdapter serviceadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, service_ar);
                    service.setAdapter(serviceadapter);
                }


            }

        }
    }
    class Add_avaliability_detail extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "add_availablity"));
                params.add(new BasicNameValuePair("User_id", user_id));
                params.add(new BasicNameValuePair("From", from_str));
                params.add(new BasicNameValuePair("To", to_str));
                params.add(new BasicNameValuePair("date", date_str));
                params.add(new BasicNameValuePair("time", time_str));
                params.add(new BasicNameValuePair("Car_type", car_str));
                params.add(new BasicNameValuePair("Service", "One Way"));
                params.add(new BasicNameValuePair("Comment", comment_str));
                params.add(new BasicNameValuePair("Communication", commucation_str));
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
                load(new All_avaliability_display());
                Toast.makeText(getActivity(),"Add car SuccessFully",Toast.LENGTH_LONG).show();
                String message="New Available Car For this Date:"+date_str+" And Car type is "+car_str;
                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", from_str+" To "+to_str);
                    notifcationBody.put("message", message);
                    notifcationBody.put("userid",user_id);

                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }
                //sendNotification(notification);
            }else{
                Toast.makeText(getActivity(),"Add car  Not SuccessFully",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
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

    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }







}
