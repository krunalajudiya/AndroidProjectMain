package com.example.helpervendor.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.helpervendor.Constant.Constant;

public class ConnectionReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkChangeReceiver";
    private OnCheckListener onCheckListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras()!=null)
        {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
            {
                onCheckListener.onCheck(true);
                Log.v(LOG_TAG, "Now you are connected to Internet!");
                Constant.isNetworkConnected = true;
            }else{
                onCheckListener.onCheck(false);
                Log.v(LOG_TAG, "Now you are not connected to Internet!");
                Constant.isNetworkConnected = false;
            }


        }
    }
    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }
    public interface OnCheckListener {
        void onCheck(Boolean value);
    }

}