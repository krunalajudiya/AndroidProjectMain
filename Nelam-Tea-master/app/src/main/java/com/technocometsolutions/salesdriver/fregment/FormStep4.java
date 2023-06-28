package com.technocometsolutions.salesdriver.fregment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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
import com.technocometsolutions.salesdriver.adapter.StateAdapter;
import com.technocometsolutions.salesdriver.model.CityItemModel;
import com.technocometsolutions.salesdriver.model.CityModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

import static android.app.Activity.RESULT_OK;


public class FormStep4 extends Fragment implements Step, BlockingStep {


    private static final int PICK_FILE_REQUEST = 1001;
    private static final int PERMISSION_CODE=1001;

    Calendar calendar;
    Spinner opratingsince;
    public SearchableSpinner city;
    List<String> arrayListsince;
    EditText currentbusiness,noofshowrooms,noofemployess,showroomsize,showroomaddress;
    ImageView showroomimage;
    int sincepos,citypos;
    String currentbusiness_txt,city_txt,noofshowrooms_txt,noofemployess_txt,since_txt,showroomsize_txt,showroomaddress_txt;
    Uri showroomimage_uri;
    public ArrayList<CityItemModel> cityItemModelArrayList = new ArrayList<>();
    private final ArrayList<String> mStrings_city = new ArrayList<>();
    public StateAdapter stateAdaptercity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step4, container, false);
        opratingsince=view.findViewById(R.id.opratingsince);
        currentbusiness=view.findViewById(R.id.currentbusiness);
        noofshowrooms=view.findViewById(R.id.noofshowrooms);
        noofemployess=view.findViewById(R.id.noofemployees);
        showroomsize=view.findViewById(R.id.showroomsize);
        showroomaddress=view.findViewById(R.id.showroomaddress);
        showroomimage=view.findViewById(R.id.showroomimage);
        city=view.findViewById(R.id.city);





        showroomimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else {
                        ChooseFile();
                    }
                }else {
                    ChooseFile();
                }
            }
        });


        arrayListsince=new ArrayList();
        calendar=Calendar.getInstance();
        sinceyear();
        ArrayAdapter<String> since=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayListsince);
        opratingsince.setAdapter(since);
        opratingsince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sincepos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        if (currentbusiness.getText().toString().trim().length()>0 && noofshowrooms.getText().toString().trim().length()>0 &&
        noofemployess.getText().toString().trim().length()>0 && showroomsize.getText().toString().trim().length()>0 &&
        showroomaddress.getText().toString().trim().length()>0){

            currentbusiness_txt=currentbusiness.getText().toString().trim();
//            city_txt=
            noofshowrooms_txt=noofshowrooms.getText().toString().trim();
            noofemployess_txt=noofemployess.getText().toString().trim();
            since_txt=String.valueOf(arrayListsince.get(sincepos));
            showroomsize_txt=showroomsize.getText().toString().trim();
            showroomaddress_txt=showroomaddress.getText().toString().trim();
        }
    }


    public void sinceyear(){
        for (int i=1900;i<=calendar.get(Calendar.YEAR) ; i++){
            arrayListsince.add(String.valueOf(i));
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


        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {


        callback.goToPrevStep();

    }

    private void ChooseFile() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_FILE_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ff","enter");
        if (resultCode == RESULT_OK && requestCode == PICK_FILE_REQUEST ){
            showroomimage.setImageURI(data.getData());
            showroomimage_uri=data.getData();
        }


    }
}