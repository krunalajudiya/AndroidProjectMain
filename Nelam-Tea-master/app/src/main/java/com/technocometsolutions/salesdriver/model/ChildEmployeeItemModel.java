package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildEmployeeItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("employe")
    @Expose
    private String employe;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }
}
