package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDelalerListModel {
    @SerializedName("dealer_id")
    @Expose
    private String dealer_id;
    @SerializedName("dealer_name")
    @Expose
    private String dealer_name;
    @SerializedName("order_id")
    @Expose
    private String order_id;
    @SerializedName("total_price")
    @Expose
    private String total_price;
    @SerializedName("total_weight")
    @Expose
    private String total_weight;
    @SerializedName("products")
    @Expose
    private List<ProductListModel> productList=null;


    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public List<ProductListModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListModel> productList) {
        this.productList = productList;
    }
}
