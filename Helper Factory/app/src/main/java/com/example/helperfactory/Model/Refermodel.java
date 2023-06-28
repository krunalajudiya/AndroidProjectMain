package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Refermodel {

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    refer data;

    public int getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public refer getData() {
        return data;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public void setData(refer data) {
        this.data = data;
    }

    public class refer{
        @SerializedName("total_referral")
        @Expose
        private String total_referral;

        @SerializedName("referal_client")
        @Expose
        private String referal_client;

        public String getTotal_referral() {
            return total_referral;
        }

        public String getReferal_client() {
            return referal_client;
        }

        public void setTotal_referral(String total_referral) {
            this.total_referral = total_referral;
        }

        public void setReferal_client(String referal_client) {
            this.referal_client = referal_client;
        }
    }
}
