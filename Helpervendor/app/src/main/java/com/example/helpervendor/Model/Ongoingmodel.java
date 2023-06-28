package com.example.helpervendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ongoingmodel {

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    private data data1;


    public String getError() {
        return this.error;
    }

    public String getError_msg() {
        return this.error_msg;
    }

    public data getData() {
        return this.data1;
    }

    public class data {
        @SerializedName("booking")
        @Expose
        List<Ongoing> ongoings;

        public List<Ongoing> getOngoings() {
            return ongoings;
        }
    }
    public class Ongoing {
        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("client_id")
        @Expose
        String client_id;
        @SerializedName("cat_id")
        @Expose
        String cat_id;
        @SerializedName("sub_id")
        @Expose
        String sub_id;
        @SerializedName("date")
        @Expose
        String date;
        @SerializedName("time")
        @Expose
        String time;

        @SerializedName("issue_photo")
        @Expose
        String issue_photo;

        @SerializedName("Sub_name")
        @Expose
        String Sub_name;
        @SerializedName("bill_pdf")
        @Expose
        List<Invoice_detail> bill_pdf;
        @SerializedName("Price")
        @Expose
        String Price;
        @SerializedName("coupon_amount")
        @Expose
        String coupon_amount;
        @SerializedName("client_name")
        @Expose
        String client_name;
        @SerializedName("mobile")
        @Expose
        String mobile;
        @SerializedName("Address")
        @Expose
        String Address;
        @SerializedName("Status")
        @Expose
        String Status;

        public String getId() {
            return id;
        }

        public String getClient_id() {
            return client_id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public String getSub_id() {
            return sub_id;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getIssue_photo() {
            return issue_photo;
        }

        public String getSub_name() {
            return Sub_name;
        }

        public List<Invoice_detail> getBill_pdf() {
            return bill_pdf;
        }

        public String getPrice() {
            return Price;
        }

        public String getClient_name() {
            return client_name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAddress() {
            return Address;
        }

        public String getStatus() {
            return Status;
        }

        public String getCoupon_amount() {
            return coupon_amount;
        }

    }
    public class Invoice_detail{
        @SerializedName("bill_pdf")
        @Expose
        String bill_pdf;
        @SerializedName("total_charges")
        @Expose
        String total_charges;

        public String getBill_pdf() {
            return bill_pdf;
        }

        public String getTotal_charges() {
            return total_charges;
        }
    }
}
