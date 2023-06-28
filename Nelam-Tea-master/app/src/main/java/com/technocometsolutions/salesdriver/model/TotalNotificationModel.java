package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalNotificationModel {

    @SerializedName("items")
    @Expose
    private ArrayList<NotificationItemModel> items = null;
    @SerializedName("total_notification")
    @Expose
    private String total_notification;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public ArrayList<NotificationItemModel> getItems() {
        return items;
    }

    public String getTotal_notification() {
        return total_notification;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
