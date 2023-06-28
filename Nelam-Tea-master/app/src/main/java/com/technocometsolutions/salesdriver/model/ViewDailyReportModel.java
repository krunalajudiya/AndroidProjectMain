package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewDailyReportModel {

    @SerializedName("items")
    @Expose
    private ArrayList<DailyReportModel> items = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public ArrayList<DailyReportModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<DailyReportModel> items) {
        this.items = items;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
