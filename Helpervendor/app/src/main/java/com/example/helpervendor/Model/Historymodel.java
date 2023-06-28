package com.example.helpervendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Historymodel {

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
        List<History> histories;

        public List<History> getHistories() {
            return histories;
        }
    }
    public class History {
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
        @SerializedName("Sub_name")
        @Expose
        String Sub_name;
        @SerializedName("Price")
        @Expose
        String Price;
        @SerializedName("client_name")
        @Expose
        String client_name;
        @SerializedName("mobile")
        @Expose
        String mobile;
        @SerializedName("Address")
        @Expose
        String Address;
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

        public String getSub_id() {
            return sub_id;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getSub_name() {
            return Sub_name;
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

        public String getBill_pdf() {
            return bill_pdf;
        }
    }

}
