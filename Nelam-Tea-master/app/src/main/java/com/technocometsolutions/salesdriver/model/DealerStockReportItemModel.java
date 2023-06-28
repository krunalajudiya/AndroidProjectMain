package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealerStockReportItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("product")
    @Expose
    private List<DealerStockReportProductModel> product = null;

    private int aflag=0;

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

    public List<DealerStockReportProductModel> getProduct() {
        return product;
    }

    public void setProduct(List<DealerStockReportProductModel> product) {
        this.product = product;
    }

    public int getAflag() {
        return aflag;
    }

    public void setAflag(int aflag) {
        this.aflag = aflag;
    }
}
