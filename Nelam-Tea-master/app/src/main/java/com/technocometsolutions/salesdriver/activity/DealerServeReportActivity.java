package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.CustomAdapter;
import com.technocometsolutions.salesdriver.adapter.SimpleArrayListAdapter;
import com.technocometsolutions.salesdriver.adapter.SimpleListAdapter;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.StateItemModel;
import com.technocometsolutions.salesdriver.model.StateModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class DealerServeReportActivity extends AppCompatActivity {
    public SearchableSpinner sp_state,sp_city;
    public LoadingView loadingView;
    public ErrorView errorView;
    public StateAdapter stateAdapter;
    public StateAdapter stateAdaptercity;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public ArrayList<StateItemModel> stateItemModelArrayList = new ArrayList<>();
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    public String city_id="",state_id="",party_s_name="",con_person_mobile="",main_agency="",area_covered="",godown="",vehicle="",reply_with_detail="",remarks_if_any="";
    public EditText et_party_s_name,et_con_person_mobile,et_main_agency,et_area_covered,et_godown,et_vehicle,et_reply_with_detail,et_remarks_if_any;
    public Button btn_save;
    public SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_serve_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager=new SessionManager(this);
        getStateApi();
        GpsProvider.onGPS(this);
        btn_save = findViewById(R.id.btn_save);
        et_party_s_name = findViewById(R.id.et_party_s_name);
        et_con_person_mobile = findViewById(R.id.et_con_person_mobile);
        et_main_agency = findViewById(R.id.et_main_agency);
        et_area_covered = findViewById(R.id.et_area_covered);
        et_godown = findViewById(R.id.et_godown);
        et_vehicle = findViewById(R.id.et_vehicle);
        et_reply_with_detail = findViewById(R.id.et_reply_with_detail);
        et_remarks_if_any = findViewById(R.id.et_remarks_if_any);
        sp_state = findViewById(R.id.sp_state);
        sp_city = findViewById(R.id.sp_city);
        sp_city.setVisibility(View.GONE);
        stateAdapter = new StateAdapter(DealerServeReportActivity.this, mStrings,"Select State");
        sp_state.setAdapter(stateAdapter);
        sp_state.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {

                //sp_city.setVisibility(View.VISIBLE);
                Log.d("ff","state");

                if (position==0)
                {
                    city_id="";
                    state_id="";
                    sp_city.setVisibility(View.GONE);
                   // Toast.makeText(DealerServeReportActivity.this, ""+stateItemModelArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
                /*else if (position==stateItemModelArrayList.size())
                {
                    Toast.makeText(DealerServeReportActivity.this, ""+stateItemModelArrayList.get(position-1).getId(), Toast.LENGTH_SHORT).show();
                }*/
                else
                {
                    sp_city.setVisibility(View.VISIBLE);
                    String dealerSplit=stateAdapter.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];

                    for (int i=0;i<stateItemModelArrayList.size();i++)
                    {
                        if (stateItemModelArrayList.get(i).getId().equals(splitDelarId))
                        {
                            state_id=stateItemModelArrayList.get(i).getId();
                            break;
                        }
                    }

                     getCityApi(state_id);
                     stateAdaptercity =  new StateAdapter(DealerServeReportActivity.this, mStrings_city,"Select City");
                     sp_city.setAdapter(stateAdaptercity);
                    //Toast.makeText(DealerServeReportActivity.this, ""+stateItemModelArrayList.get(position-1).getId(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected() {
                sp_city.setVisibility(View.GONE);
            }
        });
        sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                Log.d("ff","city");
                    if (position==0)
                    {
                        city_id="";
                    }
                    else
                    {
                        //city_id=cityItemModelArrayList.get(position-1).getId();
                        String dealerSplit= stateAdaptercity.getItem(position).toString();
                        String[] lastFinalId= dealerSplit.split(" % ");
                        String splitDelarId = lastFinalId[lastFinalId.length-1];

                        for (int i=0;i<cityItemModelArrayList.size();i++)
                        {
                            if (cityItemModelArrayList.get(i).getId().equals(splitDelarId))
                            {
                                city_id=cityItemModelArrayList.get(i).getId();
                                break;
                            }
                        }
                    }

               // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        sp_state.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        sp_city.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                party_s_name=et_party_s_name.getText().toString();
                con_person_mobile=et_con_person_mobile.getText().toString();
                main_agency=et_main_agency.getText().toString();
                area_covered=et_area_covered.getText().toString();
                godown=et_godown.getText().toString();
                vehicle=et_vehicle.getText().toString();
                reply_with_detail=et_reply_with_detail.getText().toString();
                remarks_if_any=et_remarks_if_any.getText().toString();





                if (city_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(DealerServeReportActivity.this, "Please Select State and city", Toast.LENGTH_SHORT).show();
                }
                else if (party_s_name.equalsIgnoreCase(""))
                {
                    Toast.makeText(DealerServeReportActivity.this, "Please Enter PARTY\\'S NAME", Toast.LENGTH_SHORT).show();
                }
                else if (con_person_mobile.equalsIgnoreCase(""))
                {

                    Toast.makeText(DealerServeReportActivity.this, "Please Enter CON.PERSON/ MOBILE", Toast.LENGTH_SHORT).show();
                }
                else if (main_agency.equalsIgnoreCase(""))
                {
                    Toast.makeText(DealerServeReportActivity.this, "Please Enter MAIN AGENCY", Toast.LENGTH_SHORT).show();

                }
                else if (area_covered.equalsIgnoreCase(""))
                {

                    Toast.makeText(DealerServeReportActivity.this, "Please Enter AREA COVERED", Toast.LENGTH_SHORT).show();
                }

                else if (reply_with_detail.equalsIgnoreCase(""))
                {
                    Toast.makeText(DealerServeReportActivity.this, "Please Enter REPLY - WITH DETAIL", Toast.LENGTH_SHORT).show();

                }
                else if (remarks_if_any.equalsIgnoreCase(""))
                {
                    Toast.makeText(DealerServeReportActivity.this, "Please Enter REMARKS", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    getDealerApi(sessionManager.getId(),city_id,party_s_name,con_person_mobile,main_agency,area_covered,godown,vehicle,reply_with_detail,remarks_if_any);
                }
            }
        });

    }

    public void getDealerApi(String emp_id, String city_id, String party_s_name, String con_person_mobile, String main_agency, String area_covered, String godown, String vehicle, String reply_with_detail, String remarks_if_any) {
        loadingView = new LoadingView(DealerServeReportActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(DealerServeReportActivity.this);
        String url;
        url = getString(R.string.json_dealer_servey);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd_json_dealer_servey", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        finish();
                        /*try {
                            JSONArray jsonElements=response.getJSONArray("items");
                            for (int i=0;i<jsonElements.length();i++)
                            {
                                //baki
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        // JsonObject jsonObject=r
                        /*Gson gson = new Gson();
                        CityModel loginResponse = gson.fromJson(response, CityModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                cityItemModelArrayList = loginResponse.getItems();
                                for (int i=0;i<cityItemModelArrayList.size();i++)
                                {
                                    mStrings_city.add(loginResponse.getItems().get(i).getName());
                                }

                               *//* dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());

                                mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
                                CustomAdapter customAdapter=new CustomAdapter(OrderActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);*//*


                            }
                        }
                        else
                        {
                            Toast.makeText(DealerServeReportActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(DealerServeReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id);
                params.put("dealer_name", party_s_name);
                params.put("mobile_no", con_person_mobile);
                params.put("agency", main_agency);
                params.put("area", area_covered);
                params.put("godown", godown);
                params.put("vehicle", vehicle);
                params.put("detail", reply_with_detail);
                params.put("remarks", remarks_if_any);
                params.put("city_id", city_id);

                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getCityApi(String state_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        loadingView = new LoadingView(DealerServeReportActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(DealerServeReportActivity.this);
        String url;
        url = getString(R.string.json_city);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd1", "onResponse: "+response);
                        loadingView.hideLoadingView();

                        /*try {
                            JSONArray jsonElements=response.getJSONArray("items");
                            for (int i=0;i<jsonElements.length();i++)
                            {
                                //baki
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        // JsonObject jsonObject=r
                        try {
                            Gson gson = new Gson();
                            CityModel loginResponse = gson.fromJson(response, CityModel.class);
                            if (loginResponse.getSuccess())
                            {
                                if (loginResponse.getItems() != null) {
                                    cityItemModelArrayList = loginResponse.getItems();
                                    for (int i=0;i<cityItemModelArrayList.size();i++)
                                    {
                                        String data = loginResponse.getItems().get(i).getName();
                                        String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                        mStrings_city.add(data + " " + data1);

//                                    mStrings_city.add(loginResponse.getItems().get(i).getName());
                                    }

                               /* dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());

                                mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
                                CustomAdapter customAdapter=new CustomAdapter(OrderActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);*/


                                }
                            }
                            else
                            {
                                Toast.makeText(DealerServeReportActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {

                        }

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("cityapi", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(DealerServeReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                 params.put("state_id", state_id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getStateApi() {
        stateItemModelArrayList.clear();
        mStrings.clear();
        loadingView = new LoadingView(DealerServeReportActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(DealerServeReportActivity.this);
        String url;
        url = getString(R.string.json_state);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("stateapi", "onResponse: "+response);
                          loadingView.hideLoadingView();

                        /*try {
                            JSONArray jsonElements=response.getJSONArray("items");
                            for (int i=0;i<jsonElements.length();i++)
                            {
                                //baki
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        // JsonObject jsonObject=r
                        Gson gson = new Gson();
                        StateModel loginResponse = gson.fromJson(response, StateModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                stateItemModelArrayList = loginResponse.getItems();
                                for (int i=0;i<stateItemModelArrayList.size();i++)
                                {
                                    String data = loginResponse.getItems().get(i).getName();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);
                                    //mStrings.add(loginResponse.getItems().get(i).getName());

                                }

                               /* dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());

                                mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
                                CustomAdapter customAdapter=new CustomAdapter(OrderActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);*/


                            }
                        }
                        else
                        {
                            Toast.makeText(DealerServeReportActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(DealerServeReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("emp_id", id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }
}
