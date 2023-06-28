package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyReportModel {

    @SerializedName("d_id")
    @Expose
    private String d_id;
    @SerializedName("emp_id")
    @Expose
    private String emp_id;
    @SerializedName("d_date")
    @Expose
    private String d_date;
    @SerializedName("client_status")
    @Expose
    private String client_status;
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("person_name")
    @Expose
    private String person_name;
    @SerializedName("contact_number")
    @Expose
    private String contact_number;
    @SerializedName("city_name")
    @Expose
    private String city_name;
    @SerializedName("in_product")
    @Expose
    private String in_product;
    @SerializedName("meeting_reason")
    @Expose
    private String meeting_reason;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("visiting_card")
    @Expose
    private String visiting_card;

    public String getD_id() {
        return d_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getD_date() {
        return d_date;
    }

    public String getClient_status() {
        return client_status;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getPerson_name() {
        return person_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getIn_product() {
        return in_product;
    }

    public String getMeeting_reason() {
        return meeting_reason;
    }

    public String getRemark() {
        return remark;
    }

    public String getVisiting_card() {
        return visiting_card;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public void setClient_status(String client_status) {
        this.client_status = client_status;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void setIn_product(String in_product) {
        this.in_product = in_product;
    }

    public void setMeeting_reason(String meeting_reason) {
        this.meeting_reason = meeting_reason;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setVisiting_card(String visiting_card) {
        this.visiting_card = visiting_card;
    }
}
