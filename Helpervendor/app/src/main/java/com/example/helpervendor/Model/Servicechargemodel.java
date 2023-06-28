package com.example.helpervendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Servicechargemodel {
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
        @SerializedName("charges")
        @Expose
        List<Servicecharge> servicecharges;

        public List<Servicecharge> getServicecharges() {
            return servicecharges;
        }
    }
    public class Servicecharge {
        @SerializedName("c_id")
        @Expose
        String s_id;


        @SerializedName("s_charge")
        @Expose
        String s_charge;

        public String getS_id() {
            return s_id;
        }

        public String getS_charge() {
            return s_charge;
        }
    }
}

