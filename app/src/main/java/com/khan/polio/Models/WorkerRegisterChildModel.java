package com.khan.polio.Models;

public class WorkerRegisterChildModel {
    private String name, fatherName, houseNumber;

    public WorkerRegisterChildModel (){ }

    public WorkerRegisterChildModel(String name, String fatherName, String houseNumber) {
        this.name = name;
        this.fatherName = fatherName;
        this.houseNumber = houseNumber;
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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
