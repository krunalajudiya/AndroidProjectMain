package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpensesModel {

    @SerializedName("e_id")
    @Expose
    private String e_id;
    @SerializedName("emp_id")
    @Expose
    private String emp_id;
    @SerializedName("e_date")
    @Expose
    private String e_date;
    @SerializedName("e_from")
    @Expose
    private String e_from;
    @SerializedName("e_to")
    @Expose
    private String e_to;
    @SerializedName("hotel_stay")
    @Expose
    private String hotel_stay;
    @SerializedName("t_id")
    @Expose
    private String t_id;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("toll_charge")
    @Expose
    private String toll_charge;
    @SerializedName("breakfast_charge")
    @Expose
    private String breakfast_charge;
    @SerializedName("lunch_charge")
    @Expose
    private String lunch_charge;
    @SerializedName("dinner_charge")
    @Expose
    private String dinner_charge;
    @SerializedName("da")
    @Expose
    private String da;
    @SerializedName("mis_charge")
    @Expose
    private String mis_charge;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("bill")
    @Expose
    private String bill;

    public String getE_id() {
        return e_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getE_date() {
        return e_date;
    }

    public String getE_from() {
        return e_from;
    }

    public String getE_to() {
        return e_to;
    }

    public String getHotel_stay() {
        return hotel_stay;
    }


    public String getT_id() {
        return t_id;
    }

    public String getDistance() {
        return distance;
    }

    public String getToll_charge() {
        return toll_charge;
    }

    public String getBreakfast_charge() {
        return breakfast_charge;
    }

    public String getLunch_charge() {
        return lunch_charge;
    }

    public String getDinner_charge() {
        return dinner_charge;
    }

    public String getDa() {
        return da;
    }

    public String getMis_charge() {
        return mis_charge;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getBill() {
        return bill;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public void setE_date(String e_date) {
        this.e_date = e_date;
    }

    public void setE_from(String e_from) {
        this.e_from = e_from;
    }

    public void setE_to(String e_to) {
        this.e_to = e_to;
    }

    public void setHotel_stay(String hotel_stay) {
        this.hotel_stay = hotel_stay;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setToll_charge(String toll_charge) {
        this.toll_charge = toll_charge;
    }

    public void setBreakfast_charge(String breakfast_charge) {
        this.breakfast_charge = breakfast_charge;
    }

    public void setLunch_charge(String lunch_charge) {
        this.lunch_charge = lunch_charge;
    }

    public void setDinner_charge(String dinner_charge) {
        this.dinner_charge = dinner_charge;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public void setMis_charge(String mis_charge) {
        this.mis_charge = mis_charge;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }
}
