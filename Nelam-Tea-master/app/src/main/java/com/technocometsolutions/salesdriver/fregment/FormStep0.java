package com.technocometsolutions.salesdriver.fregment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;
import com.technocometsolutions.salesdriver.model.DealerListModel;

import java.util.ArrayList;


public class FormStep0 extends Fragment implements Step, BlockingStep {

    RadioGroup radioGroup;
    Spinner delerspinner;
    RadioButton delar,distributer;
    private ArrayList<DealerListItemModel> dealerListItemModels = new ArrayList<>();
    private final ArrayList<String> mStrings = new ArrayList<>();
//    public StateAdapter stateAdapter;
    ArrayAdapter deleradapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step0, container, false);
        radioGroup=view.findViewById(R.id.delarordistributer);
        delerspinner=view.findViewById(R.id.delerspinner);
        delar=view.findViewById(R.id.delar);
        distributer=view.findViewById(R.id.distributer);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.delar){
                    delerspinner.setVisibility(View.VISIBLE);
                }
                if (checkedId==R.id.distributer){
                    delerspinner.setVisibility(View.GONE);
                }
            }
        });

        getDealerApi();

        return view;
    }

    public void getDealerApi() {
        //spinnar
//        dealerListItemModels.clear();
//        loadingView = new LoadingView(AddOrderActivity.this);
//        loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_dealer_list);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
//                        loadingView.hideLoadingView();


                        Gson gson = new Gson();
                        DealerListModel loginResponse = gson.fromJson(response, DealerListModel.class);

                        if (loginResponse.getSuccess())
                        {

                            if (loginResponse.getItems() != null) {
                                dealerListItemModels = loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: " + dealerListItemModels.size());
                                for (int i = 0; i < dealerListItemModels.size(); i++) {

                                    String data = loginResponse.getItems().get(i).getDealerName();
                                    String data1=  "" + loginResponse.getItems().get(i).getId();
                                    mStrings.add(data + " " + data1);

                                }
//                                stateAdapter = new StateAdapter(getActivity(), mStrings,"Select Dealers");
                                deleradapter =new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,mStrings);
                                delerspinner.setAdapter(deleradapter);

//                                for (int j=1;j<dealerListItemModels.size();j++)
//                                {
//                                    String dealerSplit= stateAdapter.getItem(j).toString();
//                                    Log.d("DealerPoist",""+dealerSplit);
//                                    String[] splitDelarId= dealerSplit.split(" % ");
//                                    String lastFinalId = splitDelarId[splitDelarId.length-1];
//
//                                    if (!sessionManager.getDealerId().equals("0")) {
//                                        if (sessionManager.getDealerId().equals(lastFinalId)) {
//                                            int abc = mStrings.indexOf(dealerSplit);
//                                            sp_dealers.setSelectedItem(abc + 1);
//                                            Log.d("1DealerPoist", "" + abc);
//                                        }
//                                    }
//
//                                }




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
//                loadingView.hideLoadingView();
//                Log.d("dd", "onErrorResponse: "+error.getMessage());
//                errorView = new ErrorView(getActivity(), new ErrorView.OnNoInternetConnectionListerner() {
//                    @Override
//                    public void onRetryButtonClicked() {
//                        // getDataPunchOut(sessionManager.getId());
//                    }
//
//                    @Override
//                    public void onCancelButtonClicked() {
//                        onBackPressed();
//                    }
//                });
//                errorView.showLoadingView();

            }
        }){
//            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//                return params;
//            };
        };

// Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (delar.isChecked()){
            callback.goToNextStep();
        }
        if (distributer.isChecked()){
            callback.goToNextStep();
        }

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}