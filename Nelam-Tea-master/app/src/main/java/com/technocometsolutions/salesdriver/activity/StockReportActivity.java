package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.adapter.StockReportAdapter;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.DealerStockItemModel;
import com.technocometsolutions.salesdriver.model.DealerStockModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class StockReportActivity extends AppCompatActivity {
    public RecyclerView my_recycler_order;
    public String dealer_id="";
    public StateAdapter stateAdapter;
    public SearchableSpinner sp_dealers;
    public RecyclerView.LayoutManager layoutManager;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    private List<DealerStockItemModel> dealerStockItemModels = new ArrayList<>();
    public SessionManager sessionManager;
    public StockReportAdapter stockReportAdapter;
    public static StockReportActivity stockReportActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stockReportActivity=this;

        my_recycler_order=findViewById(R.id.my_recycler_order);

        sessionManager=new SessionManager(this);
        sp_dealers = findViewById(R.id.sp_dealers);
        layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        my_recycler_order.setLayoutManager(mLayoutManager);
        my_recycler_order.setItemAnimator(new DefaultItemAnimator());


        getDealerApi();
        GpsProvider.onGPS(this);
        stateAdapter = new StateAdapter(StockReportActivity.this, mStrings,"Select Dealers");
        sp_dealers.setAdapter(stateAdapter);
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                    dealer_id="";
                   // lv_select_dealer.setVisibility(View.GONE);
                }
                else
                {
                    //dealer_id=dealerListItemModels.get(position-1).getId();
                    String dealerSplit= stateAdapter.getItem(position).toString();
                    String splitDelarId= String.valueOf(dealerSplit.lastIndexOf(" % "));



                    for (int i=0;i<dealerListItemModels.size();i++)
                    {
                        if (dealerListItemModels.get(i).getId().equals(splitDelarId))
                        {
                            dealer_id=dealerListItemModels.get(i+1).getId();
                            break;
                        }
                    }
                  //  lv_select_dealer.setVisibility(View.VISIBLE);
                    getCategorisApi(sessionManager.getId(),""+dealer_id);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void getCategorisApi(String emp_id, String dealer_id) {
        RequestQueue queue = Volley.newRequestQueue(StockReportActivity.this);
        String url;
        url = getString(R.string.json_stock_report);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);



                        Gson gson = new Gson();
                        DealerStockModel loginResponse = gson.fromJson(response, DealerStockModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerStockItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                stockReportAdapter=new StockReportAdapter(StockReportActivity.this,dealerStockItemModels);
                                my_recycler_order.setAdapter(stockReportAdapter);

                                 /*sdadsd;
                                 adasd;
                                 asdasd;
                                 adasd;*/

                            }
                        }
                        else
                        {
                            Toast.makeText(StockReportActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("emp_id",emp_id);
                params.put("dealer_id",dealer_id);

                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    public void getDealerApi() {
        //spinnar
        dealerListItemModels.clear();

        RequestQueue queue = Volley.newRequestQueue(StockReportActivity.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);



                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<dealerListItemModels.size();i++)
                                {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "<font color=#ffffff> % " + loginResponse.getItems().get(i).getId() + " </font>" ;
                                    mStrings.add(data + " " + data1);

                                    //mStrings.add(loginResponse.getItems().get(i).getDealerName());
                                }

                                stateAdapter = new StateAdapter(StockReportActivity.this, mStrings,"Select Dealers");
                                sp_dealers.setAdapter(stateAdapter);

                                for (int j=1;j<dealerListItemModels.size();j++)
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
                            Toast.makeText(StockReportActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
