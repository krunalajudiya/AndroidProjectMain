package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesReportProductItemModel {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total_price")
    @Expose
    private String total_price;
    @SerializedName("total_weight")
    @Expose
    private String total_weight;


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
}
