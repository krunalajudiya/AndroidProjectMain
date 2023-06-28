package com.technocometsolutions.salesdriver.model;

public class ViewAttendanceModel {
    String in_time;
    String out_time;
    String in_out_date;
    String in_out_a_p_w;

    public ViewAttendanceModel() {
    }

    public ViewAttendanceModel(String in_time, String out_time, String in_out_date, String in_out_a_p_w) {
        this.in_time = in_time;
        this.out_time = out_time;
        this.in_out_date = in_out_date;
        this.in_out_a_p_w = in_out_a_p_w;
    }


    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getIn_out_date() {
        return in_out_date;
    }

    public void setIn_out_date(String in_out_date) {
        this.in_out_date = in_out_date;
    }

    public String getIn_out_a_p_w() {
        return in_out_a_p_w;
    }

    public void setIn_out_a_p_w(String in_out_a_p_w) {
        this.in_out_a_p_w = in_out_a_p_w;
    }
}
