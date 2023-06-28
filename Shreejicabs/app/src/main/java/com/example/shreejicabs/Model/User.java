package com.example.shreejicabs.Model;

public class User {

    String id;
    String name;
    String mobile_number;
    String email;
    String type;
    String city_pref;


    public User(String id, String name, String mobile_number, String email,String type,String city_pref) {
        this.id = id;
        this.name = name;
        this.mobile_number = mobile_number;
        this.email = email;
        this.type=type;
        this.city_pref=city_pref;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity_pref() {
        return city_pref;
    }

    public void setCity_pref(String city_pref) {
        this.city_pref = city_pref;
    }
}
