package com.example.helpervendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Categorymodel {

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
        @SerializedName("category")
        @Expose
        List<Category> category;

        public List<Category> getCategory() {
            return category;
        }
    }
    public class Category {
        @SerializedName("c_id")
        @Expose
        String c_id;

        @SerializedName("subcat")
        @Expose
        List<Subcategory> subcategory;

        @SerializedName("c_name")
        @Expose
        String c_name;
        @SerializedName("c_img")
        @Expose
        String c_img;
        @SerializedName("description")
        @Expose
        String description;
        @SerializedName("offer")
        @Expose
        String offer;
        @SerializedName("status")
        @Expose
        String status;



        public String getC_id() {
            return c_id;
        }

        public String getC_name() {
            return c_name;
        }

        public String getC_img() {
            return c_img;
        }

        public String getDescription() {
            return description;
        }

        public String getOffer() {
            return offer;
        }

        public String getStatus() {
            return status;
        }

        public List<Subcategory> getSubcategory() {
            return subcategory;
        }
    }

    public class Subcategory
    {
        @SerializedName("Sub_id")
        @Expose
        private String sub_id;

        @SerializedName("Cat_id")
        @Expose
        private String cat_id;

        @SerializedName("Sub_name")
        @Expose
        private String sub_name;

        public String getSub_id() {
            return sub_id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public String getSub_name() {
            return sub_name;
        }
    }
}
