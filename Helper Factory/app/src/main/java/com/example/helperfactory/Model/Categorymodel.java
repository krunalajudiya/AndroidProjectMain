package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Categorymodel {

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    private Categorymodel.data data1;


    public int getError() {
        return this.error;
    }

    public String getError_msg() {
        return this.error_msg;
    }

    public Categorymodel.data getData() {
        return this.data1;
    }

    public class data {
        @SerializedName("category")
        @Expose
        List<Categorymodel.Category> category;

        public List<Categorymodel.Category> getCategory() {
            return category;
        }
    }
    public class Category {
        @SerializedName("c_id")
        @Expose
        String c_id;
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
    }
}
