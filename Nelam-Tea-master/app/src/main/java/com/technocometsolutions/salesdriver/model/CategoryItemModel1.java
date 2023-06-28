package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryItemModel1 {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    private int aflag=0;
    @SerializedName("products")
    @Expose
    private List<ProductCatModel1> products = null;


    public CategoryItemModel1(String category, List<ProductCatModel1> products) {
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

    public List<ProductCatModel1> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCatModel1> products) {
        this.products = products;
    }

    public int getAflag() {
        return aflag;
    }

    public void setAflag(int aflag) {
        this.aflag = aflag;
    }
}
