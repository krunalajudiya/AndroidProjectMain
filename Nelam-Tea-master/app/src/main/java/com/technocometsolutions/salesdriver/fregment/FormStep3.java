package com.technocometsolutions.salesdriver.fregment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.technocometsolutions.salesdriver.R;

public class FormStep3 extends Fragment implements Step , BlockingStep {

    EditText contactperson,phonenumber,address;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step3, container, false);
        contactperson=view.findViewById(R.id.contactperson);
        phonenumber=view.findViewById(R.id.phonenumber);
        address=view.findViewById(R.id.address);





        return view;
    }
    void getalldata(){
        if (contactperson.getText().toString().trim().length()>0 && phonenumber.getText().toString().trim().length()>0 &&
        address.getText().toString().trim().length()>0){

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