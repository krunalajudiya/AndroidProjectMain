package com.technocometsolutions.salesdriver.fregment;


import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.ViewAttendanceAdapter;
import com.technocometsolutions.salesdriver.model.PunchInListModel;
import com.technocometsolutions.salesdriver.utlity.DU;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewAttendanceMangementFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public ViewAttendanceMangementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAttendanceMangementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAttendanceMangementFragment newInstance(int param1, String param2) {
        ViewAttendanceMangementFragment fragment = new ViewAttendanceMangementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
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
    public List<PunchInListModel.Item> viewAttendanceModelList=new ArrayList<>();
    public ViewAttendanceAdapter viewAttendanceAdapter;
    public RecyclerView rvCategoriesList;
    public CardView cvToDate,cvFromDate;
    String fromdateVal = "";
    String todateVal = "";
    TextView tv_fromdate,tv_todate;
    public LinearLayout lvGo;
    public String TAG="ViewAttendanceMangementFragment";
    public LoadingView loadingView;
    public ErrorView errorView;
    public SessionManager sessionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_view_attendance_mangement, container, false);
        sessionManager=new SessionManager(getActivity());
        rvCategoriesList=(RecyclerView)rootView.findViewById(R.id.rvCategoriesList);
        cvFromDate=(CardView)rootView.findViewById(R.id.cvFromDate);
        cvToDate=(CardView)rootView.findViewById(R.id.cvToDate);
        tv_fromdate=(TextView)rootView.findViewById(R.id.tv_fromdate);
        tv_todate=(TextView)rootView.findViewById(R.id.tv_todate);
        lvGo=(LinearLayout) rootView.findViewById(R.id.lvGo);
        viewAttendanceAdapter=new ViewAttendanceAdapter(getActivity(),viewAttendanceModelList);
        // Attach the adapter to the recyclerview to populate items
        rvCategoriesList.setAdapter(viewAttendanceAdapter);
        // Set layout manager to position the items
        rvCategoriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        // That's all!


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

        tv_fromdate.setText(fromdateVal);
        tv_todate.setText(todateVal);


        Log.d("dateaa",""+sessionManager.getId());
        Log.d("dateaa",""+fromdateVal);
        Log.d("dateaa",""+todateVal);
        getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
        //prepareMovieData();
        cvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        },
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
// If you're calling this from a support Fragment
                dpd.show(getFragmentManager(), "Datepickerdialog");*/
                showDateDialog(2);
            }
        });
        cvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(1);
                /*Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        },
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
// If you're calling this from a support Fragment
                dpd.show(getFragmentManager(), "Datepickerdialog");*/
            }
        });

        lvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
            }
        });


        return rootView;
    }

    public void showDateDialog(final int type) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.date_time_layout);
        dialog.setTitle("Choose Date");

        final DatePicker dp = (DatePicker) dialog
                .findViewById(R.id.datePicker1);
        Button btnDismissPicker = (Button) dialog
                .findViewById(R.id.btnDismissPicker);

        Button btnOkDismiss = (Button) dialog.findViewById(R.id.btnOkPicker);

        dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                    }
                });

        btnDismissPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        btnOkDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String day = dp.getDayOfMonth() + "";
                String month = (dp.getMonth() + 1) + "";
                String year = dp.getYear() + "";
                if (day.length() == 1) {
                    day = "0" + day;
                }
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String current_date = DU.formatDate(day, month, year);

                if(type == 1){
                    tv_fromdate.setText(current_date);
                }else{
                    tv_todate.setText(current_date);
                }

                dialog.dismiss();

              //  loadDetails();


            }
        });
        dialog.show();
    }

    public void prepareMovieData() {
        /*ViewAttendanceModel viewAttendanceModel;
        viewAttendanceModel = new ViewAttendanceModel("In:1:47PM ","Out: ","01/07/2019","P");
        viewAttendanceModelList.add(viewAttendanceModel);
        viewAttendanceModel = new ViewAttendanceModel("In: ","Out: ","02/07/2019","A");
        viewAttendanceModelList.add(viewAttendanceModel);
        viewAttendanceModel = new ViewAttendanceModel("In:11:51AM ","Out: ","03/07/2019","P");
        viewAttendanceModelList.add(viewAttendanceModel);
        viewAttendanceModel = new ViewAttendanceModel("In: ","Out: ","04/07/2019","A");
        viewAttendanceModelList.add(viewAttendanceModel);
        viewAttendanceModel = new ViewAttendanceModel("In: ","Out: ","05/07/2019","A");
        viewAttendanceModelList.add(viewAttendanceModel);
        viewAttendanceModel = new ViewAttendanceModel("In:11:51AM ","Out: ","06/07/2019","P");
        viewAttendanceModelList.add(viewAttendanceModel);
        viewAttendanceModel = new ViewAttendanceModel("In: ","Out: ","07/07/2019","W/O");*/

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        
    }








    public void getDataPunchOutView(String id, String fromdate, String todate) {
        viewAttendanceModelList.clear();
        loadingView = new LoadingView(getActivity());
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_attendance_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        PunchInListModel loginResponse = gson.fromJson(response, PunchInListModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {

                                viewAttendanceModelList.addAll(loginResponse.getItems());


                                viewAttendanceAdapter.notifyDataSetChanged();

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
                        getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
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
                params.put("from_date", fromdate);
                params.put("to_date", todate);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }


}
