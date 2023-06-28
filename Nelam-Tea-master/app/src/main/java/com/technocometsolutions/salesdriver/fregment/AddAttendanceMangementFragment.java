package com.technocometsolutions.salesdriver.fregment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.AddAttendanceAdapter;
import com.technocometsolutions.salesdriver.model.AttendanceManagementModel;
import com.technocometsolutions.salesdriver.model.PunchInModel;
import com.technocometsolutions.salesdriver.utlity.DU;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.LocationReceiverServices;
import com.technocometsolutions.salesdriver.utlity.SessionManager;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddAttendanceMangementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public AddAttendanceMangementFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddAttendanceMangementFragment newInstance(int param1, String param2) {
        AddAttendanceMangementFragment fragment = new AddAttendanceMangementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    public List<PunchInModel.Item> attendanceMangementModelList=new ArrayList<>();
    public List<AttendanceManagementModel.Item> itemArrayList=new ArrayList<>();
    public RecyclerView rvCategoriesList;
    public AddAttendanceAdapter addAttendanceAdapter;
    public SessionManager sessionManager;
    public CardView btnAttendManagentPunchIn,btnAdvancePunchOut;
    public String TAG="AddAttendanceMangementFragment";
    public LoadingView loadingView;
    public ErrorView errorView;
    String fromdateVal = "";
    String todateVal = "";
    TextView tv_todaydate,tv_date_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_add_attendance_mangement, container, false);
        sessionManager=new SessionManager(getActivity());
        btnAttendManagentPunchIn=(CardView)rootView.findViewById(R.id.btnAttendManagentPunchIn);
        btnAdvancePunchOut=(CardView)rootView.findViewById(R.id.btnAdvancePunchOut);
        tv_todaydate=(TextView) rootView.findViewById(R.id.tv_todaydate);
        tv_date_time=(TextView) rootView.findViewById(R.id.tv_date_time);
        rvCategoriesList=(RecyclerView)rootView.findViewById(R.id.rvCategoriesList);
        addAttendanceAdapter=new AddAttendanceAdapter(getActivity(),itemArrayList);
        // Attach the adapter to the recyclerview to populate items
        rvCategoriesList.setAdapter(addAttendanceAdapter);
        // Set layout manager to position the items
        rvCategoriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        // That's all!


        getDataAttendanceManagement(sessionManager.getId());
        btnAttendManagentPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataPunchIn(sessionManager.getId());
            }
        });
        btnAdvancePunchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataPunchOut(sessionManager.getId());
            }
        });


        int current_date = 0;
        int current_year = 0;
        int current_month = 0;

        Calendar c = Calendar.getInstance();
        current_year = c.get(Calendar.YEAR);
        current_month = c.get(Calendar.MONTH);
        current_month = current_month + 1;
        current_date = c.get(Calendar.DAY_OF_MONTH);

        Log.d("current_month : ", "" + current_month);
        Log.d("current_year : ", "" + current_year);
        Log.d("current_date : ", "" + current_date);

        String str_current_month = current_month < 10 ? "0" + String.valueOf(current_month) : String.valueOf(current_month);
        String str_current_date = current_date < 10 ? "0" + String.valueOf(current_date) : String.valueOf(current_date);

        fromdateVal = DU.formatDate("01", str_current_month, current_year + "");
        todateVal = DU.formatDate(str_current_date, str_current_month, current_year + "");
        tv_todaydate.setText("Today's Punch In - Out History ("+todateVal+")");


       /* SimpleDateFormat sdf3 = new SimpleDateFormat("dd mm,yyyy HH.mm");

            String date4 = sdf3.format(new Date());
            //new format
            SimpleDateFormat sdf4 = new SimpleDateFormat("dd mm,yyyy hh.mm aa");
            //formatting the given time to new format with AM/PM
            System.out.println("Given date and time in AM/PM: "+sdf4.format(date4));*/

        tv_date_time.setText(""+ current_date +" "+new SimpleDateFormat("MMMM").format(c.getTime())+", "+ current_year +" "+new SimpleDateFormat("hh:mm aa").format(c.getTime()));
        /*int finalCurrent_date = current_date;
        int finalCurrent_year = current_year;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tv_date_time.setText(""+ finalCurrent_date +" "+new SimpleDateFormat("MMMM").format(c.getTime())+", "+ finalCurrent_year +" "+new SimpleDateFormat("hh:mm:ss aa").format(c.getTime()));
            }
        },500,1000);*/
        /*Timer  myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }, 1000, 1000); // initial delay 1 second, interval 1 second*/



        prepareMovieData();
        return rootView;
    }

    private static String getMonth(String date) throws ParseException{
        Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM").format(cal.getTime());
        return monthName;
    }


    public void getDataPunchIn(String id) {
        loadingView = new LoadingView(getActivity());
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_punch_in);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        PunchInModel loginResponse = gson.fromJson(response, PunchInModel.class);
                        if (loginResponse.getSuccess())
                        {
                            getActivity().startService(new Intent(getActivity(), LocationReceiverServices.class));
                            if (loginResponse.getItems() != null) {

                                getDataAttendanceManagement(id);

                                //   attendanceMangementModelList.add((PunchInModel.Item) loginResponse.getItems());
                                //  addAttendanceAdapter.notifyDataSetChanged();

                                //itemList.addAll(loginResponse.getItems());

                                //sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());

                            /*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                                //dixit
                                // categoriesListAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        getDataPunchIn(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        getActivity().onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    public void getDataPunchOut(String id) {
        loadingView = new LoadingView(getActivity());
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_punch_out);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        PunchInModel loginResponse = gson.fromJson(response, PunchInModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                getActivity().stopService(new Intent(getActivity(), LocationReceiverServices.class));
                                getDataAttendanceManagement(id);
                                //   attendanceMangementModelList.add((PunchInModel.Item) loginResponse.getItems());
                                //  addAttendanceAdapter.notifyDataSetChanged();

                                //itemList.addAll(loginResponse.getItems());

                                //sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());

                            /*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                                //dixit
                                // categoriesListAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        getActivity().onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }






    public void getDataAttendanceManagement(String id) {
        itemArrayList.clear();
        loadingView = new LoadingView(getActivity());
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_punch_attendance_management);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        AttendanceManagementModel loginResponse = gson.fromJson(response, AttendanceManagementModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {

                                itemArrayList.addAll(loginResponse.getItems());
                                //   attendanceMangementModelList.add((PunchInModel.Item) loginResponse.getItems());
                                  addAttendanceAdapter.notifyDataSetChanged();

                                //itemList.addAll(loginResponse.getItems());

                                //sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());

                            /*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                                //dixit
                                // categoriesListAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d(TAG, "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        getDataPunchIn(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        getActivity().onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }



    public void prepareMovieData() {
        /*AddAttendanceMangementModel addAttendanceMangementModel;
        addAttendanceMangementModel = new AddAttendanceMangementModel("in","","PUNCH IN:15:07:2019 10:12:53 AM");
        attendanceMangementModelList.add(addAttendanceMangementModel);
        addAttendanceMangementModel = new AddAttendanceMangementModel("out","","PUNCH OUT:15:07:2019 9:41:03 PM");
        attendanceMangementModelList.add(addAttendanceMangementModel);*/

    }
}