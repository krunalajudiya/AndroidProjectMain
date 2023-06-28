package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stepstone.stepper.StepperLayout;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.adapter.StepAdapter;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class AddNewDealer extends AppCompatActivity{


    StepperLayout stepperLayout;
    RadioGroup radioGroup;
    RadioButton dealer,distributer;
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    private final ArrayList<String> mStrings = new ArrayList<>();
    //    public StateAdapter stateAdapter;
    ArrayAdapter dealeradapter;
    EditText firmname,gstno,panno,bank;
    public SearchableSpinner city,dealerspinner;
    RadioGroup firmtype;
    RadioButton proprietorship,partnership;
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;
    EditText contactperson,phonenumber,address,noofemployes;
    Spinner opratingsince;
    int sincepos;
    List<String> arrayListsince;
    Calendar calendar;
    Button submit;
    RadioGroup dealerordistributer;
    ArrayAdapter<String> since;
    public LoadingView loadingView;
    public ErrorView errorView;
    public StateAdapter stateAdapter;
    public SessionManager sessionManager;


    String dealer_str,dealerdistributerid_str="",firmname_str,city_str,firmtype_str,opratingsince_str,gstno_str,panno_str,bank_str,contactpersonname_str,mobilenumber_str,address_str,noofemployes_str;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_dealer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepperLayout=findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(new StepAdapter(getSupportFragmentManager(),this));
        radioGroup=findViewById(R.id.dealerordistributer);
        dealerspinner=findViewById(R.id.dealerspinner);
        dealer=findViewById(R.id.dealer);
        distributer=findViewById(R.id.distributer);
        firmname=findViewById(R.id.firmname);
        gstno=findViewById(R.id.gstno);
        panno=findViewById(R.id.panno);
        bank=findViewById(R.id.bank);
        city=findViewById(R.id.d_city);
        firmtype=findViewById(R.id.firmtyperadiogroup);
        contactperson=findViewById(R.id.contactperson);
        phonenumber=findViewById(R.id.phonenumber);
        address=findViewById(R.id.address);
        submit=findViewById(R.id.submit);
        opratingsince=findViewById(R.id.opratingsince);
        dealerordistributer=findViewById(R.id.dealerordistributer);
        noofemployes=findViewById(R.id.noofemployees);
        sessionManager=new SessionManager(this);



        arrayListsince=new ArrayList();
        calendar=Calendar.getInstance();
        sinceyear();
        since=new ArrayAdapter(AddNewDealer.this, android.R.layout.simple_list_item_1,arrayListsince);
        opratingsince.setAdapter(since);
        opratingsince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opratingsince_str=since.getItem(position);
                Log.d("since",opratingsince_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        firmtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.partnership){
                    firmtype_str="partnership";
                }
                if (checkedId==R.id.proprietorship){
                    firmtype_str="propritrorship";
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.dealer){
                    dealerspinner.setVisibility(View.VISIBLE);
                    dealer_str="1";
                }
                if (checkedId==R.id.distributer){
                    dealerspinner.setVisibility(View.GONE);
                    dealer_str="0";
                }
            }
        });

        getDealerApi(sessionManager.getId());
        dealerspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                dealerdistributerid_str=dealerListItemModels.get(position-1).getId();
                Log.d("dealerdistributerid",dealerdistributerid_str);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        dealerspinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        getCityApi(sessionManager.getId());
        stateAdaptercity =  new StateAdapter(AddNewDealer.this, mStrings_city,"Select City");
        city.setAdapter(stateAdaptercity);
        city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                city_str=mStrings_city.get(position-1);


                // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        city.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
                submitdata(firmname_str,city_str,firmtype_str,gstno_str,panno_str,bank_str,dealer_str,dealerdistributerid_str,contactpersonname_str,mobilenumber_str,address_str,noofemployes_str,opratingsince_str);

            }
        });


    }
    public void getdata(){

//        if (delarordistributer.getCheckedRadioButtonId()==-1 && firmname.getText().toString().trim().length()>0 && )
        firmname_str=firmname.getText().toString().trim();
        gstno_str=gstno.getText().toString().trim();
        panno_str=panno.getText().toString().trim();
        bank_str=bank.getText().toString().trim();
        contactpersonname_str=contactperson.getText().toString().trim();
        mobilenumber_str=phonenumber.getText().toString().trim();
        address_str=address.getText().toString().trim();
        noofemployes_str=noofemployes.getText().toString().trim();

    }
    public void sinceyear(){
        for (int i = 1900; i<=calendar.get(Calendar.YEAR) ; i++){
            arrayListsince.add(String.valueOf(i));
        }
    }

    public void getDealerApi(String emp_id) {
        //spinnar
        dealerListItemModels.clear();
        loadingView = new LoadingView(AddNewDealer.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddNewDealer.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);

                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerListItemModels = loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: " + dealerListItemModels.size());
                                for (int i = 0; i < dealerListItemModels.size(); i++) {

                                    String data = loginResponse.getItems().get(i).getDealerName();
//                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
//                                    mStrings.add(data + " " + data1);
                                    mStrings.add(data);

                                }
                                stateAdapter = new StateAdapter(AddNewDealer.this, mStrings,"Select Distributor");
                                dealerspinner.setAdapter(stateAdapter);

                                for (int j=1;j<=dealerListItemModels.size();j++)
                                {
                                    String dealerSplit= stateAdapter.getItem(j).toString();
                                    Log.d("DealerPoist",""+dealerSplit);
                                    String[] splitDelarId= dealerSplit.split(" % ");
                                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                                    if (!sessionManager.getDealerId().equals("0")) {
                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
                                            int abc = mStrings.indexOf(dealerSplit);
                                            dealerspinner.setSelectedItem(abc + 1);
                                            Log.d("1DealerPoist", "" + abc);
                                        }
                                    }

                                }




                            }
                        }
                        else
                        {
                            Toast.makeText(AddNewDealer.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(AddNewDealer.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("emp_id",emp_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getCityApi(String emp_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        // loadingView = new LoadingView(SaveexActivity.this);
        // loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddNewDealer.this);
        String url;
        url = getString(R.string.json_all_city);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
//                        loadingView.hideLoadingView();

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
//                                        String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId() ;
//                                        mStrings_city.add(data + " " + data1);
                                        mStrings_city.add(data);

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
                                Toast.makeText(AddNewDealer.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                /*errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        // getDataPunchOut(sessionManager.getId());
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
                //params.put("state_id", state_id);
                //params.put("password", password);
                params.put("emp_id",emp_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    void submitdata(String firmname,String city,String type,String gstno,String panno,String bank,String typeofdealer,String dealerdistributerid,String personname,String mobile,String address,String noofemployes,String operatingsince)
    {

        loadingView = new LoadingView(AddNewDealer.this);
        loadingView.showLoadingView();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        try {
            params.put("firm_name",firmname);
            params.put("city", city);
            params.put("type", type);
            params.put("gst_no", gstno);
            params.put("pan_no", panno);
            params.put("bank",bank);
            params.put("type_of_dealer",typeofdealer);
            params.put("dealer_distributor_id",dealerdistributerid);
            params.put("contact_person", personname);
            params.put("c_mobile", mobile);
            params.put("c_address", address);
            params.put("n_employee", noofemployes);
            params.put("op_date", operatingsince);


            Log.d("id"," "+dealerdistributerid);

//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        String url;
        url = getString(R.string.json_add_dealer);
        client.post(""+url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                //  Log.d("respons",""+response.toString());
                Log.d("dd", "onResponse: "+response);
                loadingView.hideLoadingView();
                finish();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("error","dixit: "+res);

                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+res);
                errorView = new ErrorView(AddNewDealer.this, new ErrorView.OnNoInternetConnectionListerner() {
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
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}