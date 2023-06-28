package com.technocometsolutions.salesdriver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalougeDetailsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("catalogue_pdf")
    @Expose
    private String catalogue_pdf;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCatalogue_pdf() {
        return catalogue_pdf;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCatalogue_pdf(String catalogue_pdf) {
        this.catalogue_pdf = catalogue_pdf;
    }
}
