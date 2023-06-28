package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    private int aflag=0;
    @SerializedName("products")
    @Expose
    private List<ProductCatModel> products = null;


    public CategoryItemModel(String category, List<ProductCatModel> products) {
        super();
        this.category = category;
        this.products = products;
    }

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

    public List<ProductCatModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCatModel> products) {
        this.products = products;
    }

    public int getAflag() {
        return aflag;
    }

    public void setAflag(int aflag) {
        this.aflag = aflag;
    }
}
