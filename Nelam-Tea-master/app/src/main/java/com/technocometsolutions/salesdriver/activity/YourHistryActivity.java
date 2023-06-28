package com.technocometsolutions.salesdriver.activity;

import android.app.Dialog;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.GetOrderListAdapter;
import com.technocometsolutions.salesdriver.adapter.SalesReportListAdapter;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.ChildEmployeeItemModel;
import com.technocometsolutions.salesdriver.model.ChildEmployeeModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.DesignationItemModel;
import com.technocometsolutions.salesdriver.model.DesignationModel;
import com.technocometsolutions.salesdriver.model.GetDelalerListModel;
import com.technocometsolutions.salesdriver.model.GetOrderListModel;
import com.technocometsolutions.salesdriver.model.SalesReportItemModel;
import com.technocometsolutions.salesdriver.model.SalesReportListModel;
import com.technocometsolutions.salesdriver.utlity.DU;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class YourHistryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public RecyclerView rvCategoriesList;
    public RecyclerView rvSalesReportList;
    public CardView cvToDate,cvFromDate;
    public LinearLayout lvGo;
    String fromdateVal = "";
    String todateVal = "";
    TextView tv_fromdate,tv_todate;
    public SearchableSpinner sp_dealers;
    public StateAdapter stateAdapter;
    public SearchableSpinner sp_designation;
    public SearchableSpinner sp_child_employee;
    public StateAdapter stateAdapterDEsignation;
    public StateAdapter stateAdapterChildEmployeee;
    public final ArrayList<String> mStrings = new ArrayList<>();
    public final ArrayList<String> designantionList = new ArrayList<>();
    public final ArrayList<String> childEmployeeList = new ArrayList<>();
    public ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public SessionManager sessionManager;
    /*public LoadingView loadingView;
    public ErrorView errorView;*/
    public GetOrderListAdapter getOrderListAdapter;
    public SalesReportListAdapter salesReportListAdapter;
    public List<GetDelalerListModel> getDelalerListModelList;
    String child_emp_id;
    public List<DesignationItemModel> designationItemModelList;
    public List<ChildEmployeeItemModel> childEmployeeItemModelList;
    public List<SalesReportItemModel> salesReportItemModelList;
    Boolean from_date_mod=false;
    Boolean to_date_mod=false;
    LoadingView loadingView;
    TextView nothingdatatxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_histry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager=new SessionManager(this);
        rvCategoriesList=(RecyclerView)findViewById(R.id.my_recycler_order);
        rvSalesReportList=(RecyclerView)findViewById(R.id.rvSalesReport);
        sp_dealers = findViewById(R.id.sp_dealers);
        sp_designation= findViewById(R.id.sp_designation);
        sp_child_employee = findViewById(R.id.sp_designation_emp);
        cvFromDate=(CardView)findViewById(R.id.cvFromDate);
        cvToDate=(CardView)findViewById(R.id.cvToDate);
        tv_fromdate=(TextView)findViewById(R.id.tv_fromdate);
        tv_todate=(TextView)findViewById(R.id.tv_todate);
        lvGo=(LinearLayout) findViewById(R.id.lvGo);
        nothingdatatxt=findViewById(R.id.nothing_data);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(YourHistryActivity.this);
        rvCategoriesList.setLayoutManager(linearLayoutManager);

