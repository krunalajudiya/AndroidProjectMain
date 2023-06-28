package com.technocometsolutions.salesdriver.utlity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.technocometsolutions.salesdriver.R;

public class ErrorView {

    Dialog dialog;
    public interface OnNoInternetConnectionListerner
    {
        public void onRetryButtonClicked();
        public void onCancelButtonClicked();
    }
    public ErrorView(Context context, OnNoInternetConnectionListerner onNoInternetConnectionListerner)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_layout_no_internet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnRetry=dialog.findViewById(R.id.btnRetry);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                onNoInternetConnectionListerner.onRetryButtonClicked();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                onNoInternetConnectionListerner.onCancelButtonClicked();
            }
        });

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
