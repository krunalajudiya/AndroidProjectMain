package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviewmodel {
    @SerializedName("error")
    @Expose
    String error;

    @SerializedName("error_msg")
    @Expose
    String error_msg;
    private Data data;
    public String getError() {
        return error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public  Reviewmodel.Data getData(){return this.data;}

    public class Data{

        @SerializedName("review")
        @Expose
        List<Review> reviewList;

        public List<Review> getReviewList() {
            return reviewList;
        }
    }
   public class Review{
        @SerializedName("r_id")
        @Expose
        String r_id;
        @SerializedName("s_id")
        @Expose
        String s_id;
        @SerializedName("Sub_name")
        @Expose
        String Sub_name;
        @SerializedName("Sub_image")
        @Expose
        String Sub_image;
        @SerializedName("u_id")
        @Expose
        String u_id;
        @SerializedName("msg")
        @Expose
        String msg;
        @SerializedName("Client_name")
        @Expose
        String Client_name;
        @SerializedName("rating")
        @Expose
        String rating;


        public String getR_id() {
            return r_id;
        }

        public String getS_id() {
            return s_id;
        }

        public String getU_id() {
            return u_id;
        }

        public String getMsg() {
            return msg;
        }

        public String getClient_name() {
            return Client_name;
        }

        public String getRating() {
            return rating;
        }

        public String getSub_name() {
            return Sub_name;
        }

        public String getSub_image() {
            return Sub_image;
        }
    }
}
