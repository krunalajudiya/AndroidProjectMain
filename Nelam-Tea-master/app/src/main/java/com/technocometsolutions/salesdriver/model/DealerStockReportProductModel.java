package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerStockReportProductModel {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("dealer_kg")
    @Expose
    private String dealerStock;
    @SerializedName("retailer_kg")
    @Expose
    private String retailerStock;

    @SerializedName("total_dealer_bag")
    @Expose
    private String total_dealer_bag;
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

    public String getDealerStock() {
        return dealerStock;
    }

    public void setDealerStock(String dealerStock) {
        this.dealerStock = dealerStock;
    }

    public String getRetailerStock() {
        return retailerStock;
    }

    public void setRetailerStock(String retailerStock) {
        this.retailerStock = retailerStock;
    }

    public String getDifferent() {
        return different;
    }

    public void setDifferent(String different) {
        this.different = different;
    }

    public String getTotal_dealer_bag() {
        return total_dealer_bag;
    }

    public void setTotal_dealer_bag(String total_dealer_bag) {
        this.total_dealer_bag = total_dealer_bag;
    }
}
