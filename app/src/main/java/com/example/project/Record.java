package com.example.project;

public class Record {
    String userID;
    String categoryID;
    String hospitalID;
    String doctorID;
    String result;
    String date;
    String description;

    public Record(String userID, String categoryID, String hospitalID, String doctorID, String result, String date, String description) {
        this.userID = userID;
        this.categoryID = categoryID;
        this.hospitalID = hospitalID;
        this.doctorID = doctorID;
        this.result = result;
        this.date = date;
        this.description = description;
    }

    public Record() {

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
