package com.example.project;

import java.io.Serializable;

public class User implements Serializable {

    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String phoneNumber;
    public String bloodType;
    public String sex;
    public String role;
    public String picture;

    public User() {

    }

    public User(String email, String first, String last, String password, String phone, String blood, String sex, String role) {
        this.email = email;
        this.firstName = first;
        this.lastName = last;
        this.password = password;
        this.phoneNumber = phone;
        this.bloodType = blood;
        this.sex = sex;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first) {
        this.firstName = first;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last) {
        this.lastName = last;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String blood) {
        this.bloodType = blood;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
