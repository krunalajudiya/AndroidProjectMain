package com.technocometsolutions.salesdriver.fregment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.technocometsolutions.salesdriver.activity.SalesReport;
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class FormStep1 extends Fragment implements Step , BlockingStep {


    EditText firmname,gstno,panno,bank;
    public SearchableSpinner city;
    RadioGroup firmtype;
    RadioButton proprietorship,partnership;
    String firmname_txt,gstno_txt,panno_txt,bank_txt,firmtype_txt,city_txt;
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step1, container, false);
        firmname=view.findViewById(R.id.firmname);
        gstno=view.findViewById(R.id.gstno);
        panno=view.findViewById(R.id.panno);
        bank=view.findViewById(R.id.bank);
        city=view.findViewById(R.id.d_city);
        firmtype=view.findViewById(R.id.firmtyperadiogroup);






        getCityApi();
        stateAdaptercity =  new StateAdapter(getActivity(), mStrings_city,"Select City");
        city.setAdapter(stateAdaptercity);
        city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                city_txt=mStrings_city.get(position-1);


                // Toast.makeText(DealerServeReportActivity.this, ""+cityItemModelArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        city.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {

            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        return view;
    }
    void getalldata(){
        if (firmname.getText().toString().trim().length()>0 && gstno.getText().toString().trim().length()>0 && panno.getText().toString().trim().length()>0
        && bank.getText().toString().trim().length()>0 && firmtype.getCheckedRadioButtonId()!=-1){

            firmname_txt=firmname.getText().toString().trim();
            gstno_txt=gstno.getText().toString().trim();
            panno_txt=panno.getText().toString().trim();
            bank_txt=bank.getText().toString().trim();

            if (proprietorship.isChecked()){
                firmtype_txt="proprietorship";
            }
            else if (partnership.isChecked()){
                firmtype_txt="partnership";
            }

        }
    }

    public void getCityApi() {
        cityItemModelArrayList.clear();
        mStrings_city.clear();
        // loadingView = new LoadingView(SaveexActivity.this);
        // loadingView.showLoadingView();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url;
        url = getString(R.string.json_all_city);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dd", "onResponse: "+response);
//                        loadingView.hideLoadingView();

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

                        try {
                            Gson gson = new Gson();
                            CityModel loginResponse = gson.fromJson(response, CityModel.class);
                            if (loginResponse.getSuccess())
                            {
                                if (loginResponse.getItems() != null) {
                                    cityItemModelArrayList = loginResponse.getItems();
                                    for (int i=0;i<cityItemModelArrayList.size();i++)
                                    {

                                        String data = loginResponse.getItems().get(i).getName();
                                        String data1=  "<font color=#11FFFFFF> % " + loginResponse.getItems().get(i).getId() ;
                                        mStrings_city.add(data + " " + data1);

//                                    mStrings_city.add(loginResponse.getItems().get(i).getName());
                                    }

                               /* dealerListItemModels=loginResponse.getItems();
                                Log.d("dixittttt", "onCreate: "+dealerListItemModels.size());

                                mSimpleListAdapter = new SimpleListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSimpleArrayListAdapter = new SimpleArrayListAdapter(OrderActivity.this, loginResponse.getItems());
                                mSearchableSpinner.setAdapter(mSimpleArrayListAdapter);
                                CustomAdapter customAdapter=new CustomAdapter(OrderActivity.this,R.layout.view_list_item,R.id.tv_product_name1,loginResponse.getItems());
                                mSearchableSpinner1.setAdapter(customAdapter);*/


                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), ""+loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {

                        }

                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  loadingView.hideLoadingView();
                Log.d("dd", "onErrorResponse: "+error.getMessage());
                /*errorView = new ErrorView(SaveexActivity.this, new ErrorView.OnNoInternetConnectionListerner() {
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
                //params.put("state_id", state_id);
                //params.put("password", password);
                return params;
            };
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

        Fragment step2=new FormStep2();
        Bundle step1=new Bundle();
        step1.putString("step1data","send data");
        step2.setArguments(step1);

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {


        callback.goToPrevStep();

    }
}