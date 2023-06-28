package com.technocometsolutions.salesdriver.fregment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.technocometsolutions.salesdriver.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FormStep5 extends Fragment implements Step, BlockingStep {



    Spinner sincewall,sincedc,sincegvtpgvt;
    Calendar calendar;
    List<String> arrayListsince;
    EditText brandwall,lastyearwall,thisyearwall,branddc,lastyeardc,thisyeardc,brandgvtpgvt,lastyeargvtpgvt,thisyeargvtpgvt;
    String brandwall_txt,lastyearwall_txt,thisyearwall_txt,branddc_txt,lastyeardc_txt,thisyeardc_txt,brandgvtpgvt_txt,lastyeargvtpgvt_txt,thisyeargvtpgvt_txt;
    String sincewall_txt,sincedc_txt,sincegvtpgvt_txt;
    int sincewallpos,sincedcpos,sincegvtpgvtpos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step5, container, false);

        sincewall=view.findViewById(R.id.sincewall);
        sincedc=view.findViewById(R.id.sincedc);
        sincegvtpgvt=view.findViewById(R.id.sincegvt);
        brandwall=view.findViewById(R.id.brandwall);
        branddc=view.findViewById(R.id.branddc);
        brandgvtpgvt=view.findViewById(R.id.brandgvtpgvt);
        lastyearwall=view.findViewById(R.id.lastyearwall);
        lastyeardc=view.findViewById(R.id.lastyeardc);
        lastyeargvtpgvt=view.findViewById(R.id.lastyeargvtpgvt);
        thisyearwall=view.findViewById(R.id.thisyearwall);
        thisyeardc=view.findViewById(R.id.thisyeardc);
        thisyeargvtpgvt=view.findViewById(R.id.thisyeargvtpgvt);





        arrayListsince=new ArrayList();
        calendar=Calendar.getInstance();
        sinceyear();
        ArrayAdapter<String> since=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayListsince);
        sincewall.setAdapter(since);
        sincedc.setAdapter(since);
        sincegvtpgvt.setAdapter(since);

        sincewall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sincewallpos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sincedc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sincedcpos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sincegvtpgvt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sincegvtpgvtpos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    void getalldata(){

        if (brandwall.getText().toString().trim().length()>0 && branddc.getText().toString().trim().length()>0 &&
        brandgvtpgvt.getText().toString().trim().length()>0 && lastyearwall.getText().toString().trim().length()>0 &&
        lastyeardc.getText().toString().trim().length()>0 && lastyeargvtpgvt.getText().toString().trim().length()>0 &&
        thisyearwall.getText().toString().trim().length()>0 && thisyeardc.getText().toString().trim().length()>0 &&
        thisyeargvtpgvt.getText().toString().trim().length()>0){

            brandwall_txt=brandwall.getText().toString().trim();
            branddc_txt=branddc.getText().toString().trim();
            brandgvtpgvt_txt=brandgvtpgvt.getText().toString().trim();
            lastyearwall_txt=lastyearwall.getText().toString().trim();
            lastyeardc_txt=lastyeardc.getText().toString().trim();
            lastyeargvtpgvt_txt=lastyeargvtpgvt.getText().toString().trim();
            thisyearwall_txt=thisyearwall.getText().toString().trim();
            thisyeardc_txt=thisyeardc.getText().toString().trim();
            thisyeargvtpgvt_txt=thisyeargvtpgvt.getText().toString().trim();

            sincewall_txt=String.valueOf(arrayListsince.get(sincewallpos));
            sincedc_txt=String.valueOf(arrayListsince.get(sincedcpos));
            sincegvtpgvt_txt=String.valueOf(arrayListsince.get(sincegvtpgvtpos));
        }
    }

    public void sinceyear(){
        for (int i = 1900; i<=calendar.get(Calendar.YEAR) ; i++){
            arrayListsince.add(String.valueOf(i));
        }
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

}