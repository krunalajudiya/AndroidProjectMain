package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.technocometsolutions.salesdriver.adapter.AddOrderAdapter;
import com.technocometsolutions.salesdriver.adapter.RetailerOrderAdapter;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;
import com.technocometsolutions.salesdriver.model.CategoryItemModel1;
import com.technocometsolutions.salesdriver.model.CategoryListModel;
import com.technocometsolutions.salesdriver.model.CategoryListModel1;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.RetailerItemModel;
import com.technocometsolutions.salesdriver.model.RetailerModel;
import com.technocometsolutions.salesdriver.model.RouteItemModel;
import com.technocometsolutions.salesdriver.model.RouteModel;
import com.technocometsolutions.salesdriver.model.SuccessModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class RetailerActivity extends AppCompatActivity {

    public RecyclerView my_recycler_order;
    public RecyclerView.LayoutManager layoutManager;
    public List<CategoryItemModel1> categoryItemModelList=new ArrayList<>();
    public List<CategoryItemModel1> categoryItemModelList1=new ArrayList<>();
    public SearchView search;
    public RetailerOrderAdapter mAdapter;
    public LoadingView loadingView;
    public ErrorView errorView;
    public String dealer_id="",d_dealer_id="";
    public String retailer_id="";
    public double total_pricenew1 = 0;
    public double total_kgnew1 = 0;
    public double total_bagsnew1 = 0;
    public TextView tv_total_price,tv_kg_weight,tv_total_bags;
    public static RetailerActivity retailerActivity;
    public TextView btn_submit,btn_npc;
    public SessionManager sessionManager;
    public StateAdapter stateAdapter;
    public StateAdapter routestateAdapter;
    public StateAdapter retailerstateAdapter;
    public SearchableSpinner sp_dealers;
    public SearchableSpinner spRoute;
    public SearchableSpinner spRetailer,sp_city;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private final ArrayList<String> routeStringList = new ArrayList<>();
    private final ArrayList<String> retailerList = new ArrayList<>();
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    private ArrayList<DealerListItemModel> d_dealerListItemModels = new ArrayList<>();
    private ArrayList<RouteItemModel> routeItemModelArrayList = new ArrayList<>();
    private ArrayList<RetailerItemModel> retailerItemModelArrayList= new ArrayList<>();
    public LinearLayout lv_select_dealer;
    public String route_id;
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retailerActivity=this;
        GpsProvider.onGPS(this);

        my_recycler_order=findViewById(R.id.my_recycler_order);
        sessionManager=new SessionManager(this);
        sp_city = findViewById(R.id.sp_city);
        sp_dealers = findViewById(R.id.sp_dealers);
        spRoute= findViewById(R.id.sp_route);
        spRetailer= findViewById(R.id.sp_retailer);
        lv_select_dealer = findViewById(R.id.lv_select_dealer);
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_kg_weight = findViewById(R.id.tv_kg_weight);
        tv_total_bags = findViewById(R.id.tv_total_bags);
        tv_total_price.setText("Total Price \n ₹0 ");
        tv_kg_weight.setText("Weight & KG \n 0 KG");
        tv_total_bags.setText("Total Boxes\n0");
        lv_select_dealer.setVisibility(View.GONE);
        btn_submit = findViewById(R.id.btn_submit);
        btn_npc = findViewById(R.id.btn_npc);
        getCityApi(sessionManager.getId());
        stateAdaptercity =  new StateAdapter(RetailerActivity.this, mStrings_city,"Select City");
        sp_city.setAdapter(stateAdaptercity);
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
        sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position!=0) {
                    Log.d("cxc", mStrings_city.get(position - 1));
                    getDealerApi1(cityItemModelArrayList.get(position - 1).getId());
                    get_d_DealerApi1(cityItemModelArrayList.get(position - 1).getId());
                    Log.d("city_id",cityItemModelArrayList.get(position-1).getId());
                    //getDealerApi("3551");
                    //get_d_DealerApi("3551");
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });

        Log.d("ffff","log");
        //spRoute.setVisibility(View.VISIBLE);
        sp_dealers.setVisibility(View.VISIBLE);
        //dealer_id= getIntent().getStringExtra("dealer_id");
        //getRouteList(sessionManager.getId(),dealer_id);
        getDealerApi(sessionManager.getId());
        get_d_DealerApi(sessionManager.getId());


            sp_dealers.setVisibility(View.VISIBLE);
            lv_select_dealer.setVisibility(View.VISIBLE);

            spRetailer.setVisibility(View.VISIBLE);
            //getRouteList(sessionManager.getId(),dealer_id);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        my_recycler_order.setLayoutManager(mLayoutManager);
        my_recycler_order.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));



        stateAdapter = new StateAdapter(RetailerActivity.this, mStrings,"Select Dealers");
        sp_dealers.setAdapter(stateAdapter);
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    dealer_id="";
                    //lv_select_dealer.setVisibility(View.GONE);
                    //spRetailer.setVisibility(View.GONE);
                    spRoute.setVisibility(View.GONE);
                }
                else
                {
                    //dealer_id=dealerListItemModels.get(position-1).getId();
                    //lv_select_dealer.setVisibility(View.VISIBLE);
                    //getCategorisApi(""+dealer_id);
                    String dealerSplit= stateAdapter.getItem(position).toString();
                    String[] splitDelarId= dealerSplit.split(" % ");
                    String lastFinalId = splitDelarId[splitDelarId.length-1];


                    for (int i=0;i<dealerListItemModels.size();i++)
                    {
                        if (dealerListItemModels.get(i).getId().equals(lastFinalId))
                        {
                            dealer_id=dealerListItemModels.get(i).getId();
                            break;
                        }
                    }
                    //spRoute.setVisibility(View.VISIBLE);
                    spRetailer.setVisibility(View.VISIBLE);
                    //spRetailer.setVisibility(View.VISIBLE);
                    //getRouteList(sessionManager.getId(),dealer_id);
                    //get_d_DealerApi(dealer_id);
                    //getRetailerList(sessionManager.getId(),dealer_id);


                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        routestateAdapter = new StateAdapter(RetailerActivity.this, routeStringList,"Select Route");
        spRoute.setAdapter(routestateAdapter);
        spRoute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    route_id="";
                    spRetailer.setVisibility(View.GONE);
                    //lv_select_dealer.setVisibility(View.GONE);
                }
                else
                {
                    spRetailer.setVisibility(View.VISIBLE);
                    //route_id=routeItemModelArrayList.get(position-1).getId();
                    String dealerSplit= routestateAdapter.getItem(position).toString();
                    String[] splitDelarId= dealerSplit.split(" % ");
                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                    for (int i=0;i<routeItemModelArrayList.size();i++)
                    {
                        if (routeItemModelArrayList.get(i).getId().equals(lastFinalId))
                        {
                            route_id=routeItemModelArrayList.get(i).getId();
                            break;
                        }
                    }
                    Log.d("RouteID",""+route_id +" Delaer14:"+ sessionManager.getId()+" Delaer:"+ dealer_id);
                    //lv_select_dealer.setVisibility(View.VISIBLE);
                    getRetailerList(sessionManager.getId(),dealer_id,route_id);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        retailerstateAdapter = new StateAdapter(RetailerActivity.this, retailerList,"Select Dealer");
        spRetailer.setAdapter(retailerstateAdapter);
        spRetailer.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    //dealer_id="";
                    //lv_select_dealer.setVisibility(View.GONE);
                    retailer_id="";
                    lv_select_dealer.setVisibility(View.GONE);
                }
                else
                {
                    //dealer_id=dealerListItemModels.get(position-1).getId();
                    //lv_select_dealer.setVisibility(View.VISIBLE);
                    //getCategorisApi(""+dealer_id);

                    //retailer_id=retailerItemModelArrayList.get(position-1).getId();
                    lv_select_dealer.setVisibility(View.VISIBLE);
                    d_dealer_id=d_dealerListItemModels.get(position-1).getId();
                    /*String dealerSplit= retailerstateAdapter.getItem(position).toString();
                    String[] splitDelarId= dealerSplit.split(" % ");
                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                    for (int i=0;i<retailerItemModelArrayList.size();i++)
                    {
                        if (retailerItemModelArrayList.get(i).getId().equals(lastFinalId))
                        {
                            retailer_id=retailerItemModelArrayList.get(i).getId();
                            break;
                        }
                    }*/
                    Log.d("RetailerID",""+retailer_id);
                    getCategorisApi(""+d_dealer_id);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if ()
                {

                }
                else
                {

                }*/

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
                            categoryItemModelList1.add(categoryItemModelList.get(i));
                        }

                    }
                }

                if (tv_total_price.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                }
                if (tv_kg_weight.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Enter Weight", Toast.LENGTH_SHORT).show();
                }
                else if (sessionManager.getId().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Select Id", Toast.LENGTH_SHORT).show();
                }

                else if (dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Select distributor", Toast.LENGTH_SHORT).show();
                }
                else if (d_dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this,"Please Select Dealer",Toast.LENGTH_LONG).show();
                }
                else if (categoryItemModelList1.size()==0)
                {
                    Toast.makeText(RetailerActivity.this,"Please Enter Product",Toast.LENGTH_LONG).show();
                }
                else
                {
                    getDataSubmitOrder(sessionManager.getId(),d_dealer_id,tv_total_price.getText().toString(),tv_kg_weight.getText().toString(),retailer_id);
                }



            }
        });
        btn_npc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if ()
                {

                }
                else
                {

                }*/

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

                        if (categoryItemModelList.get(i).getProducts().get(j).getTotal_kg()==0)
                        {

                        }
                        else
                        {
                            categoryItemModelList1.add(categoryItemModelList.get(i));
                        }

                    }
                }

                if (tv_total_price.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                }
                if (tv_kg_weight.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Enter Weight", Toast.LENGTH_SHORT).show();
                }
                else if (sessionManager.getId().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Select Id", Toast.LENGTH_SHORT).show();
                }

                else if (dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this, "Please Select Distributor", Toast.LENGTH_SHORT).show();
                }
                else if (d_dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(RetailerActivity.this,"Please Select Dealer",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (categoryItemModelList1.size()==0) {
                        getDataNpcSubmitOrder(sessionManager.getId(), dealer_id, route_id, retailer_id);
                    }
                    else {
                        Toast.makeText(RetailerActivity.this, "You have already order entered please submit", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });




    }
    public void getCityApi(String emp_id) {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        // loadingView = new LoadingView(SaveexActivity.this);
        // loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
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
                                Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
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

                                stateAdapter = new StateAdapter(RetailerActivity.this, mStrings,"Select Distributor");
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
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        //loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);

                        if (loginResponse.getSuccess())
                        {
                            //get_d_DealerApi("3551");
                            if (loginResponse.getItems() != null) {
                                dealerListItemModels = loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: " + dealerListItemModels.size());
                                for (int i = 0; i < dealerListItemModels.size(); i++) {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);

                                }
                                stateAdapter = new StateAdapter(RetailerActivity.this, mStrings,"Select Distributor");
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
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
//                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
//                    @Override
//                    public void onRetryButtonClicked() {
//                        // getDataPunchOut(sessionManager.getId());
//                    }
//
//                    @Override
//                    public void onCancelButtonClicked() {
//                        onBackPressed();
//                    }
//                });
//                errorView.showLoadingView();
                //get_d_DealerApi("3551");

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
    public void get_d_DealerApi(String emp_id){
        d_dealerListItemModels.clear();
        retailerList.clear();
//        loadingView = new LoadingView(RetailerActivity.this);
//        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_d_dealer_list);
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
                                d_dealerListItemModels = loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: " + d_dealerListItemModels.size());
                                for (int i = 0; i < d_dealerListItemModels.size(); i++) {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    retailerList.add(data + " " + data1);

                                }
                                retailerstateAdapter = new StateAdapter(RetailerActivity.this, retailerList,"Select Dealers");
                                spRetailer.setAdapter(retailerstateAdapter);

                                for (int j=1;j<=d_dealerListItemModels.size();j++)
                                {
                                    String dealerSplit= retailerstateAdapter.getItem(j).toString();
                                    Log.d("DealerPoist",""+dealerSplit);
                                    String[] splitDelarId= dealerSplit.split(" % ");
                                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                                    if (!sessionManager.getDealerId().equals("0")) {
                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
                                            int abc = mStrings.indexOf(dealerSplit);
                                            spRetailer.setSelectedItem(abc + 1);
                                            Log.d("1DealerPoist", "" + abc);
                                        }
                                    }

                                }




                            }
                        }
                        else
                        {
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    public void get_d_DealerApi1(String city_id) {
        //spinnar
        d_dealerListItemModels.clear();
        retailerList.clear();
//        loadingView = new LoadingView(RetailerActivity.this);
//        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_d_dealer_list);
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
                                d_dealerListItemModels = loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: " + d_dealerListItemModels.size());
                                for (int i = 0; i < d_dealerListItemModels.size(); i++) {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    retailerList.add(data + " " + data1);

                                }
                                retailerstateAdapter = new StateAdapter(RetailerActivity.this, retailerList,"Select Dealers");
                                spRetailer.setAdapter(retailerstateAdapter);

                                for (int j=1;j<=d_dealerListItemModels.size();j++)
                                {
                                    String dealerSplit= retailerstateAdapter.getItem(j).toString();
                                    Log.d("DealerPoist",""+dealerSplit);
                                    String[] splitDelarId= dealerSplit.split(" % ");
                                    String lastFinalId = splitDelarId[splitDelarId.length-1];

                                    if (!sessionManager.getDealerId().equals("0")) {
                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
                                            int abc = mStrings.indexOf(dealerSplit);
                                            spRetailer.setSelectedItem(abc + 1);
                                            Log.d("1DealerPoist", "" + abc);
                                        }
                                    }

                                }




                            }
                        }
                        else
                        {
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    public void getRouteList(String emp_id,String dealer_id) {
        //spinnar
        routeItemModelArrayList.clear();
        routeStringList.clear();
        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_route_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd1", "onResponse: "+response);
                        loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        RouteModel loginResponse = gson.fromJson(response, RouteModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                routeItemModelArrayList=loginResponse.getItems();
                                Log.d("dixittttt1", "onCreate: "+routeItemModelArrayList.size());
                                for (int i=0;i<routeItemModelArrayList.size();i++)
                                {
                                    String data = loginResponse.getItems().get(i).getRoute();
                                    String data1=  "<font color=#11ffffff> % " + loginResponse.getItems().get(i).getId();
                                    routeStringList.add(data + " " + data1);
                                    //routeStringList.add(loginResponse.getItems().get(i).getRoute());
                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("dealer_id",dealer_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    public void getRetailerList(String emp_id,String dealer_id,String route_id1) {
        //spinnar
        retailerItemModelArrayList.clear();
        retailerList.clear();
        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_retailer_list_new);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                        loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        RetailerModel loginResponse = gson.fromJson(response, RetailerModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                retailerItemModelArrayList=loginResponse.getItems();
                                Log.d("dixittttt2", "onCreate: "+retailerItemModelArrayList.size());
                                for (int i=0;i<retailerItemModelArrayList.size();i++)
                                {
                                    String data = loginResponse.getItems().get(i).getRetailer_name();
                                    String data1=  "<font color=#ffffff> % " + loginResponse.getItems().get(i).getId();
                                    retailerList.add(data + " " + data1);
                                    //retailerList.add(loginResponse.getItems().get(i).getRetailer_name());
                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("dealer_id",dealer_id);
                params.put("route_id",route_id1);


                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void getCategorisApi(String dealer_id) {
        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_category_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        CategoryListModel1 loginResponse = gson.fromJson(response, CategoryListModel1.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                categoryItemModelList=loginResponse.getItems();
                                Log.d("Dixit1","aa"+categoryItemModelList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                Log.d("Dixit","aa"+loginResponse.getItems().get(0).getProducts().size());
                                mAdapter = new RetailerOrderAdapter(RetailerActivity.this, categoryItemModelList);
                                my_recycler_order.setAdapter(mAdapter);

                            }
                        }
                        else
                        {
                            Toast.makeText(RetailerActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getProgucteed()
    {
        int total_pricenew = 0;
        int total_kgnew = 0;
        int total_bagsnew = 0;
        for (int i=0;i<categoryItemModelList.size();i++)
        {


            for (int j=0; j < categoryItemModelList.get(i).getProducts().size();j++)
            {
                //wiat for app is run
                total_pricenew +=Integer.parseInt(""+categoryItemModelList.get(i).getProducts().get(j).getTotal_price());
                total_kgnew +=Integer.parseInt(""+categoryItemModelList.get(i).getProducts().get(j).getTotal_kg());
                total_bagsnew +=Integer.parseInt(""+categoryItemModelList.get(i).getProducts().get(j).getTotal_qty());
                total_pricenew1=total_pricenew;
                total_kgnew1=total_kgnew;
                total_bagsnew1=total_bagsnew;
                Log.d("totl", String.valueOf(total_bagsnew));
                tv_total_price.setText("Total Price ₹"+total_pricenew+" ");
                tv_kg_weight.setText("Weight & KG "+total_kgnew+" KG");
                tv_total_bags.setText("Total Boxes\n"+total_bagsnew);
            }
        }
    }

    public static RetailerActivity getRetailerActivity()
    {
        return retailerActivity;
    }

    public void getDataSubmitOrder(String emp_id, String dealer_id, String total_order_price, String total_kg_weight, String retailer_id) {

        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
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
                        try {
                            SuccessModel successModel = gson.fromJson(response, SuccessModel.class);
                            if (successModel.getSuccess())
                            {

                                finish();
                                Toast.makeText(RetailerActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(RetailerActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e)
                        {

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

                            *//*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*//*
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
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                //params.put("retailer_id", retailer_id);
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

                Log.d("dixitttabdobariya","aaa "+jsonArray.toString());
                Log.d("dixitttabdobariya","aaa "+total_order_price);
                Log.d("dixitttabdobariya","aaa "+emp_id);
                Log.d("dixitttabdobariya","aaa "+dealer_id);
                params.put("products", jsonArray.toString());
                Log.d("dixitttabdobariya12","aaa "+params.toString());



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



    public void getDataNpcSubmitOrder(String emp_id, String dealer_id, String route_id1, String retailer_id) {

        loadingView = new LoadingView(RetailerActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(RetailerActivity.this);
        String url;
        url = getString(R.string.json_npc);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponseddddddd", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        categoryItemModelList1.clear();
                        Gson gson=new Gson();
                        try {
                            SuccessModel successModel = gson.fromJson(response, SuccessModel.class);
                            if (successModel.getSuccess())
                            {

                                finish();
                                Toast.makeText(RetailerActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(RetailerActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e)
                        {

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

                            *//*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*//*
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
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(RetailerActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("retailer_id", retailer_id);
                params.put("route_id", route_id1);
                //  params.put("total_order_price", ""+total_pricenew1);
                /*params.put("total_order_weight", ""+total_kgnew1);

                JSONObject obj = null;
                JSONArray jsonArray = new JSONArray();
                for (int j = 0; j < categoryItemModelList1.size(); j++) {
                    for (int i = 0; i < categoryItemModelList1.get(j).getProducts().size(); i++) {
                        if (categoryItemModelList1.get(j).getProducts().get(i).getTotal_kg()==0)
                        {

                        }
                        else
                        {
                            obj = new JSONObject();
                            try {
                                obj.put("category_id", categoryItemModelList1.get(j).getId());
                                obj.put("product_id", categoryItemModelList1.get(j).getProducts().get(i).getId());
                                obj.put("total_weight", categoryItemModelList1.get(j).getProducts().get(i).getTotal_qty());
                                //           obj.put("original_price", categoryItemModelList1.get(j).getProducts().get(i).getPrice());
                                //             obj.put("total_price", categoryItemModelList1.get(j).getProducts().get(i).getTotal_price());
                                //                obj.put("original_weight", categoryItemModelList1.get(j).getProducts().get(i).getWeight());
                                //              obj.put("total_weight", categoryItemModelList1.get(j).getProducts().get(i).getTotal_kg());

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(obj);
                        }

                    }
                }

                Log.d("dixitttabdobariya","aaa "+jsonArray.toString());
                Log.d("dixitttabdobariya","aaa "+total_order_price);
                Log.d("dixitttabdobariya","aaa "+emp_id);
                Log.d("dixitttabdobariya","aaa "+dealer_id);
                params.put("products", jsonArray.toString());*/
                Log.d("dixitttabdobariya12","aaa "+params.toString());



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







    /*@Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }
}
