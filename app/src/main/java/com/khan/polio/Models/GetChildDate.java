package com.khan.polio.Models;

public class GetChildDate {
    private String date, status;

    public GetChildDate(){}

    public GetChildDate(String date, String status) {
        this.date = date;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
