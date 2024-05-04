package com.example.project;

import java.io.Serializable;

public class Hospital implements Serializable {
    String name;
    String email;
    String phoneNumber;
    String description;
    String imageURL;
    String location;
    double longitude;
    double latitude;

    public Hospital() {

    }

    public Hospital(String name, String email, String phoneNumber, String description, String imageURL,
                    String location, double longitude, double latitude) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.imageURL = imageURL;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
