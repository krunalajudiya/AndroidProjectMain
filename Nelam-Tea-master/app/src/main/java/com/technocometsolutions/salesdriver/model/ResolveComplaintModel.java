package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResolveComplaintModel {
    @SerializedName("items")
    @Expose
    private List<ResolveComplaintItemModel> items = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ResolveComplaintItemModel> getItems() {
        return items;
    }

    public void setItems(List<ResolveComplaintItemModel> items) {
        this.items = items;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
