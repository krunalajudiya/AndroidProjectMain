package com.technocometsolutions.salesdriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.GetOrderListAdapter;
import com.technocometsolutions.salesdriver.adapter.NotificationAdapter;
import com.technocometsolutions.salesdriver.adapter.RetailerOrderAdapter;
import com.technocometsolutions.salesdriver.model.GetOrderListModel;
import com.technocometsolutions.salesdriver.model.NotificationItemModel;
import com.technocometsolutions.salesdriver.model.NotificationModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    public RecyclerView rvNotification;
    public RecyclerView.LayoutManager layoutManager;
    public RetailerOrderAdapter mAdapter;
    public LoadingView loadingView;
    public ErrorView errorView;
    public List<NotificationItemModel> notificationItemModelList=new ArrayList<>();
    public NotificationAdapter notificationAdapter;
    public SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GpsProvider.onGPS(this);

        rvNotification=findViewById(R.id.rvNotification);
        layoutManager=new LinearLayoutManager(this);
        rvNotification.setLayoutManager(layoutManager);
        sessionManager=new SessionManager(this);
        getNotificationList(""+sessionManager.getId());
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getNotificationList(String emp_id) {


        loadingView = new LoadingView(NotificationActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(NotificationActivity.this);
        String url;
        url = getString(R.string.json_notification);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        NotificationModel loginResponse = gson.fromJson(response, NotificationModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                loadingView.hideLoadingView();
                                notificationItemModelList=loginResponse.getItems();
                                notificationAdapter=new NotificationAdapter(NotificationActivity.this,notificationItemModelList);
                                rvNotification.setAdapter(notificationAdapter);

                                Log.d("Dixit","aa"+notificationItemModelList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());

                            }
                        }
                        else
                        {
                            Toast.makeText(NotificationActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(NotificationActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
    protected void onResume() {
        super.onResume();
        GpsProvider.onGPS(this);
    }
}
