package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Historymodel {
    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    private data data1;

    public int getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public data getData1() {
        return data1;
    }

    public class data {
        @SerializedName("booking")
        @Expose
        List<History_data> history_data;

        public List<History_data> getHistory_data() {
            return history_data;
        }
    }

    public class  History_data{
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
        @SerializedName("Sub_name")
        @Expose
        String sub_cat_name;
        @SerializedName("Status")
        @Expose
        String Status;
        @SerializedName("bill_pdf")
        @Expose
        String bill_pdf;

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

        public String getSub_cat_name() {
            return sub_cat_name;
        }

        public String getStatus() {
            return Status;
        }
        public String getBill_pdf() {
            return bill_pdf;
        }


    }
    public class Vendor_detail{
        @SerializedName("vendor_name")
        @Expose
        String vendor_name;
        @SerializedName("mobile")
        @Expose
        String mobile;

        public String getVendor_name() {
            return vendor_name;
        }

        public String getMobile() {
            return mobile;
        }
    }
}
