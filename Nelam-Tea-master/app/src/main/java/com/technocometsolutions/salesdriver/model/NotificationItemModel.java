package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationItemModel {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("attachments")
    @Expose
    private List<NotificationAttachmentItemModel> attachments=null;


    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationAttachmentItemModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<NotificationAttachmentItemModel> attachments) {
        this.attachments = attachments;
    }
}
