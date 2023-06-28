package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletePaymentItemModel {
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("employe")
    @Expose
    private String employe;
    @SerializedName("dealer")
    @Expose
    private String dealer;
    @SerializedName("order_no")
    @Expose
    private String order_no;
    @SerializedName("amount")
    @Expose
    private String amount;


    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
