package com.example.shreejicabs.Model;

public class Avaliabilitymodel {
    String id;
    String from;
    String to;
    String date;
    String time;
    String cartype;
    String service;
    String comment;
    String communication;
    String driverid;
    String drivername;
    String drivernumber;
    String status;
    String Create_at;

    public Avaliabilitymodel(String id,String from, String to, String date,String time, String cartype, String service, String comment, String communication,String status) {
        this.id=id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time=time;
        this.cartype = cartype;
        this.service = service;
        this.comment = comment;
        this.communication = communication;
        this.status=status;
    }

    public Avaliabilitymodel(String id, String from, String to, String date, String time, String cartype, String service, String comment, String communication, String driverid,String drivername,String drivernumber,String Create_at) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.cartype = cartype;
        this.service = service;
        this.comment = comment;
        this.communication = communication;
        this.driverid = driverid;
        this.drivername=drivername;
        this.drivernumber=drivernumber;
        this.Create_at=Create_at;

    }

    public Avaliabilitymodel() {
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCartype() {
        return cartype;
    }

    public String getService() {
        return service;
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

    public String getStatus() {
        return status;
    }

    public String getCreate_at() {
        return Create_at;
    }
}
