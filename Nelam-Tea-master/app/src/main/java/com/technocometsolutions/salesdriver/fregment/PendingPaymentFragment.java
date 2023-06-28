package com.technocometsolutions.salesdriver.fregment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.technocometsolutions.salesdriver.adapter.DealerPaymentPendingAdapter;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;
import com.technocometsolutions.salesdriver.model.DealerPaymentPandingItemModel;
import com.technocometsolutions.salesdriver.model.DealerPaymentPandingModel;
import com.technocometsolutions.salesdriver.utlity.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class PendingPaymentFragment extends Fragment {

    public SearchableSpinner sp_dealers;
    public RecyclerView rv_payment;
    public RecyclerView.LayoutManager layoutManager;
    public DealerPaymentPendingAdapter mAdapter;
    public EditText et_amount,et_remark;

    public Button btn_save;
    public SessionManager sessionManager;
    public String dealer_id="";
    public TextView tv_total;
    public LinearLayout lv_select_dealer_or_not;
    public LinearLayout layout_save;
    public StateAdapter stateAdapter;
    public final ArrayList<String> mStrings = new ArrayList<>();
    public static PendingPaymentFragment pendingPaymentFragment;
    public ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    public List<DealerPaymentPandingItemModel> dealerPaymentPandingItemModelArrayList = new ArrayList<>();
    public List<DealerPaymentPandingItemModel> dealerPaymentPandingItemModelArrayList1 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_payment, container, false);

        sessionManager=new SessionManager(getActivity());
        pendingPaymentFragment=this;
        lv_select_dealer_or_not = view.findViewById(R.id.lv_select_dealer_or_not);
        layout_save= view.findViewById(R.id.layout_save);
        et_amount = view.findViewById(R.id.et_amount);
        et_remark = view.findViewById(R.id.et_remark);
        btn_save = view.findViewById(R.id.btn_save);
        tv_total = view.findViewById(R.id.tv_total);
        sp_dealers = view.findViewById(R.id.sp_dealers);
        rv_payment = view.findViewById(R.id.rv_payment);

        layoutManager = new LinearLayoutManager(getActivity());
        rv_payment.setHasFixedSize(true);
        rv_payment.setLayoutManager(layoutManager);



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (et_amount.getText().toString().equalsIgnoreCase("") || et_amount.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
                } else */
                if (dealer_id.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please select dealer", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getActivity(), "Api Discussion ma che", Toast.LENGTH_SHORT).show();
                    getCollectPaymnetList(sessionManager.getId(),dealer_id);
                    //getPaymentapi(sessionManager.getId(),et_amount.getText().toString(),dealer_id);
                }
            }
        });

        lv_select_dealer_or_not.setVisibility(View.GONE);
        //getDealerApi();

        getDealerApi();
        stateAdapter = new StateAdapter(getActivity(), mStrings,"Select Dealers");
        sp_dealers.setAdapter(stateAdapter);
        sp_dealers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                if (position == 0) {
                    lv_select_dealer_or_not.setVisibility(View.GONE);
                    dealer_id = "";

                } else {
                    lv_select_dealer_or_not.setVisibility(View.VISIBLE);
                   // dealer_id = dealerListItemModels.get(position - 1).getId();

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
                    getDealerApiPaymentpanding(dealer_id);

//                    Log.d("delar_id111",""+dealerListItemModels.get((int) id).toString());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return view;
    }


  public void getDealerApiPaymentpanding(String dealer_id) {
        //spinnar
        dealerPaymentPandingItemModelArrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_dealer_payment);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd123", "onResponse: "+response);


                        Gson gson = new Gson();
                        DealerPaymentPandingModel loginResponse = gson.fromJson(response, DealerPaymentPandingModel.class);
                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerPaymentPandingItemModelArrayList=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerPaymentPandingItemModelArrayList.size());
                                mAdapter = new DealerPaymentPendingAdapter(getActivity(),dealerPaymentPandingItemModelArrayList);
                                rv_payment.setAdapter(mAdapter);

                                // mAdapter.notifyDataSetChanged();
                                tv_total.setText("₹"+loginResponse.getTotal());

                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("dealer_id",""+dealer_id);
                return params;
            };
        };

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    public void getCollectPaymnetList(String emp_id,String dealer_id) {
        //spinnar
        dealerPaymentPandingItemModelArrayList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_dealer_collect_payment);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);

                        dealerPaymentPandingItemModelArrayList1.clear();
                        getActivity().finish();
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
                if (dealerPaymentPandingItemModelArrayList1.size()!=0) {
                    for (int i = 0; i < dealerPaymentPandingItemModelArrayList1.size(); i++) {
                        params.put("order[" + i + "]", "" + dealerPaymentPandingItemModelArrayList1.get(i).getOrderId());
                    }
                }
                if(!et_amount.getText().toString().trim().equals("")){
                    params.put("payment",et_amount.getText().toString().trim());
                    params.put("remark",et_remark.getText().toString().trim());

                }


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
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url;
            url = getString(R.string.json_dealer_list);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("dd123", "onResponse: "+response);


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
                                        String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId();mStrings.add(data + " " + data1);

                                        //mStrings.add(loginResponse.getItems().get(i).getDealerName());
                                    }

                                    stateAdapter = new StateAdapter(getActivity(), mStrings,"Select Dealers");
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
                                Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    public static PendingPaymentFragment getInstance()
    {
        return pendingPaymentFragment;
    }

}
