package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.technocometsolutions.salesdriver.fregment.FormStep0;
import com.technocometsolutions.salesdriver.fregment.FormStep1;
import com.technocometsolutions.salesdriver.fregment.FormStep2;
import com.technocometsolutions.salesdriver.fregment.FormStep3;
import com.technocometsolutions.salesdriver.fregment.FormStep4;
import com.technocometsolutions.salesdriver.fregment.FormStep5;
import com.technocometsolutions.salesdriver.fregment.FormStep6;

public class StepAdapter extends AbstractFragmentStepAdapter {

    public StepAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

//        Bundle b = new Bundle();
//        b.putInt(CURRENT_STEP_POSITION_KEY, position);
//        step.setArguments(b);
        switch(position){
            case 0:
                return new FormStep0();
            case 1:
                return new FormStep1();
            case 2:
                Fragment step2=new FormStep2();
                Bundle step1=new Bundle();
                step1.putString("step1data","send data");
                step2.setArguments(step1);
                return (Step) step2;
            case 3:
                return new FormStep3();
            case 4:
                return new FormStep4();
            case 5:
                return new FormStep5();
            case 6:
                return new FormStep6();
            default:
                return new FormStep1();
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        return super.getViewModel(position);
    }
}
