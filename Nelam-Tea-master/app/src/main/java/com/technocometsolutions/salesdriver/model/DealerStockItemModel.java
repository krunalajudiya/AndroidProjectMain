package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerStockItemModel {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("dealer_stock")
    @Expose
    private String dealerStock;
    @SerializedName("retailer_stock")
    @Expose
    private String retailerStock;
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
}
