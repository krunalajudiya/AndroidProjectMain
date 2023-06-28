package com.technocometsolutions.salesdriver.activity;

import android.content.Intent;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.CustomAdapter;
import com.technocometsolutions.salesdriver.adapter.OrderListAdapter;
import com.technocometsolutions.salesdriver.adapter.SimpleArrayListAdapter;
import com.technocometsolutions.salesdriver.adapter.SimpleListAdapter;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.ProductModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class OrderActivity extends AppCompatActivity {
    public RecyclerView rv_product_list;
    private List<ProductModel> productModelList = new ArrayList<>();
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public SearchableSpinner mSearchableSpinner;
    public com.toptoche.searchablespinnerlibrary.SearchableSpinner mSearchableSpinner1;
    private SimpleListAdapter mSimpleListAdapter;
    private SimpleArrayListAdapter mSimpleArrayListAdapter;
    private OrderListAdapter mAdapter;
    public LoadingView loadingView;
    public ErrorView errorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GpsProvider.onGPS(this);
        rv_product_list = findViewById(R.id.rv_product_list);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
               // Intent intent=new Intent(OrderActivity.this,AddOrderActivity.class);
                Intent intent=new Intent(OrderActivity.this,OrderScNewActivity.class);
                startActivity(intent);
            }
        });

        mSearchableSpinner = findViewById(R.id.sp_dealers);
        mSearchableSpinner1 = findViewById(R.id.sincewall);
        dealerListItemModels.clear();
        /*loadingView = new LoadingView(OrderActivity.this);
        loadingView.showLoadingView();*/
        RequestQueue queue = Volley.newRequestQueue(OrderActivity.this);
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
                       // loadingView.hideLoadingView();

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

                                mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
                                CustomAdapter customAdapter=new CustomAdapter(OrderActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);


                            }
                        }
                        else
                        {
                            Toast.makeText(OrderActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(OrderActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                // params.put("emp_id", id);
                //params.put("password", password);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);



        mSearchableSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {

            }
            @Override
            public void onNothingSelected() {

            }
        });
        Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
      //  mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
        rv_product_list = findViewById(R.id.rv_product_list);
        mAdapter = new OrderListAdapter(this,productModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_product_list.setLayoutManager(mLayoutManager);
        rv_product_list.setItemAnimator(new DefaultItemAnimator());
        rv_product_list.setAdapter(mAdapter);

        prepareMovieData();
    }

    public void getDataDealers() {

    }

    public void prepareMovieData() {
        ProductModel productModel=new ProductModel(1,"dd","1000");
        productModelList.add(productModel);
        productModel=new ProductModel(2,"dd","1000");
        productModelList.add(productModel);
        productModel=new ProductModel(3,"dd","1000");
        productModelList.add(productModel);
        productModel=new ProductModel(4,"dd","1000");
        productModelList.add(productModel);
        productModel=new ProductModel(5,"dd","1000");
        productModelList.add(productModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }
}
