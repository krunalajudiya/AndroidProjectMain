package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Membershipmodel {

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
        @SerializedName("membership")
        @Expose
        List<Membership> memberships;

        public List<Membership> getMemberships() {
            return memberships;
        }
    }
    public class Membership {
        @SerializedName("m_id")
        @Expose
        String m_id;
        @SerializedName("m_name")
        @Expose
        String m_name;
        @SerializedName("m_price")
        @Expose
        String m_price;
        @SerializedName("m_discount")
        @Expose
        String m_discount;
        @SerializedName("include_list")
        @Expose
        String include_list;
        @SerializedName("m_valid_upto")
        @Expose
        String m_valid_upto;

        public String getM_id() {
            return m_id;
        }

        public String getM_name() {
            return m_name;
        }

        public String getM_price() {
            return m_price;
        }

        public String getM_discount() {
            return m_discount;
        }

        public String getInclude_list() {
            return include_list;
        }

        public String getM_valid_upto() {
            return m_valid_upto;
        }
    }
}
