package com.example.helperfactory.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Slidermodel {

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

    public Slidermodel.data getData() {
        return this.data1;
    }

    public class data {
        @SerializedName("slider")
        @Expose
        List<Slider> slider;

        public List<Slider> getSlider() {
            return slider;
        }
    }
    public class Slider {
        @SerializedName("Slider_id")
        @Expose
        int slider_id;
        @SerializedName("photo")
        @Expose
        String photo;
        @SerializedName("Tital")
        @Expose
        String Tital;
        @SerializedName("Discription")
        @Expose
        String Discription;

        public int getSlider_id() {
            return slider_id;
        }

        public String getPhoto() {
            return photo;
        }

        public String getTital() {
            return Tital;
        }

        public String getDiscription() {
            return Discription;
        }
    }

}
