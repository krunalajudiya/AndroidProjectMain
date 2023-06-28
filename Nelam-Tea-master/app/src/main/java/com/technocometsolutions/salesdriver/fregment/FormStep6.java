package com.technocometsolutions.salesdriver.fregment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.technocometsolutions.salesdriver.R;


public class FormStep6 extends Fragment implements Step , BlockingStep {

    EditText sqmtswall1,sqmtswall2,amountwall1,amountwall2,sqmtsdc1,sqmtsdc2,amountdc1,amountdc2,sqmtsgvtpgvt,amountgvtpgvt;
    RadioGroup durationwall1,durationwall2,durationdc1,durationdc2,durationgvtpgvt;
    RadioButton monthlywall1,quarterlywall1,yearlywall1,monthlywall2,quarterlywall2,yearlywall2,monthlydc1,quarterlydc1,yearlydc1
            ,monthlydc2,quarterlydc2,yearlydc2,monthlygvtpgvt,quarterlygvtpgvt,yearlygvtpgvt;

    String sqmtswall1_txt,sqmtswall2_txt,amountwall1_txt,amountwall2_txt,sqmtsdc1_txt,sqmtsdc2_txt,amountdc1_txt,amountdc2_txt
            ,sqmtsgvtpgvt_txt,amountgvtpgvt_txt,durationwall1_txt,durationwall2_txt,durationdc1_txt,durationdc2_txt,durationgvtpgvt_txt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form_step6, container, false);
        sqmtswall1=view.findViewById(R.id.sqmtswall1);
        sqmtswall2=view.findViewById(R.id.sqmtswall2);
        sqmtsdc1=view.findViewById(R.id.sqmtswall2);
        sqmtsdc2=view.findViewById(R.id.sqmtswall2);
        sqmtsgvtpgvt=view.findViewById(R.id.sqmtswall2);
        amountwall1=view.findViewById(R.id.amountwall1);
        amountwall2=view.findViewById(R.id.amountwall2);
        amountdc1=view.findViewById(R.id.amountdc1);
        amountdc2=view.findViewById(R.id.amountdc2);
        amountgvtpgvt=view.findViewById(R.id.amountgvtpgvt);
        durationwall1=view.findViewById(R.id.durationwall1);
        durationwall2=view.findViewById(R.id.durationwall2);
        durationdc1=view.findViewById(R.id.durationdc1);
        durationdc2=view.findViewById(R.id.durationdc2);
        durationgvtpgvt=view.findViewById(R.id.durationgvtpgvt);





        return view;
    }

    void getalldata(){
        if (sqmtswall1.getText().toString().trim().length()>0 && sqmtswall2.getText().toString().trim().length()>0
                && sqmtsdc1.getText().toString().trim().length()>0 && sqmtsdc2.getText().toString().trim().length()>0
                && sqmtsdc1.getText().toString().trim().length()>0 &&sqmtsdc2.getText().toString().trim().length()>0
                && sqmtsgvtpgvt.getText().toString().trim().length()>0 && amountwall1.getText().toString().trim().length()>0
                && amountwall2.getText().toString().trim().length()>0 && amountdc1.getText().toString().trim().length()>0
                && amountdc2.getText().toString().trim().length()>0&& amountgvtpgvt.getText().toString().trim().length()>0
                && durationwall1.getCheckedRadioButtonId()!=-1&& durationwall2.getCheckedRadioButtonId()!=-1
                && durationdc1.getCheckedRadioButtonId()!=-1&& durationdc2.getCheckedRadioButtonId()!=-1
                && durationgvtpgvt.getCheckedRadioButtonId()!=-1){

            sqmtswall1_txt=sqmtswall1.getText().toString().trim();
            sqmtswall2_txt=sqmtswall2.getText().toString().trim();
            sqmtsdc1_txt=sqmtsdc1.getText().toString().trim();
            sqmtsdc2_txt=sqmtsdc2.getText().toString().trim();
            sqmtsgvtpgvt_txt=sqmtsgvtpgvt.getText().toString().trim();
            amountwall1_txt=amountwall1.getText().toString().trim();
            amountwall2_txt=amountwall2.getText().toString().trim();
            amountdc1_txt=amountdc1.getText().toString().trim();
            amountdc2_txt=amountdc2.getText().toString().trim();
            amountgvtpgvt_txt=amountgvtpgvt.getText().toString().trim();

            if (monthlywall1.isChecked()){
                durationwall1_txt="monthly";
            }
            if (monthlywall2.isChecked()){
                durationwall2_txt="monthly";
            }
            if (monthlydc1.isChecked()){
                durationdc1_txt="monthly";
            }
            if (monthlydc2.isChecked()){
                durationdc2_txt="monthly";
            }
            if (monthlygvtpgvt.isChecked()){
                durationgvtpgvt_txt="monthly";
            }
            if (quarterlywall1.isChecked()){
                durationwall1_txt="qurterly";
            }
            if (quarterlywall2.isChecked()){
                durationwall2_txt="qurterly";
            }
            if (quarterlydc1.isChecked()){
                durationdc1_txt="qurterly";
            }
            if (quarterlydc2.isChecked()){
                durationdc2_txt="qurterly";
            }
            if (quarterlygvtpgvt.isChecked()){
                durationgvtpgvt_txt="qurterly";
            }
            if (yearlywall1.isChecked()){
                durationwall1_txt="yearly";
            }
            if (yearlywall2.isChecked()){
                durationwall2_txt="yearly";
            }
            if (yearlydc1.isChecked()){
                durationdc1_txt="yearly";
            }
            if (yearlydc2.isChecked()){
                durationdc2_txt="yearly";
            }
            if (yearlygvtpgvt.isChecked()){
                durationgvtpgvt_txt="yearly";
            }
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