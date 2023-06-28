package com.technocometsolutions.salesdriver.fregment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.technocometsolutions.salesdriver.activity.YourHistryActivity;
import com.technocometsolutions.salesdriver.adapter.GetOrderListAdapter;
import com.technocometsolutions.salesdriver.adapter.PendingLeaveAdapter;
import com.technocometsolutions.salesdriver.model.GetDelalerListModel;
import com.technocometsolutions.salesdriver.model.GetOrderListModel;
import com.technocometsolutions.salesdriver.model.PendingLeaveItemModel;
import com.technocometsolutions.salesdriver.model.PendingLeaveModel;
import com.technocometsolutions.salesdriver.utlity.ErrorView;
import com.technocometsolutions.salesdriver.utlity.LoadingView;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingLeaveFragment extends Fragment {
    public SessionManager sessionManager;
    public LoadingView loadingView;
    public ErrorView errorView;
    public PendingLeaveAdapter pendingLeaveAdapter;
    public List<PendingLeaveItemModel> pendingLeaveItemModelList;
    public RecyclerView rvCategoriesList;
    public LinearLayout nodata;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pending_leave, container, false);
        rvCategoriesList=(RecyclerView)view.findViewById(R.id.rvpendingLeave);
        nodata=view.findViewById(R.id.noData);
        sessionManager=new SessionManager(getContext());
        getPendingLeaveList(""+sessionManager.getId());
        return view;
    }

    public void getPendingLeaveList(String emp_id) {
        loadingView = new LoadingView(getActivity());
        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.pendingLeavesList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", "onResponse: "+response);
                        loadingView.hideLoadingView();
                        Gson gson = new Gson();
                        PendingLeaveModel loginResponse = gson.fromJson(response, PendingLeaveModel.class);
                        if (loginResponse.getSuccess())
                        {
                            if (loginResponse.getItems() != null) {
                                loadingView.hideLoadingView();
                                pendingLeaveItemModelList=loginResponse.getItems();
                                pendingLeaveAdapter=new PendingLeaveAdapter(getActivity(),pendingLeaveItemModelList);
                                rvCategoriesList.setAdapter(pendingLeaveAdapter);
                                nodata.setVisibility(View.GONE);
                                rvCategoriesList.setVisibility(View.VISIBLE);

                                Log.d("Dixit","aa"+pendingLeaveItemModelList.size());
                                Log.d("Dixit","aa"+loginResponse.getItems().size());
                                }



                        }
                        else

                        {
                            nodata.setVisibility(View.VISIBLE);
                            rvCategoriesList.setVisibility(View.GONE);

                            //Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
                    @Override
                    public void onRetryButtonClicked() {
                        //getDataPunchOutView(sessionManager.getId(),fromdateVal,todateVal);
                    }

                    @Override
                    public void onCancelButtonClicked() {
                        //onBackPressed();
                    }
                });
                errorView.showLoadingView();

            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id);
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

}
