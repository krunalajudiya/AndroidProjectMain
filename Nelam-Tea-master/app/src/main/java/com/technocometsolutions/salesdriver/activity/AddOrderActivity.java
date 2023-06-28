package com.technocometsolutions.salesdriver.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.AddOrderAdapter;
import com.technocometsolutions.salesdriver.adapter.DealerStockReportAdapter;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;
import com.technocometsolutions.salesdriver.model.CategoryItemModel1;
import com.technocometsolutions.salesdriver.model.CategoryListModel;
import com.technocometsolutions.salesdriver.model.CategoryListModel1;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.DealerStockReportItemModel;
import com.technocometsolutions.salesdriver.model.DealerStockReportModel;
import com.technocometsolutions.salesdriver.model.SuccessModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsLocationReceiver;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class AddOrderActivity extends AppCompatActivity {
    public RecyclerView my_recycler_order,my_recycler;
    public RecyclerView.LayoutManager layoutManager;
    public List<CategoryItemModel1> categoryItemModelList=new ArrayList<>();
    public List<CategoryItemModel1> categoryItemModelList1=new ArrayList<>();
    public AddOrderAdapter mAdapter;
    public DealerStockReportAdapter dealerStockReportAdapter;
    public List<DealerStockReportItemModel> dealerStockReportItemModels;
    public SearchView search;
    public LoadingView loadingView;
    public ErrorView errorView;
    public String dealer_id="";
    public String dealer_id11="";
    public String dealer_idpostion="";
    public String dealer_idnamesplit="";
    public double total_pricenew1 = 0;
    public double total_kgnew1 = 0;
    public double total_bagsnew1 = 0;
    public TextView tv_total_price,tv_kg_weight,tv_total_bags;
    public static AddOrderActivity addOrderActivity;
    public TextView btn_submit;
    public SessionManager sessionManager;
    public StateAdapter stateAdapter;
    public SearchableSpinner sp_dealers,sp_city;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private final ArrayList<String> sStrings = new ArrayList<>();
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public LinearLayout lv_select_dealer;
    public Button btnDealerStockReport;
    public GpsLocationReceiver gpsLocationReceiver;
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        addOrderActivity=this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        my_recycler_order=findViewById(R.id.my_recycler_order);
        my_recycler=findViewById(R.id.my_recycler);
        sessionManager=new SessionManager(this);
        sp_dealers = findViewById(R.id.sp_dealers);
        sp_city = findViewById(R.id.sp_city);
        lv_select_dealer = findViewById(R.id.lv_select_dealer);
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_kg_weight = findViewById(R.id.tv_kg_weight);
        tv_total_bags = findViewById(R.id.tv_total_bags);
        tv_total_price.setText("Total Price \n ₹0 ");
        tv_kg_weight.setText("Weight & KG \n 0 KG");
        tv_total_bags.setText("Total Boxes \n  0");
        lv_select_dealer.setVisibility(View.GONE);
        btn_submit = findViewById(R.id.btn_submit);
        btnDealerStockReport=findViewById(R.id.btnDealerStockReport);
        btn_submit.setClickable(true);

        GpsProvider.onGPS(AddOrderActivity.this);
        //gpsLocationReceiver=new GpsLocationReceiver();
        //registerReceiver(gpsLocationReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


        btnDealerStockReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddOrderActivity.this,StockReportActivity.class);
                startActivity(intent);

            }
        });
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        //  my_recycler_order.setLayoutManager(layoutManager);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        search.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        my_recycler.setLayoutManager(mLayoutManager1);
        my_recycler.setItemAnimator(new DefaultItemAnimator());
        my_recycler_order.setLayoutManager(mLayoutManager);
        my_recycler_order.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));



        getCityApi(sessionManager.getId());
        stateAdaptercity =  new StateAdapter(AddOrderActivity.this, mStrings_city,"Select City");
        sp_city.setAdapter(stateAdaptercity);
        getDealerApi(sessionManager.getId());
        sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position!=0) {
                    Log.d("cxc", mStrings_city.get(position - 1));
                    getDealerApi1(cityItemModelArrayList.get(position - 1).getId());
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });

        //sp_dealers.set
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    dealer_id="";
                    lv_select_dealer.setVisibility(View.GONE);
                }
                else
                {
                        dealer_id11 = null;
                        //dealer_id=dealerListItemModels.get(position).getId();

                        lv_select_dealer.setVisibility(View.VISIBLE);

                        String dealerSplit= stateAdapter.getItem(position).toString();
                        String[] splitDelarId= dealerSplit.split(" % ");
                        String lastFinalId = splitDelarId[splitDelarId.length-1];



                        for (int i=0;i<dealerListItemModels.size();i++)
                        {
                            if (dealerListItemModels.get(i).getId().equals(lastFinalId))
                            {

                                dealer_id11=dealerListItemModels.get(i).getId();

                                Log.d("DelarPostion",""+dealer_id11);
                                break;
                            }
                        }
                    getCategorisApi(dealer_id11);
                    //getDealersStockReportApi(dealer_id11);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryItemModelList1.clear();
                for (int i=0;i<categoryItemModelList.size();i++)
                {


                    for (int j=0; j < categoryItemModelList.get(i).getProducts().size();j++)
                    {
                        //wiat for app is run
                        /*total_pricenew +=Double.parseDouble(""+continentList.get(i).getProducts().get(j).getTotal_price());
                        total_kgnew +=Double.parseDouble(""+continentList.get(i).getProducts().get(j).getTotal_kg());
                        tv_total_price.setText("Total Price \n ₹"+total_pricenew+" ");
                        tv_kg_weight.setText("Weight &amp; KG \n "+total_kgnew+" KG");*/

                        if (categoryItemModelList.get(i).getProducts().get(j).getTotal_qty()==0)
                        {

                        }
                        else
                        {
                            Log.d("response_size",""+categoryItemModelList.size());
                            categoryItemModelList1.add(categoryItemModelList.get(i));
                            Log.d("response_size1",""+categoryItemModelList1.size());
                        }

                    }
                }

                if (tv_total_price.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(AddOrderActivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                }
                if (tv_kg_weight.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(AddOrderActivity.this, "Please Enter Weight", Toast.LENGTH_SHORT).show();
                }
                else if (sessionManager.getId().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(AddOrderActivity.this, "Please Select Id", Toast.LENGTH_SHORT).show();
                }

                else if (dealer_id11.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddOrderActivity.this, "Please Select Distributor", Toast.LENGTH_SHORT).show();
                }
                else if (categoryItemModelList1.size()==0)
                {
                    Toast.makeText(AddOrderActivity.this, "Please Enter Order", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    getDataSubmitOrder(sessionManager.getId(),dealer_id11,tv_total_price.getText().toString(),tv_kg_weight.getText().toString());
                }



            }
        });


    }
    public void getCityApi(String emp_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        // loadingView = new LoadingView(SaveexActivity.this);
        // loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddOrderActivity.this);
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
                                        String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId() ;
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
                                Toast.makeText(AddOrderActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void getDealerApi(String emp_id) {
        //spinnar
        dealerListItemModels.clear();
        loadingView = new LoadingView(AddOrderActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddOrderActivity.this);
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
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);

                                }
                                stateAdapter = new StateAdapter(AddOrderActivity.this, mStrings,"Select Distributor");
                                sp_dealers.setAdapter(stateAdapter);

                                for (int j=1;j<=dealerListItemModels.size();j++)
                                {
                                    String dealerSplit= stateAdapter.getItem(j).toString();
                                    Log.d("DealerPoist",""+dealerSplit);
                                    String[] splitDelarId= dealerSplit.split(" % ");
                                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                                    if (!sessionManager.getDealerId().equals("0")) {
                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
                                            int abc = mStrings.indexOf(dealerSplit);
                                            sp_dealers.setSelectedItem(abc + 1);
                                            Log.d("1DealerPoist", "" + abc);
                                        }
                                    }

                                }




                            }
                        }
                        else
                        {
                            Toast.makeText(AddOrderActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(AddOrderActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    public void getDealerApi1(String city_id) {
        //spinnar
        dealerListItemModels.clear();
        mStrings.clear();
        loadingView = new LoadingView(AddOrderActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddOrderActivity.this);
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
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);

                                }
                                stateAdapter = new StateAdapter(AddOrderActivity.this, mStrings,"Select Distributor");
                                sp_dealers.setAdapter(stateAdapter);

                                for (int j=1;j<=dealerListItemModels.size();j++)
                                {
                                    String dealerSplit= stateAdapter.getItem(j).toString();
                                    Log.d("DealerPoist",""+dealerSplit);
                                    String[] splitDelarId= dealerSplit.split(" % ");
                                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                                    if (!sessionManager.getDealerId().equals("0")) {
                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
                                            int abc = mStrings.indexOf(dealerSplit);
                                            sp_dealers.setSelectedItem(abc + 1);
                                            Log.d("1DealerPoist", "" + abc);
                                        }
                                    }

                                }




                            }
                        }
                        else
                        {
                            Toast.makeText(AddOrderActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(AddOrderActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("city_id",city_id);

                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getCategorisApi(String dealer_id) {
        loadingView = new LoadingView(AddOrderActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddOrderActivity.this);
        String url;
        url = getString(R.string.json_category_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponsecategory", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        CategoryListModel1 loginResponse = gson.fromJson(response, CategoryListModel1.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                categoryItemModelList=loginResponse.getItems();
                                Log.d("Dixit","aa"+categoryItemModelList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                Log.d("Dixit","aa"+loginResponse.getItems().get(0).getProducts().size());
                                mAdapter = new AddOrderAdapter(AddOrderActivity.this, categoryItemModelList);
                                my_recycler_order.setAdapter(mAdapter);

                            }
                        }
                        else
                        {
                            Toast.makeText(AddOrderActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(AddOrderActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
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
                params.put("dealer_id", dealer_id);
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

    public void getDealersStockReportApi(String dealer_id) {
        //    loadingView = new LoadingView(AddOrderActivity.this);
        //   loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddOrderActivity.this);
        String url;
        url = getString(R.string.json_dealers_stock_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        //  loadingView.hideLoadingView();

                        Gson gson = new Gson();
                        DealerStockReportModel loginResponse = gson.fromJson(response, DealerStockReportModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                dealerStockReportItemModels=loginResponse.getItems();
                                Log.d("Dixit","aa"+categoryItemModelList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                //  Log.d("Dixit","aa"+loginResponse.getItems().get(0).getProducts().size());
                                dealerStockReportAdapter = new DealerStockReportAdapter(AddOrderActivity.this, dealerStockReportItemModels);
                                my_recycler.setAdapter(dealerStockReportAdapter);

                            }
                        }
                        else
                        {
                            Toast.makeText(AddOrderActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //         loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                /*errorView = new ErrorView(AddOrderActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("emp_id", sessionManager.getId());
                params.put("dealer_id", dealer_id);
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
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getProgucteed()
    {
        Log.d("getproguctedd", String.valueOf(categoryItemModelList.size()));
        int total_pricenew = 0;
        int total_kgnew = 0;
        int total_bagsnew = 0;

        for (int i=0;i<categoryItemModelList.size();i++)
        {
            for (int j=0; j < categoryItemModelList.get(i).getProducts().size();j++)
            {
//                Log.d("ffsize", String.valueOf(categoryItemModelList.get(i).getProducts().size()));

                //wiat for app is run
                total_pricenew +=Integer.parseInt(""+categoryItemModelList.get(i).getProducts().get(j).getTotal_price());
                total_kgnew +=Integer.parseInt(""+categoryItemModelList.get(i).getProducts().get(j).getTotal_kg());
                total_bagsnew +=Integer.parseInt(""+categoryItemModelList.get(i).getProducts().get(j).getTotal_qty());
                total_pricenew1=total_pricenew;
                total_kgnew1=total_kgnew;
                total_bagsnew1=total_bagsnew;
                //Log.d("ss",String.valueOf(total_bagsnew));
                tv_total_price.setText("Total Price \n ₹"+total_pricenew+" ");
                tv_kg_weight.setText("Weight & KG \n "+total_kgnew+" KG");
                tv_total_bags.setText("Total Boxes \n "+total_bagsnew);
                Log.d("fftotal", String.valueOf(total_bagsnew));
            }
        }
    }


    public static AddOrderActivity getAddOrderActivity()
    {
        return addOrderActivity;
    }

    public void getDataSubmitOrder(String emp_id, String dealer_id, String total_order_price, String total_kg_weight) {

        loadingView = new LoadingView(AddOrderActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(AddOrderActivity.this);
        String url;
        url = getString(R.string.json_place_order);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponseddddddd", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        categoryItemModelList1.clear();
                        Gson gson=new Gson();
                        SuccessModel successModel = gson.fromJson(response, SuccessModel.class);
                        if (successModel.getSuccess())
                        {
                            finish();
                            Toast.makeText(AddOrderActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        else {

                            Toast.makeText(AddOrderActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();

                        }



                        /*Gson gson = new Gson();
                        CategoryListModel loginResponse = gson.fromJson(response, CategoryListModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                continentList=loginResponse.getItems();
                                Log.d("Dixit","aa"+continentList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                Log.d("Dixit","aa"+loginResponse.getItems().get(0).getProducts().size());
                                listAdapter = new MyListAdapter(OrderScNewActivity.this, continentList);
                                myList.setAdapter(listAdapter);
                                // listAdapter.notifyDataSetChanged();
                                //  viewAttendanceModelList.addAll(loginResponse.getItems());
                                //    viewAttendanceAdapter.notifyDataSetChanged();
                                //   attendanceMangementModelList.add((PunchInModel.Item) loginResponse.getItems());
                                //  addAttendanceAdapter.notifyDataSetChanged();
                                //itemList.addAll(loginResponse.getItems());
                                //sessionManager.isLoggedIn(itemList.get(0).getEmpId(),itemList.get(0).getFirstName(),itemList.get(0).getMiddleName(),itemList.get(0).getLastName(),itemList.get(0).getEmpCode(),itemList.get(0).getPassword(),itemList.get(0).getBirthDate(),itemList.get(0).getGender(),itemList.get(0).getBloodGroup(),itemList.get(0).getGenderType(),itemList.get(0).getReportingTo(),itemList.get(0).getDesignation(),itemList.get(0).getJoiningDate(),itemList.get(0).getQualification(),itemList.get(0).getOfficialEmail(),itemList.get(0).getPersonalEmail(),itemList.get(0).getMobileNo());
                            Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                                //dixit
                                // categoriesListAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(OrderScNewActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponseNew: "+error.getMessage());
                errorView = new ErrorView(AddOrderActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
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
                params.put("dealer_id", dealer_id);
                params.put("emp_id", emp_id);
                params.put("total_order_price", ""+total_pricenew1);
                params.put("total_order_weight", ""+total_kgnew1);


                JSONArray jsonArray = new JSONArray();
                for (int j = 0; j < categoryItemModelList1.size(); j++) {
                    Log.d("resposne_size_new",""+categoryItemModelList1.size());
                    for (int i = 0; i < categoryItemModelList1.get(j).getProducts().size(); i++) {
                        if (categoryItemModelList1.get(j).getProducts().get(i).getTotal_kg()==0)
                        {
                        }
                        else
                        {
                            JSONObject obj = null;
                            obj = new JSONObject();
                            try {
                                obj.put("category_id", categoryItemModelList1.get(j).getId());
                                obj.put("product_id", categoryItemModelList1.get(j).getProducts().get(i).getId());
                                obj.put("quantity", categoryItemModelList1.get(j).getProducts().get(i).getTotal_qty());
                                obj.put("original_price", "");
                                obj.put("total_price", categoryItemModelList1.get(j).getProducts().get(i).getTotal_price());
                                obj.put("original_weight", "");
                                obj.put("total_weight", categoryItemModelList1.get(j).getProducts().get(i).getTotal_kg());
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(obj);
                        }
                    }
                }
                for (int j = 0; j < categoryItemModelList.size(); j++) {

                    for (int i = 0; i < categoryItemModelList.get(j).getProducts().size(); i++) {
                        if (categoryItemModelList.get(j).getProducts().get(i).getTotal_qty()==0)
                        {

                        }
                        else
                        {
                            JSONObject obj = null;
                            obj = new JSONObject();
                            try {
                                obj.put("category_id", categoryItemModelList.get(j).getId());
                                obj.put("product_id", categoryItemModelList.get(j).getProducts().get(i).getId());
                                obj.put("quantity", categoryItemModelList.get(j).getProducts().get(i).getTotal_qty());
                                obj.put("original_price", "");
                                obj.put("total_price", categoryItemModelList.get(j).getProducts().get(i).getTotal_price());
                                obj.put("original_weight", "");
                                obj.put("total_weight", categoryItemModelList.get(j).getProducts().get(i).getTotal_kg());

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(obj);
                        }

                    }
                }


                Log.d("dixitttabdobariya","aaaNew "+jsonArray.toString());

                params.put("products", jsonArray.toString());
                Log.d("dixitttaproducts"," "+params.toString());



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
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(AddOrderActivity.this);
        //registerReceiver(gpsLocationReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(gpsLocationReceiver);
    }
}