package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCatModel1 {
    @SerializedName("id")
    @Expose
    private String id;
    /*@SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("product_code")
    @Expose
    private String productCode;*/
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("product_photo")
    @Expose
    private String product_photo;


    /*@SerializedName("strip_items")
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
            private int minteger = 0;*/
    private int total_qty = 0;
    private int total_price = 0;
    private int total_kg = 0;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }*/

   /* public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }*/

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_photo() {
        return product_photo;
    }

    public void setProduct_photo(String product_photo) {
        this.product_photo = product_photo;
    }

    /*public String getStripItems() {
        return stripItems;
    }

    public void setStripItems(String stripItems) {
        this.stripItems = stripItems;
    }*/

    /*public String getTotalStrips() {
        return totalStrips;
    }

    public void setTotalStrips(String totalStrips) {
        this.totalStrips = totalStrips;
    }*/

    /*public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }*/

    /*public int getMinteger() {
        return minteger;
    }

    public void setMinteger(int minteger) {
        this.minteger = minteger;
    }*/

    public int getTotal_qty() {
        return total_qty;
    }

    public int getTotal_price() {
        return total_price;
    }

    public int getTotal_kg() {
        return total_kg;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public void setTotal_kg(int total_kg) {
        this.total_kg = total_kg;
    }
}
