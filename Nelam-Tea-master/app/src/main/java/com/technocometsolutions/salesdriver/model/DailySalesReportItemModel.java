package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailySalesReportItemModel {


    @SerializedName("pdf")
    @Expose
    private String pdf;

    public DailySalesReportItemModel(String pdf) {
        this.pdf = pdf;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
