package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Offermodel {

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
        return this.error;
    }

    public String getError_msg() {
        return this.error_msg;
    }

    public data getData() {
        return this.data1;
    }

    public class data {
        @SerializedName("offer")
        @Expose
        List<Offer> offerList;

        public List<Offer> getOfferList() {
            return offerList;
        }
    }
    public class Offer {
        @SerializedName("o_id")
        @Expose
        String o_id;
        @SerializedName("o_name")
        @Expose
        String o_name;
        @SerializedName("o_desc")
        @Expose
        String o_desc;
        @SerializedName("o_img")
        @Expose
        String o_img;


        public String getO_id() {
            return o_id;
        }

        public String getO_name() {
            return o_name;
        }

        public String getO_desc() {
            return o_desc;
        }

        public String getO_img() {
            return o_img;
        }
    }
}