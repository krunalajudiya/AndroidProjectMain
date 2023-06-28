package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Insertresultmodel {

    @SerializedName("error")
    @Expose
    String error;

    @SerializedName("error_msg")
    @Expose
    String error_msg;

    public String getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }
}
