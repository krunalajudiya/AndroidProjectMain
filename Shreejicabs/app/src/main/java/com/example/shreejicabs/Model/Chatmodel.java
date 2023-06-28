package com.example.shreejicabs.Model;

public class Chatmodel {
    String sender;
    String sendername;
    String receiver;
    String receivername;
    String message;
    String time;

    public Chatmodel(String sender,String sendername, String receiver, String receivername,String message,String time) {
        this.sender = sender;
        this.sendername=sendername;
        this.receiver = receiver;
        this.receivername=receivername;
        this.message = message;
        this.time=time;
    }

    public Chatmodel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
