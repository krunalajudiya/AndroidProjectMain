package com.technocometsolutions.salesdriver.activity;

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
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class DealerVisitActivity extends AppCompatActivity {
    public SearchableSpinner sp_dealers;
    public Button btn_save;
    public EditText et_amount;
    public SessionManager sessionManager;
    public LoadingView loadingView;
    public ErrorView errorView;
    public String dealer_id="";
    public StateAdapter stateAdapter;
    private final ArrayList<String> mStrings = new ArrayList<>();
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_visit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager=new SessionManager(this);
        sp_dealers=findViewById(R.id.sp_dealers);
        //lv_select_dealer_or_not=findViewById(R.id.lv_select_dealer_or_not);
        et_amount=findViewById(R.id.et_amount);
        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_amount.getText().toString().equalsIgnoreCase("") || et_amount.getText().toString().equalsIgnoreCase("0"))
                {
                    Toast.makeText(DealerVisitActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                }
                else if (dealer_id.equalsIgnoreCase(""))
                {
                    Toast.makeText(DealerVisitActivity.this, "Please select dealer", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(DealerVisitActivity.this, "Api Discussion ma che", Toast.LENGTH_SHORT).show();
                    //getPaymentapi(sessionManager.getId(),et_amount.getText().toString(),dealer_id);
                }
            }
        });

       // lv_select_dealer_or_not.setVisibility(View.GONE);
        getDealerApi(sessionManager.getId());
        GpsProvider.onGPS(this);
        stateAdapter = new StateAdapter(DealerVisitActivity.this, mStrings,"Select Dealers");
        sp_dealers.setAdapter(stateAdapter);
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position==0)
                {
                  //  lv_select_dealer_or_not.setVisibility(View.GONE);
                    dealer_id="";
                    //  lv_select_dealer.setVisibility(View.GONE);
                }
                else
                {
                 //   lv_select_dealer_or_not.setVisibility(View.VISIBLE);
                    //dealer_id=dealerListItemModels.get(position-1).getId();
                    String dealerSplit= stateAdapter.getItem(position).toString();
                    String[] lastFinalId= dealerSplit.split(" % ");
                    String splitDelarId = lastFinalId[lastFinalId.length-1];




                    for (int i=0;i<dealerListItemModels.size();i++)
                    {
                        if (dealerListItemModels.get(i).getId().equals(splitDelarId))
                        {
                            dealer_id=dealerListItemModels.get(i).getId();
                            break;
                        }
                    }

                   /* lv_select_dealer.setVisibility(View.VISIBLE);
                    getCategorisApi(""+dealerListItemModels.get(position-1).getId());*/
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    public void getDealerApi(String emp_id) {
        //spinnar
        dealerListItemModels.clear();
        loadingView = new LoadingView(DealerVisitActivity.this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(DealerVisitActivity.this);
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
                                dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());
                                for (int i=0;i<dealerListItemModels.size();i++)
                                {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);
                                    //mStrings.add(loginResponse.getItems().get(i).getDealerName());
                                }

                                stateAdapter = new StateAdapter(DealerVisitActivity.this, mStrings,"Select Dealers");
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
                            Toast.makeText(DealerVisitActivity.this, ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(DealerVisitActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
