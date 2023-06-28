package com.example.shreejicabs.Model;

public class Packagesmodel {

    String p_id;
    String packagename;
    String service;
    String city;
    String car_type;
    String rate;
    String comment;
    String communication;
    String driverid;
    String drivername;
    String drivernumber;



    public Packagesmodel(String p_id, String packagename, String service, String city, String car_type, String rate, String comment, String communication) {
        this.p_id = p_id;
        this.packagename = packagename;
        this.service = service;
        this.city = city;
        this.car_type = car_type;
        this.rate = rate;
        this.comment = comment;
        this.communication = communication;
    }

    public Packagesmodel(String p_id, String packagename, String service, String city, String car_type, String rate, String comment, String communication, String driverid,String drivername,String drivernumber) {
        this.p_id = p_id;
        this.packagename = packagename;
        this.service = service;
        this.city = city;
        this.car_type = car_type;
        this.rate = rate;
        this.comment = comment;
        this.communication = communication;
        this.driverid = driverid;
        this.drivername=drivername;
        this.drivernumber=drivernumber;
    }

    public String getP_id() {
        return p_id;
    }

    public String getPackagename() {
        return packagename;
    }

    public String getService() {
        return service;
    }

    public String getCity() {
        return city;
    }

    public String getCar_type() {
        return car_type;
    }

    public String getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public String getCommunication() {
        return communication;
    }

    public String getDriverid() {
        return driverid;
    }

    public String getDrivername() {
        return drivername;
    }

    public String getDrivernumber() {
        return drivernumber;
    }
}
