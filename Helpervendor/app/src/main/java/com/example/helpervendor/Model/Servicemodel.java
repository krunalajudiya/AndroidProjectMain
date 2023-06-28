package com.example.helpervendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Servicemodel {

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("error_msg")
    @Expose
    private String error_msg;

    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;


    public int getError() {
        return error;
    }

    public String getError_msg() {
        return this.error_msg;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("sub_id")
        @Expose
        public String category;

        public String getCategory() {
            return category;
        }
    }

}
