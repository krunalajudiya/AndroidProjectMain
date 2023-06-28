package com.example.shreejicabs.Model;

public class Addvehiclemodel {

    String  id;
    String car_name;
    String fuel;
    String color;
    String crrier;
    String model_year;
    String photo;

    public Addvehiclemodel(){}

    public Addvehiclemodel(String id, String car_name, String fuel, String color, String crrier, String model_year,String photo) {
        this.id = id;
        this.car_name = car_name;
        this.fuel = fuel;
        this.color = color;
        this.crrier = crrier;
        this.model_year = model_year;
        this.photo=photo;
    }

    public String getId() {
        return id;
    }

    public String getCar_name() {
        return car_name;
    }


    public String getFuel() {
        return fuel;
    }

    public String getColor() {
        return color;
    }

    public String getCrrier() {
        return crrier;
    }

    public String getPhoto() {
        return photo;
    }

    public String getModel_year() {
        return model_year;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }


    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCrrier(String crrier) {
        this.crrier = crrier;
    }

    public void setModel_year(String model_year) {
        this.model_year = model_year;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
