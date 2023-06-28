package com.technocometsolutions.salesdriver.utlity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.technocometsolutions.salesdriver.R;

public class LoadingView {
    Dialog dialog;
    public LoadingView(Context context)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }
    public void showLoadingView()
    {
        dialog.show();
    }
    public void hideLoadingView()
    {
        dialog.dismiss();
    }
}
