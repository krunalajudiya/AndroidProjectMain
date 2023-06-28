package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockReportNewItemModel {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("dealer_kg")
    @Expose
    private String dealerKg;
    @SerializedName("retailer_kg")
    @Expose
    private String retailerKg;
    @SerializedName("total_dealer_bag")
    @Expose
    private String totalDealerBag;
    @SerializedName("different")
    @Expose
    private String different;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDealerKg() {
        return dealerKg;
    }

    public void setDealerKg(String dealerKg) {
        this.dealerKg = dealerKg;
    }

    public String getRetailerKg() {
        return retailerKg;
    }

    public void setRetailerKg(String retailerKg) {
        this.retailerKg = retailerKg;
    }

    public String getTotalDealerBag() {
        return totalDealerBag;
    }

    public void setTotalDealerBag(String totalDealerBag) {
        this.totalDealerBag = totalDealerBag;
    }

    public String getDifferent() {
        return different;
    }

    public void setDifferent(String different) {
        this.different = different;
    }
}
