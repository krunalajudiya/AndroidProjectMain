package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCatModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("product_code")
    @Expose
    private String productCode;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("strip_items")
    @Expose
    private String stripItems;
    @SerializedName("total_strips")
    @Expose
    private String totalStrips;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("minteger")
    @Expose
    private int minteger = 0;
    private double total_qty = 0;
    private double total_price = 0;
    private double total_kg = 0;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStripItems() {
        return stripItems;
    }

    public void setStripItems(String stripItems) {
        this.stripItems = stripItems;
    }

    public String getTotalStrips() {
        return totalStrips;
    }

    public void setTotalStrips(String totalStrips) {
        this.totalStrips = totalStrips;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getMinteger() {
        return minteger;
    }

    public void setMinteger(int minteger) {
        this.minteger = minteger;
    }


    public double getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(double total_qty) {
        this.total_qty = total_qty;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getTotal_kg() {
        return total_kg;
    }

    public void setTotal_kg(double total_kg) {
        this.total_kg = total_kg;
    }
}
