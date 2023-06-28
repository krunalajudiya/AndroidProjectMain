package com.technocometsolutions.salesdriver.model;

public class AddAttendanceMangementModel {
    public String in_out;
    public String in_out_time;
    public String in_out_name;


    public AddAttendanceMangementModel() {

    }

    public AddAttendanceMangementModel(String in_out, String in_out_time, String in_out_name) {
        this.in_out = in_out;
        this.in_out_time = in_out_time;
        this.in_out_name = in_out_name;
    }

    public String getIn_out() {
        return in_out;
    }

    public void setIn_out(String in_out) {
        this.in_out = in_out;
    }

    public String getIn_out_time() {
        return in_out_time;
    }

    public void setIn_out_time(String in_out_time) {
        this.in_out_time = in_out_time;
    }

    public String getIn_out_name() {
        return in_out_name;
    }

    public void setIn_out_name(String in_out_name) {
        this.in_out_name = in_out_name;
    }
}
