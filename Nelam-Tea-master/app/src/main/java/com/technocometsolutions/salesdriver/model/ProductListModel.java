package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("category_id")
    @Expose
    private String category_id;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("original_price")
    @Expose
    private String original_price;
    @SerializedName("total_price")
    @Expose
    private String total_price;
    @SerializedName("original_weight")
    @Expose
    private String original_weight;
    @SerializedName("total_weight")
    @Expose
    private String total_weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getOriginal_weight() {
        return original_weight;
    }

    public void setOriginal_weight(String original_weight) {
        this.original_weight = original_weight;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }
}
