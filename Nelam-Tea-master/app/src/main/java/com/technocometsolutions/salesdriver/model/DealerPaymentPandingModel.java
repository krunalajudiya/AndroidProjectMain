package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealerPaymentPandingModel {
    @SerializedName("items")
    @Expose
    private List<DealerPaymentPandingItemModel> items = null;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DealerPaymentPandingItemModel> getItems() {
        return items;
    }

    public void setItems(List<DealerPaymentPandingItemModel> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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
