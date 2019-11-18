package com.khan.polio.Models;

import com.khan.polio.ChildRegisteration;

public class ChildRegistrationModel {
    private String name,fatherName,age,houseNumber,homeAddress,status,date;
    public ChildRegistrationModel(){}

    public ChildRegistrationModel(String name, String fatherName, String age, String houseNumber, String homeAddress, String status, String date) {
        this.name = name;
        this.fatherName = fatherName;
        this.age = age;
        this.houseNumber = houseNumber;
        this.homeAddress = homeAddress;
        this.status = status;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }
}
