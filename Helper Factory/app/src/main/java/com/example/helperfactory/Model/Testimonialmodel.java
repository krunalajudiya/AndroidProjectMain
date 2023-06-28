package com.example.helperfactory.Model;

public class Testimonialmodel {

    String t_name;
    String t_desc;
    int t_img;

    public Testimonialmodel(String t_name, String t_desc, int t_img) {
        this.t_name = t_name;
        this.t_desc = t_desc;
        this.t_img = t_img;
    }

    public String getT_name() {
        return t_name;
    }

    public String getT_desc() {
        return t_desc;
    }

    public int getT_img() {
        return t_img;
    }
}
