package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resultmodel {

    @SerializedName("error")
    @Expose
    String error;

    @SerializedName("error_msg")
    @Expose
    String error_msg;

    @SerializedName("data")
    @Expose
    Data data;



    public String getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        @SerializedName("Client_id")
        @Expose
        String client_id;
        @SerializedName("Client_name")
        @Expose
        String client_name;
        @SerializedName("Mobile")
        @Expose
        String Mobile;
        @SerializedName("Photo")
        @Expose
        String photo;
        @SerializedName("Address")
        @Expose
        String address;
        @SerializedName("City")
        @Expose
        String city;
        @SerializedName("Pincode")
        @Expose
        String pincode;

        @SerializedName("referral_code")
        @Expose
        String referral_code;

        @SerializedName("membership_status")
        @Expose
        String membership_status;

        public String getClient_id() {
            return client_id;
        }

        public String getClient_name() {
            return client_name;
        }

        public String getMobile() {
            return Mobile;
        }

        public String getPhoto() {
            return photo;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        public String getReferral_code() {
            return referral_code;
        }

        public String getMembership_status() {
            return membership_status;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public void setClient_name(String client_name) {
            this.client_name = client_name;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public void setReferral_code(String referral_code) {
            this.referral_code = referral_code;
        }

        public void setMembership_status(String membership_status) {
            this.membership_status = membership_status;
        }
    }
}
