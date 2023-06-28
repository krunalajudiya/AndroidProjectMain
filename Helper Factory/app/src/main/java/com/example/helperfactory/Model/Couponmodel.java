package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Couponmodel {

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    coupon data;

    public int getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public coupon getData() {
        return data;
    }



    public class coupon{

        @SerializedName("coupon_code")
        @Expose
        private String coupon_code;

        @SerializedName("coupon_amount")
        @Expose
        private  String coupon_amount;

        @SerializedName("expire_date")
        @Expose
        private String expire_date;

        public String getCoupon_code() {
            return coupon_code;
        }

        public String getCoupon_amount() {
            return coupon_amount;
        }

        public String getExpire_date() {
            return expire_date;
        }
    }
}
