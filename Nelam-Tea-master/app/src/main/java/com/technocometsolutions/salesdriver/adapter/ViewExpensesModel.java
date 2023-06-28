package com.technocometsolutions.salesdriver.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.technocometsolutions.salesdriver.model.ExpensesModel;

import java.util.ArrayList;

public class ViewExpensesModel {

    @SerializedName("items")
    @Expose
    private ArrayList<ExpensesModel> items = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public ArrayList<ExpensesModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExpensesModel> items) {
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
