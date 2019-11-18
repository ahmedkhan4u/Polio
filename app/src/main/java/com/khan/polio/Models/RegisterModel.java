package com.khan.polio.Models;

public class RegisterModel {
    private String Name,Email,Cnic,Contact,imageUrl,userId;

    public RegisterModel(){

    }

    public RegisterModel(String name, String email, String cnic, String contact, String imageUrl, String userId) {
        Name = name;
        Email = email;
        Cnic = cnic;
        Contact = contact;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCnic() {
        return Cnic;
    }

    public void setCnic(String cnic) {
        Cnic = cnic;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