//        if (sessionManager.getKEY_designation().equals("7"))
//        {
//            sp_designation.setVisibility(View.GONE);
//        }
//        else
//        {
//            sp_designation.setVisibility(View.VISIBLE);
//        }

        getDelalerListModelList=new ArrayList<>();
        childEmployeeItemModelList=new ArrayList<>();
        designationItemModelList=new ArrayList<>();
        salesReportItemModelList=new ArrayList<>();
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

        GpsProvider.onGPS(this);
        Log.d("dateaa",""+sessionManager.getId());
        Log.d("dateaa",""+fromdateVal);
        Log.d("dateaa",""+todateVal);

        sessionManager=new SessionManager(this);
        String emp_id=sessionManager.getId();

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


        tv_fromdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                from_date_mod=true;
            }
        });
        tv_todate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                from_date_mod=true;
            }
        });
        //getDealerApi();
        getOrderList(emp_id,fromdateVal,todateVal);
        getDesignationList(sessionManager.getKEY_designation());
        lvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from_date_mod || to_date_mod){
                    fromdateVal=tv_fromdate.getText().toString();
                    todateVal=tv_todate.getText().toString();
                    Log.d("fromdate",tv_fromdate.getText().toString());
                    Log.d("todate",tv_todate.getText().toString());
                    getOrderList(emp_id,fromdateVal,todateVal);
                    from_date_mod=false;
                    to_date_mod=false;
                }

            }
        });
        /* stateAdapter = new StateAdapter(this, mStrings);
        sp_dealers.setAdapter(stateAdapter);
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {

                } else {

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
*/
        stateAdapterDEsignation = new StateAdapter(this, designantionList,"Select Designation");
        sp_designation.setAdapter(stateAdapterDEsignation);
        sp_designation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    sp_child_employee.setVisibility(View.GONE);

                } else {

                    sp_child_employee.setVisibility(View.VISIBLE);
                    getChildEmployeeList(designationItemModelList.get(position-1).getId(),"4");

                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        stateAdapterChildEmployeee = new StateAdapter(this, childEmployeeList,"Select Designation of Employee");
        sp_child_employee.setAdapter(stateAdapterChildEmployeee);
        sp_child_employee.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    child_emp_id="";
                    rvSalesReportList.setVisibility(View.GONE);
                    rvCategoriesList.setVisibility(View.VISIBLE);
                } else {

                    child_emp_id=childEmployeeItemModelList.get(position-1).getId();
                    getSalesReportList(child_emp_id);
                    rvSalesReportList.setVisibility(View.VISIBLE);
                    rvCategoriesList.setVisibility(View.GONE);


                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void showDateDialog(final int type) {

        final Dialog dialog = new Dialog(YourHistryActivity.this);
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



    public void getDealerApi() {
        //spinnar
        dealerListItemModels.clear();
        /*loadingView = new LoadingView(this);
        loadingView.showLoadingView();*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                     //   loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                            //    loadingView.hideLoadingView();
                                dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<dealerListItemModels.size();i++)
                                {
                                    mStrings.add(loginResponse.getItems().get(i).getDealerName());
                                }

                            }
                        }
                        else
                        {
                        //    Toast.makeText(YourHistryActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
              /*  errorView = new ErrorView(YourHistryActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        //onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            };
        };

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getOrderList(String emp_id, String fromdateVal, String todateVal) {
       loadingView = new LoadingView(YourHistryActivity.this);
        loadingView.showLoadingView();
        getDelalerListModelList.clear();
        RequestQueue queue = Volley.newRequestQueue(YourHistryActivity.this);
        String url;
        url = getString(R.string.json_my_get_order_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                      loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        GetOrderListModel loginResponse = gson.fromJson(response, GetOrderListModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                loadingView.hideLoadingView();
                                rvCategoriesList.setVisibility(View.VISIBLE);
                                nothingdatatxt.setVisibility(View.GONE);
                                getDelalerListModelList=loginResponse.getItems();
                                Log.d("ff", String.valueOf(getDelalerListModelList.size()));
                                    getOrderListAdapter=new GetOrderListAdapter(YourHistryActivity.this,getDelalerListModelList);

                                    rvCategoriesList.setAdapter(getOrderListAdapter);

                                Log.d("Dixit","aa"+getDelalerListModelList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                // Log.d("Dixit","aa"+loginResponse.getItems().get(0).getProducts().size());
                                //  myScheduleAdapter.notifyDataSetChanged();
                                // listAdapter.notifyDataSetChanged();

                                //  viewAttendanceModelList.addAll(loginResponse.getItems());


                                //    viewAttendanceAdapter.notifyDataSetChanged();

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
                            rvCategoriesList.setVisibility(View.GONE);
                            nothingdatatxt.setVisibility(View.VISIBLE);
                            loadingView.hideLoadingView();
                            getOrderListAdapter=new GetOrderListAdapter(YourHistryActivity.this,getDelalerListModelList);
                            rvCategoriesList.setAdapter(getOrderListAdapter);
                            Log.d("else","fjkaf");
                         //   Toast.makeText(YourHistryActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
               /* errorView = new ErrorView(YourHistryActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id);
                params.put("from", fromdateVal);
                params.put("to", todateVal);
                //  params.put("from_date", fromdate);
                //  params.put("to_date", todate);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getDesignationList(String designation) {
        //spinnar
        designantionList.clear();
       /* loadingView = new LoadingView(this);
        loadingView.showLoadingView();*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_designation);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                      //  loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DesignationModel loginResponse = gson.fromJson(response, DesignationModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                              //  loadingView.hideLoadingView();
                                designationItemModelList=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<designationItemModelList.size();i++)
                                {
                                    designantionList.add(loginResponse.getItems().get(i).getDesignation());
                                }

                            }
                        }
                        else
                        {
                         //   Toast.makeText(YourHistryActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
              /*  errorView = new ErrorView(YourHistryActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        //onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

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
       /* loadingView = new LoadingView(this);
        loadingView.showLoadingView();*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_child_employeee);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                     //   loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        ChildEmployeeModel loginResponse = gson.fromJson(response, ChildEmployeeModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                //loadingView.hideLoadingView();
                                childEmployeeItemModelList=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<childEmployeeItemModelList.size();i++)
                                {
                                    childEmployeeList.add(loginResponse.getItems().get(i).getEmploye());
                                }

                            }
                        }
                        else
                        {
                         ///   Toast.makeText(YourHistryActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                /*errorView = new ErrorView(YourHistryActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        //onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

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

    public void getSalesReportList(String emp_id) {
        /*loadingView = new LoadingView(YourHistryActivity.this);
        loadingView.showLoadingView();*/
        RequestQueue queue = Volley.newRequestQueue(YourHistryActivity.this);
        String url;
        url = getString(R.string.json_child_salesReport);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                       // loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        SalesReportListModel loginResponse = gson.fromJson(response, SalesReportListModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                              //  loadingView.hideLoadingView();
                                salesReportListAdapter=new SalesReportListAdapter(YourHistryActivity.this,salesReportItemModelList);
                                rvSalesReportList.setAdapter(salesReportListAdapter);

                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                // Log.d("Dixit","aa"+loginResponse.getItems().get(0).getProducts().size());
                                //  myScheduleAdapter.notifyDataSetChanged();
                                // listAdapter.notifyDataSetChanged();

                                //  viewAttendanceModelList.addAll(loginResponse.getItems());


                                //    viewAttendanceAdapter.notifyDataSetChanged();

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
                        //    Toast.makeText(YourHistryActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
              /*  errorView = new ErrorView(YourHistryActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();*/

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("child_emp_id", emp_id);
                //  params.put("from_date", fromdate);
                //  params.put("to_date", todate);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }
}
