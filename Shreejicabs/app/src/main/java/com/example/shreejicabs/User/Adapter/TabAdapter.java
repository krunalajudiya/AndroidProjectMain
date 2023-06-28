package com.example.shreejicabs.User.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.shreejicabs.User.Fragment.Avalible;

public class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totaltabas;

    public TabAdapter(@NonNull FragmentManager fm,Context context,int totaltabas) {
        super(fm);
        this.context=context;
        this.totaltabas=totaltabas;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Avalible();
            case 1:
//                return new Service();

        }

        return null;
    }

    @Override
    public int getCount() {
        return totaltabas;
    }
}
