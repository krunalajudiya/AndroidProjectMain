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
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.DealerCompletePaymentAdapter;
import com.technocometsolutions.salesdriver.adapter.DealerPaymentPendingAdapter;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.ChildEmployeeItemModel;
import com.technocometsolutions.salesdriver.model.ChildEmployeeModel;
import com.technocometsolutions.salesdriver.model.CompletePaymentItemModel;
import com.technocometsolutions.salesdriver.model.CompletePaymentModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerPaymentPandingItemModel;
import com.technocometsolutions.salesdriver.model.DesignationItemModel;
import com.technocometsolutions.salesdriver.model.DesignationModel;
import com.technocometsolutions.salesdriver.utlity.DU;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class CompletePaymentFragment extends Fragment {

    public SearchableSpinner sp_dealers;
    public RecyclerView rv_payment;
    public RecyclerView.LayoutManager layoutManager;
    public DealerPaymentPendingAdapter mAdapter;
    public DealerCompletePaymentAdapter completePaymentAdapter;
    public SessionManager sessionManager;
    public String dealer_id="";
    public StateAdapter stateAdapter;
    public final ArrayList<String> mStrings = new ArrayList<>();
    public ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public List<DealerPaymentPandingItemModel> dealerPaymentPandingItemModelArrayList = new ArrayList<>();
    public LinearLayout lv_select_dealer_or_not;
    String fromdateVal = "";
    String todateVal = "";
    TextView tv_fromdate,tv_todate;
    String child_emp_id;
    String designation_id;

    public List<DesignationItemModel> designationItemModelList;
    public List<ChildEmployeeItemModel> childEmployeeItemModelList;
    public List<CompletePaymentItemModel> completePaymentItemModelList;
    public final ArrayList<String> designantionList = new ArrayList<>();
    public final ArrayList<String> childEmployeeList = new ArrayList<>();
    public SearchableSpinner sp_designation;
    public SearchableSpinner sp_child_employee;
    public StateAdapter stateAdapterDEsignation;
    public StateAdapter stateAdapterChildEmployeee;
    public CardView cvToDate,cvFromDate;
    public LinearLayout lvGo;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_complete_payment, container, false);
        sessionManager=new SessionManager(getActivity());
        lv_select_dealer_or_not = view.findViewById(R.id.lv_select_dealer_or_not);

        sp_designation= view.findViewById(R.id.sp_designation);
        sp_child_employee = view.findViewById(R.id.sp_designation_emp);
        cvFromDate=(CardView)view.findViewById(R.id.cvFromDate);
        cvToDate=(CardView)view.findViewById(R.id.cvToDate);
        tv_fromdate=(TextView)view.findViewById(R.id.tv_fromdate);
        tv_todate=(TextView)view.findViewById(R.id.tv_todate);
        lvGo=(LinearLayout) view.findViewById(R.id.lvGo);
        rv_payment = view.findViewById(R.id.rv_payment);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_payment.setHasFixedSize(true);
        rv_payment.setLayoutManager(layoutManager);

        lv_select_dealer_or_not.setVisibility(View.VISIBLE);
        designationItemModelList=new ArrayList<>();
        childEmployeeItemModelList=new ArrayList<>();
        completePaymentItemModelList=new ArrayList<>();
        int current_date = 0;
        int current_year = 0;
        int current_month = 0;

        Log.d("getEmployeeId",""+sessionManager.getId());
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
        getCompletePaymentList(sessionManager.getId(),fromdateVal,todateVal);
        if (sessionManager.getKEY_designation().equals("7"))
        {
            sp_designation.setVisibility(View.GONE);
        }
        else
        {
            sp_designation.setVisibility(View.VISIBLE);
        }

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

                getCompletePaymentList(sessionManager.getId(),fromdateVal,todateVal);
            }
        });
        String emp_id=sessionManager.getId();
        getDesignationList(sessionManager.getKEY_designation());

        stateAdapterDEsignation = new StateAdapter(getActivity(), designantionList,"Select Designation");
        sp_designation.setAdapter(stateAdapterDEsignation);
        sp_designation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    sp_child_employee.setVisibility(View.GONE);
                    designation_id="";

                } else {

                    sp_child_employee.setVisibility(View.VISIBLE);
                    //dealer_id=dealerListItemModels.get(position).getId();
                    //lv_select_dealer.setVisibility(View.VISIBLE);

                    String dealerSplit= stateAdapterDEsignation.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];




                    for (int i=0;i<designationItemModelList.size();i++)
                    {
                        if (designationItemModelList.get(i).getId().equals(splitDelarId))
                        {
                            designation_id=designationItemModelList.get(i).getId();
                            break;
                        }
                    }
                    getChildEmployeeList(designation_id,""+sessionManager.getId());

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        stateAdapterChildEmployeee = new StateAdapter(getActivity(), childEmployeeList,"Select Designation of Employee");
        sp_child_employee.setAdapter(stateAdapterChildEmployeee);
        sp_child_employee.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    child_emp_id="";

                } else {

                    //child_emp_id=childEmployeeItemModelList.get(position-1).getId();
                    dealer_id=dealerListItemModels.get(position).getId();
                    //lv_select_dealer.setVisibility(View.VISIBLE);

                    String dealerSplit= stateAdapterChildEmployeee.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];



                    for (int i=0;i<childEmployeeItemModelList.size();i++)
                    {
                        if (childEmployeeItemModelList.get(i).getId().equals(splitDelarId))
                        {
                            child_emp_id=childEmployeeItemModelList.get(i).getId();
                            break;
                        }
                    }
                    getCompletePaymentList(child_emp_id,fromdateVal,todateVal);


                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
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
    public void getDesignationList(String designation) {
        //spinnar
        designantionList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_designation);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);


                        Gson gson = new Gson();
                        DesignationModel loginResponse = gson.fromJson(response, DesignationModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                designationItemModelList=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<designationItemModelList.size();i++)
                                {
                                    String data = loginResponse.getItems().get(i).getDesignation();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();designantionList.add(data + " " + data1);

                                    //designantionList.add(loginResponse.getItems().get(i).getDesignation());
                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dd", "onErrorResponse: "+error.getMessage());

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("designation", designation);

                return params;
            };
        };

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getChildEmployeeList(String designation,String emp_id) {
        //spinnar
        childEmployeeItemModelList.clear();
        childEmployeeList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_child_employeee);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);


                        Gson gson = new Gson();
                        ChildEmployeeModel loginResponse = gson.fromJson(response, ChildEmployeeModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                childEmployeeItemModelList=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<childEmployeeItemModelList.size();i++)
                                {
                                    String data = loginResponse.getItems().get(i).getEmploye();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();childEmployeeList.add(data + " " + data1);

                                    //childEmployeeList.add(loginResponse.getItems().get(i).getEmploye());
                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dd", "onErrorResponse: "+error.getMessage());

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("designation", designation);
                params.put("emp_id", emp_id);

                return params;
            };
        };

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getCompletePaymentList(String childEmpId,String form,String to) {
        //spinnar
        completePaymentItemModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_dealer_complete_payment);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);


                        Gson gson = new Gson();
                        CompletePaymentModel loginResponse = gson.fromJson(response, CompletePaymentModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                completePaymentItemModelList=loginResponse.getItems();
                                Log.d("dixitttttComplete", "onCreate: "+completePaymentItemModelList.size());
                                completePaymentAdapter= new DealerCompletePaymentAdapter(getActivity(),completePaymentItemModelList);
                                rv_payment.setAdapter(completePaymentAdapter);


                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dd", "onErrorResponse: "+error.getMessage());

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id",childEmpId);
                params.put("from",form);
                params.put("to",to);
                return params;
            };
        };

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
}
