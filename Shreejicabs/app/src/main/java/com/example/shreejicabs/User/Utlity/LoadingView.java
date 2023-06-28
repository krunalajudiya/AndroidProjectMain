package com.example.shreejicabs.User.Utlity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.example.shreejicabs.R;

public class LoadingView {

    Dialog dialog;

    public LoadingView(Context context){

        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loadingview_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
    }
    public void ShowLoadingView(){
        dialog.show();
    }
    public void HideLoadingView(){
        dialog.dismiss();
    }
}
