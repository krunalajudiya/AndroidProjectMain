package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ongoingmodel {

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    private Ongoingmodel.data data1;

    public int getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public Ongoingmodel.data getData1() {
        return data1;
    }

    public class data {
        @SerializedName("booking")
        @Expose
        List<Ongoing_data> ongoing_data;

        public List<Ongoing_data> getOngoing_data() {
            return ongoing_data;
        }
    }

    public class  Ongoing_data{
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
        String sub_cat_id;
        @SerializedName("date")
        @Expose
        String date;
        @SerializedName("time")
        @Expose
        String time;
        @SerializedName("vendor_detail")
        @Expose
        List<Vendor_detail> vendor_detail;

        @SerializedName("bill_pdf")
        @Expose
        List<Invoice_detail> bill_pdf;

        @SerializedName("Sub_name")
        @Expose
        String sub_cat_name;
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

        public String getSub_cat_id() {
            return sub_cat_id;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public List<Vendor_detail> getVendor_detail() {
            return vendor_detail;
        }

        public List<Invoice_detail> getBill_pdf() {
            return bill_pdf;
        }

        public String getSub_cat_name() {
            return sub_cat_name;
        }

        public String getStatus() {
            return Status;
        }


    }
    public class Vendor_detail{
        @SerializedName("vendor_name")
        @Expose
        String vendor_name;
        @SerializedName("mobile")
        @Expose
        String mobile;
        @SerializedName("vendor_id")
        @Expose
        String vendor_id;


        public String getVendor_name() {
            return vendor_name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getVendor_id(){return vendor_id;}
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
