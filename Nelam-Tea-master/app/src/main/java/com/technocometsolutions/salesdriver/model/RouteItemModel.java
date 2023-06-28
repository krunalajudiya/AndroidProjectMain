package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("route")
    @Expose
    private String route;

    public RouteItemModel(String id, String route) {
        this.id = id;
        this.route = route;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
