package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesReportItemModel {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("empploye_name")
    @Expose
    private String empploye_name;
    @SerializedName("dealer_name")
    @Expose
    private String dealer_name;
    @SerializedName("total_price")
    @Expose
    private String total_price;
    @SerializedName("total_weight")
    @Expose
    private String total_weight;
    @SerializedName("products")
    @Expose
    private List<SalesReportProductItemModel> items=null;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmpploye_name() {
        return empploye_name;
    }

    public void setEmpploye_name(String empploye_name) {
        this.empploye_name = empploye_name;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }

    public List<SalesReportProductItemModel> getItems() {
        return items;
    }

    public void setItems(List<SalesReportProductItemModel> items) {
        this.items = items;
    }
}
