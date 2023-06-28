package com.technocometsolutions.salesdriver.activity;

import android.app.SearchManager;
import android.content.Context;
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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.CustomAdapter;
import com.technocometsolutions.salesdriver.adapter.MyListAdapter;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;
import com.technocometsolutions.salesdriver.model.CategoryListModel;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
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

public class OrderScNewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private SearchView search;
    private MyListAdapter listAdapter;
    private ExpandableListView myList;
    private List<CategoryItemModel> continentList = new ArrayList<>();
    private List<CategoryItemModel> continentList1 = new ArrayList<>();
    public LoadingView loadingView;
    public ErrorView errorView;
    public TextView tv_total_price,tv_kg_weight;
    public static OrderScNewActivity activity;
    public com.toptoche.searchablespinnerlibrary.SearchableSpinner mSearchableSpinner1;
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public TextView btn_submit;
    public SessionManager sessionManager;
    public String dealer_id="0";
    public double total_pricenew1 = 0;
    public double total_kgnew1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sc_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager=new SessionManager(this);
        mSearchableSpinner1 = findViewById(R.id.sincewall);
        btn_submit = findViewById(R.id.btn_submit);
        GpsProvider.onGPS(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_kg_weight = findViewById(R.id.tv_kg_weight);
        tv_total_price.setText("Total Price \n ₹0 ");
        tv_kg_weight.setText("Weight &amp; KG \n 0 KG");
        activity=this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);

        //display the list
        displayList();
        //expand all Groups
      //  expandAll();

        mSearchableSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dealer_id = dealerListItemModels.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


                for (int i=0;i<continentList.size();i++)
                {


                    for (int j=0; j < continentList.get(i).getProducts().size();j++)
                    {
                        //wiat for app is run
                        /*total_pricenew +=Double.parseDouble(""+continentList.get(i).getProducts().get(j).getTotal_price());
                        total_kgnew +=Double.parseDouble(""+continentList.get(i).getProducts().get(j).getTotal_kg());
                        tv_total_price.setText("Total Price \n ₹"+total_pricenew+" ");
                        tv_kg_weight.setText("Weight &amp; KG \n "+total_kgnew+" KG");*/

                        if (continentList.get(i).getProducts().get(j).getTotal_qty()==0)
                        {

                        }
                        else
                        {
                            continentList1.add(continentList.get(i));
                        }

                    }
                }

                if (tv_total_price.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(OrderScNewActivity.this, "Please Enter Qutenty", Toast.LENGTH_SHORT).show();
                }
                else if (sessionManager.getId().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(OrderScNewActivity.this, "Please Select Id", Toast.LENGTH_SHORT).show();
                }

                else if (dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(OrderScNewActivity.this, "Please Select Dealer", Toast.LENGTH_SHORT).show();
                }
                else if (continentList1.size()==0)
                {
                    Toast.makeText(OrderScNewActivity.this, "Please Enter Order", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    getDataSubmitOrder(sessionManager.getId(),dealer_id,tv_total_price.getText().toString());
                }



            }
        });

        //spinnar
        mSearchableSpinner1 = findViewById(R.id.sincewall);
        dealerListItemModels.clear();
        loadingView = new LoadingView(OrderScNewActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(OrderScNewActivity.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
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
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());

                                /*mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);*/
                                CustomAdapter customAdapter=new CustomAdapter(OrderScNewActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);


                            }
                        }
                        else
                        {
                            Toast.makeText(OrderScNewActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(OrderScNewActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    public boolean onClose() {
        listAdapter.filterData("");
      //  expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);
       // expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filterData(newText);
       // expandAll();
        return false;
    }


    //method to expand all groups
   /* private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            myList.expandGroup(i);
        }
    }*/

    //method to expand all groups
    private void displayList() {

        //display the list
        //loadSomeData();
        //get reference to the ExpandableListView


        myList = (ExpandableListView) findViewById(R.id.expandableList);

        getDataPunchOutView(""+1);

        //create the adapter by passing your ArrayList data
        //attach the adapter to the list
        // Listview Group click listener
        myList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(getApplicationContext(), "Group Clicked " + listDataTitles.get(groupPosition), Toast.LENGTH_SHORT).show();
                //parent.smoothScrollToPosition(groupPosition);
                if (parent.isGroupExpanded(groupPosition)) {
                    ImageView imageView = v.findViewById(R.id.expandable_icon);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
                } else {
                    ImageView imageView = v.findViewById(R.id.expandable_icon);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_circle_outline_black_24dp));
                }
                return false;
            }
        });
        // Listview Group expanded listener
       /* myList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), listDataTitles.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();

            }
        });*/
        // Listview Group collasped listener
        /*myList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), listDataTitles.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();

            }
        });*/
        // Listview on child click listener
       /* myList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                *//*zxfdsf;
                sdfsdf;
                dfsdf;*//*


                *//*EditText integer_number=v.findViewById(R.id.integer_number);
                if (continentList.get(groupPosition).getProducts().get(childPosition).getMinteger()==0)
                {
                    integer_number.setHint("Qty");
                    integer_number.setText("");
                }
                else
                {
                    integer_number.setHint("Qty");



                    //integer_number.setText("");
                    *//**//*sdfsd;
                    sdfsdf;
                    sdfsdf;*//**//*

                }
                continentList.get(groupPosition).getProducts().get(childPosition).getPrice();*//*

                //Toast.makeText(getApplicationContext(), listDataTitles.get(groupPosition) + " : " + listDataChild.get(listDataTitles.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/



    }

   /* private void loadSomeData() {

        ArrayList<Country> countryList = new ArrayList<Country>();
        Country country = new Country("Pack","5",5);
        countryList.add(country);
        country = new Country("Pack","10",10);
        countryList.add(country);
        country = new Country("Pack","20",20);
        countryList.add(country);
        country = new Country("Pack","50",50);
        countryList.add(country);
        country = new Country("Pack","100",100);
        countryList.add(country);
        country = new Country("Pack","250",250);
        countryList.add(country);
        country = new Country("Pack","500",500);
        countryList.add(country);

        Continent continent = new Continent("Neelam Dano",countryList);
        continentList.add(continent);

        countryList = new ArrayList<Country>();
        country = new Country("Pack","5",5);
        countryList.add(country);
        country = new Country("Pack","10",10);
        countryList.add(country);
        country = new Country("Pack","20",20);
        countryList.add(country);
        country = new Country("Pack","50",50);
        countryList.add(country);
        country = new Country("Pack","100",100);
        countryList.add(country);
        country = new Country("Pack","250",250);
        countryList.add(country);
        country = new Country("Pack","500",500);
        countryList.add(country);


        continent = new Continent("Neelam Pati",countryList);
        continentList.add(continent);






    }*/


    public void getDataPunchOutView(String dealer_id) {

        loadingView = new LoadingView(OrderScNewActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(OrderScNewActivity.this);
        String url;
        url = getString(R.string.json_category_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
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

                            /*Intent intent=new Intent(LoginScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                                //dixit
                                // categoriesListAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(OrderScNewActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(OrderScNewActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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

    public static OrderScNewActivity getActivity()
    {
        return activity;
    }

    public void getProgucteed()
    {
        double total_pricenew = 0;
        double total_kgnew = 0;
        for (int i=0;i<continentList.size();i++)
        {


            for (int j=0; j < continentList.get(i).getProducts().size();j++)
            {
                //wiat for app is run
                total_pricenew +=Double.parseDouble(""+continentList.get(i).getProducts().get(j).getTotal_price());
                total_kgnew +=Double.parseDouble(""+continentList.get(i).getProducts().get(j).getTotal_kg());
                total_pricenew1=total_pricenew;
                total_kgnew1=total_kgnew;
                tv_total_price.setText("Total Price \n ₹"+total_pricenew+" ");
                tv_kg_weight.setText("Weight &amp; KG \n "+total_kgnew+" KG");
            }
        }
    }


    public void getDataSubmitOrder(String emp_id, String dealer_id, String total_order_price) {

        loadingView = new LoadingView(OrderScNewActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(OrderScNewActivity.this);
        String url;
        url = getString(R.string.json_place_order);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        continentList1.clear();
                        try {
                            Gson gson=new Gson();
                            SuccessModel successModel = gson.fromJson(response, SuccessModel.class);
                            if (successModel.getSuccess())
                            {
                                finish();
                                Toast.makeText(OrderScNewActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(OrderScNewActivity.this,successModel.getMessage(),Toast.LENGTH_SHORT).show();

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
                errorView = new ErrorView(OrderScNewActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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

                JSONObject obj = null;
                JSONArray jsonArray = new JSONArray();
                for (int j = 0; j < continentList1.size(); j++) {
                    for (int i = 0; i < continentList1.get(j).getProducts().size(); i++) {
                        obj = new JSONObject();
                        try {
                            obj.put("category_id", continentList1.get(j).getId());
                            obj.put("product_id", continentList1.get(j).getProducts().get(i).getId());
                            obj.put("quantity", continentList1.get(j).getProducts().get(i).getTotal_qty());
                            obj.put("original_price", continentList1.get(j).getProducts().get(i).getPrice());
                            obj.put("total_price", continentList1.get(j).getProducts().get(i).getTotal_price());

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        jsonArray.put(obj);
                    }
                }

                Log.d("dixitttabdobariya","aaa "+jsonArray.toString());
                Log.d("dixitttabdobariya","aaa "+total_order_price);
                Log.d("dixitttabdobariya","aaa "+emp_id);
                Log.d("dixitttabdobariya","aaa "+dealer_id);
                params.put("products", jsonArray.toString());



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
        GpsProvider.onGPS(this);
    }
}
