package com.example.helpervendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Vendordetailmodel {

        @SerializedName("error")
        @Expose
        String error;

        @SerializedName("error_msg")
        @Expose
        String error_msg;

        @SerializedName("data")
        @Expose
        ArrayList<Data> data;







        public String getError() {
            return error;
        }

        public String getError_msg() {
            return error_msg;
        }

    public List<Data> getData() {
        return data;
    }

    public class Data{

        @SerializedName("vendor_id")
        @Expose
        String vendor_id;
        @SerializedName("vendor_name")
        @Expose
        String vendor_name;
        @SerializedName("photo")
        @Expose
        String photo;
        @SerializedName("mobile")
        @Expose
        String mobile;
        @SerializedName("address")
        @Expose
        String address;
        @SerializedName("city")
        @Expose
        String city;
        @SerializedName("pincode")
        @Expose
        String pincode;
        @SerializedName("visiting_charge")
        @Expose
        String visiting_charge;
        @SerializedName("id_proof")
        @Expose
        String id_proof;
        @SerializedName("Status")
        @Expose
        String status;
        @SerializedName("service_status")
        @Expose
        String service_status;
        @SerializedName("sub_cat")
        @Expose
        String sub_cat;


        public String getVendor_id() {
            return vendor_id;
        }

        public void setVendor_id(String vendor_id) {
            this.vendor_id = vendor_id;
        }

        public String getVendor_name() {
            return vendor_name;
        }

        public void setVendor_name(String vendor_name) {
            this.vendor_name = vendor_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getVisiting_charge() {
            return visiting_charge;
        }

        public void setVisiting_charge(String visiting_charge) {
            this.visiting_charge = visiting_charge;
        }

        public String getId_proof() {
            return id_proof;
        }

        public void setId_proof(String id_proof) {
            this.id_proof = id_proof;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getService_status() {
            return service_status;
        }

        public void setService_status(String service_status) {
            this.service_status = service_status;
        }

        public String getSub_cat() {
            return sub_cat;
        }

        public void setSub_cat(String sub_cat) {
            this.sub_cat = sub_cat;
        }

    }

}
