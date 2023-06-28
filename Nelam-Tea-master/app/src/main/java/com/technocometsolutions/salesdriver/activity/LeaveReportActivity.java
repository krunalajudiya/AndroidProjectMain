package com.technocometsolutions.salesdriver.activity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.GpsProvider;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LeaveReportActivity extends AppCompatActivity {
    AppCompatEditText et_date_frome,et_date_to,et_reason;
    AppCompatButton btn_save_leave;
    int day, month, year;
    int dayFinal, monthFinal, yearFinal;
    String from_date="",to_date="",reason="";
    public SessionManager sessionManager;
    public LoadingView loadingView;
    public ErrorView errorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_date_frome=findViewById(R.id.et_date_frome);
        et_date_to=findViewById(R.id.et_date_to);
        et_reason=findViewById(R.id.et_reason);
        btn_save_leave=findViewById(R.id.btn_save_leave);
        sessionManager=new SessionManager(LeaveReportActivity.this);

        GpsProvider.onGPS(this);
        et_date_frome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearFinal = year;
                        monthFinal = month + 1;
                        dayFinal = dayOfMonth;

                        et_date_frome.setText(""+dayFinal+"-"+monthFinal+"-"+yearFinal);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        et_date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearFinal = year;
                        monthFinal = month + 1;
                        dayFinal = dayOfMonth;
                        et_date_to.setText(""+dayFinal+"-"+monthFinal+"-"+yearFinal);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        btn_save_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from_date=et_date_frome.getText().toString();
                to_date=et_date_to.getText().toString();
                reason=et_reason.getText().toString();
                if (from_date.equalsIgnoreCase(""))
                {
                    Toast.makeText(LeaveReportActivity.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }
                else if (to_date.equalsIgnoreCase(""))
                {
                    Toast.makeText(LeaveReportActivity.this, "Please Select To Date", Toast.LENGTH_SHORT).show();
                }
                else if (reason.equalsIgnoreCase(""))
                {
                    Toast.makeText(LeaveReportActivity.this, "Please Enter Reason", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getData_leave(""+sessionManager.getId(),from_date,to_date,reason);
                }



            }
        });


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getData_leave(String emp_id, String from_date, String to_date, String reason) {
        loadingView = new LoadingView(this);
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = getString(R.string.json_leave_report);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dixit", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Toast.makeText(LeaveReportActivity.this, " Your Leave SassesFully Add! ", Toast.LENGTH_SHORT).show();
                        et_date_frome.setText("");
                        et_date_to.setText("");
                        et_reason.setText("");
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dixit", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(LeaveReportActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //  getDataLogin(userName,password);
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
                params.put("start_date", from_date);
                params.put("end_date", to_date);
                params.put("reason", reason);
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
