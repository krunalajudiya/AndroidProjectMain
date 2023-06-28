package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Generatetokenmodel {

    @SerializedName("head")
    @Expose
    Head head;
    /*@SerializedName("body")
    @Expose
    Body body;*/

    public Head getHead() {
        return head;
    }

    /*public Body getBody() {
        return body;
    }*/

    public class Head
    {

        @SerializedName("CHECKSUMHASH")
        @Expose
        String signature;



        public String getSignature() {
            return signature;
        }
    }

    /*public class Body{

        @SerializedName("resultInfo")
        @Expose
        ResultInfo resultInfo;
        @SerializedName("txnToken")
        @Expose
        String txnToken;

        public ResultInfo getResultInfo() {
            return resultInfo;
        }

        public String getTxnToken() {
            return txnToken;
        }
    }*/
    public class ResultInfo{

        @SerializedName("resultCode")
        @Expose
        String resultCode;
        @SerializedName("resultMsg")
        @Expose
        String resultMsg;

        public String getResultCode() {
            return resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }
    }


}
