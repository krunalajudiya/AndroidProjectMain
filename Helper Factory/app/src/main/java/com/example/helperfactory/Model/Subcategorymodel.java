package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subcategorymodel {

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
        @SerializedName("category")
        @Expose
        List<SubCategory> subcategory;

        public List<SubCategory> getSubcategory() {
            return subcategory;
        }
    }
    public class SubCategory {
        @SerializedName("sub_id")
        @Expose
        String sub_id;
        @SerializedName("sub_name")
        @Expose
        String sub_name;
        @SerializedName("sub_img")
        @Expose
        String sub_img;
        @SerializedName("sub_des")
        @Expose
        String sub_des;
        @SerializedName("duration")
        @Expose
        String duration;
        @SerializedName("turnaroundtime")
        @Expose
        String turnaroundtime;
        @SerializedName("price")
        @Expose
        String price;
        @SerializedName("include")
        @Expose
        String include;
        @SerializedName("discount_price")
        @Expose
        String discount_price;

        public String getSub_id() {
            return sub_id;
        }

        public String getSub_name() {
            return sub_name;
        }

        public String getSub_img() {
            return sub_img;
        }

        public String getSub_des() {
            return sub_des;
        }

        public String getDuration() {
            return duration;
        }

        public String getTurnaroundtime() {
            return turnaroundtime;
        }

        public String getPrice() {
            return price;
        }

        public String getInclude() {
            return include;
        }

        public String getDiscount_price() {
            return discount_price;
        }
    }

}
