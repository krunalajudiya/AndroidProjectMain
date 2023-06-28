package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.RetailerOrderAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewExpensesAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewExpensesModel;
import com.technocometsolutions.salesdriver.model.CategoryListModel1;
import com.technocometsolutions.salesdriver.model.ExpensesModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ViewExpenses extends AppCompatActivity {

    RecyclerView expensesrecyclerview;
    public LoadingView loadingView;
    public SessionManager sessionManager;
    public ErrorView errorView;
    ArrayList<ExpensesModel> expensesModelsArraylist=new ArrayList<>();
    ViewExpensesAdapter viewExpensesAdapter;
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewExpenses.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        expensesrecyclerview=findViewById(R.id.expensesrecyclerview);


        getexpensedetail(sessionManager.getId());

    }

    void getexpensedetail(String emp_id){

        loadingView = new LoadingView(ViewExpenses.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(ViewExpenses.this);

        String url;
        url = getString(R.string.json_view_daily_expense);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        ViewExpensesModel loginResponse = gson.fromJson(response, ViewExpensesModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                Log.d("ff","done");
                                Log.d("ffff", String.valueOf(loginResponse.getItems().get(1).getBreakfast_charge()));
                                expensesModelsArraylist=loginResponse.getItems();
                                viewExpensesAdapter=new ViewExpensesAdapter(ViewExpenses.this,expensesModelsArraylist);
                                expensesrecyclerview.setLayoutManager(linearLayoutManager);
                                expensesrecyclerview.setAdapter(viewExpensesAdapter);
                                viewExpensesAdapter.setOnItemClickListener(new ViewExpensesAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int pos, View v) {
                                        Intent intent=new Intent(ViewExpenses.this, ExpensesDetail.class);
                                        intent.putExtra("edit_expense",true);
                                        intent.putExtra("position",pos);
                                        intent.putExtra("arraylist",new Gson().toJson(expensesModelsArraylist));
                                        Bundle bundle=new Bundle();
                                        bundle.putString("e_id",String.valueOf(expensesModelsArraylist.get(pos).getE_id()));
                                        bundle.putString("emp_id",expensesModelsArraylist.get(pos).getEmp_id());
                                        bundle.putString("e_date",expensesModelsArraylist.get(pos).getE_date());
                                        bundle.putString("e_from",expensesModelsArraylist.get(pos).getE_from());
                                        bundle.putString("e_to",expensesModelsArraylist.get(pos).getE_to());
                                        bundle.putString("hotel_stay",expensesModelsArraylist.get(pos).getHotel_stay());
                                        bundle.putString("t_id",expensesModelsArraylist.get(pos).getT_id());
                                        bundle.putString("distance",expensesModelsArraylist.get(pos).getDistance());
                                        bundle.putString("toll_charge",expensesModelsArraylist.get(pos).getToll_charge());
                                        bundle.putString("breakfast_charge",expensesModelsArraylist.get(pos).getBreakfast_charge());
                                        bundle.putString("lunch_charge",expensesModelsArraylist.get(pos).getLunch_charge());
                                        bundle.putString("dinner_charge",expensesModelsArraylist.get(pos).getDinner_charge());
                                        bundle.putString("da",expensesModelsArraylist.get(pos).getDa());
                                        bundle.putString("mis_charge",expensesModelsArraylist.get(pos).getMis_charge());
                                        bundle.putString("remarks",expensesModelsArraylist.get(pos).getRemarks());
                                        bundle.putString("bill",expensesModelsArraylist.get(pos).getBill());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        else
                        {

                            Toast.makeText(ViewExpenses.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(ViewExpenses.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                params.put("emp_id", emp_id);
                return params;
            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
//        viewExpensesAdapter.setOnItemClickListener(new ViewExpensesAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int pos, View v) {
//

//            }
//        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}