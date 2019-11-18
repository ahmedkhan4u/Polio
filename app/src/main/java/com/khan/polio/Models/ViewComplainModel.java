package com.khan.polio.Models;

public class ViewComplainModel {
    private String title,address,description,date;

    public ViewComplainModel(){}

    public ViewComplainModel(String title, String address, String description, String date) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
