package com.example.shreejicabs.Model;

public class Notificationmodel {

    String title;
    String message;
    String date;

    public Notificationmodel(){}
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public Notificationmodel(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }
}
