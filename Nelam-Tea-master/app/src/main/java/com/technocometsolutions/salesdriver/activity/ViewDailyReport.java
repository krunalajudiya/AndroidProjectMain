package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.ViewDailyReportAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewExpensesAdapter;
import com.technocometsolutions.salesdriver.adapter.ViewExpensesModel;
import com.technocometsolutions.salesdriver.model.DailyReportModel;
import com.technocometsolutions.salesdriver.model.ViewDailyReportModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ViewDailyReport extends AppCompatActivity {

    RecyclerView reportrecyclerview;
    public LoadingView loadingView;
    public ErrorView errorView;
    ArrayList<DailyReportModel> dailyReportModelArrayList=new ArrayList<>();
    ViewDailyReportAdapter viewDailyReportAdapter;
    public SessionManager sessionManager;
//    LinearLayoutManager layoutManager;
//    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewDailyReport.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reportrecyclerview=findViewById(R.id.reportrecyclerview);
        sessionManager=new SessionManager(getApplicationContext());
//        layoutManager=new LinearLayoutManager(ViewDailyReport.this);
        Log.d("emp_Id",sessionManager.getId());
        //getreportdetails(sessionManager.getId());

    }

    void getreportdetails(String emp_id){

        loadingView = new LoadingView(ViewDailyReport.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(ViewDailyReport.this);

        String url;
        url = getString(R.string.json_view_daily_report);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        ViewDailyReportModel loginResponse = gson.fromJson(response, ViewDailyReportModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                dailyReportModelArrayList=loginResponse.getItems();
                                Log.d("ff","done");
                                Log.d("ff",loginResponse.getItems().get(0).getShop_name());
                                viewDailyReportAdapter=new ViewDailyReportAdapter(ViewDailyReport.this,dailyReportModelArrayList);
                                LinearLayoutManager layoutManager=new LinearLayoutManager(ViewDailyReport.this);
                                reportrecyclerview.setLayoutManager(layoutManager);
                                reportrecyclerview.setAdapter(viewDailyReportAdapter);
                                viewDailyReportAdapter.setOnItemClickListener(new ViewExpensesAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int pos, View v) {
                                        Intent intent=new Intent(ViewDailyReport.this,SalesReport.class);
                                        intent.putExtra("position",pos);
                                        intent.putExtra("editvalue",true);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("d_id",dailyReportModelArrayList.get(pos).getD_id());
                                        bundle.putString("emp_id",dailyReportModelArrayList.get(pos).getEmp_id());
                                        bundle.putString("d_date",dailyReportModelArrayList.get(pos).getD_date());
                                        bundle.putString("client_status",dailyReportModelArrayList.get(pos).getClient_status());
                                        bundle.putString("shop_name",dailyReportModelArrayList.get(pos).getShop_name());
                                        bundle.putString("person_name",dailyReportModelArrayList.get(pos).getPerson_name());
                                        bundle.putString("contact_number",dailyReportModelArrayList.get(pos).getContact_number());
                                        bundle.putString("city_name",dailyReportModelArrayList.get(pos).getCity_name());
                                        bundle.putString("in_product",dailyReportModelArrayList.get(pos).getIn_product());
                                        bundle.putString("meeting_reason",dailyReportModelArrayList.get(pos).getMeeting_reason());
                                        bundle.putString("remark",dailyReportModelArrayList.get(pos).getRemark());
                                        bundle.putString("visiting_card",dailyReportModelArrayList.get(pos).getVisiting_card());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        else
                        {

                            Toast.makeText(ViewDailyReport.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(ViewDailyReport.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getreportdetails(sessionManager.getId());
    }
}