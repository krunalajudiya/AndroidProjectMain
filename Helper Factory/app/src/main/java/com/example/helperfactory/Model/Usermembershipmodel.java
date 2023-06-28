package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Usermembershipmodel {

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
        List<UserMembership> userMembershipList;

        public List<UserMembership> getUserMembershipList() {
            return userMembershipList;
        }
    }
    public class UserMembership {

        @SerializedName("m_discount")
        @Expose
        String m_discount;

        public String getM_discount() {
            return m_discount;
        }
    }
}
