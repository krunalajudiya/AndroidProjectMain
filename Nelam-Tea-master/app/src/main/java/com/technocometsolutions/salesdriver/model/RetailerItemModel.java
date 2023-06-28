package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetailerItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("retailer_name")
    @Expose
    private String retailer_name;
    @SerializedName("shope_name")
    @Expose
    private String shope_name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailer_name() {
        return retailer_name;
    }

    public void setRetailer_name(String retailer_name) {
        this.retailer_name = retailer_name;
    }

    public String getShope_name() {
        return shope_name;
    }

    public void setShope_name(String shope_name) {
        this.shope_name = shope_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
