package com.reminder.remindme.data.model;

//create user with id,fullname,address,mobileNumber,email,password,createat
public class User {
    private String id;
    private String fullName;
    private String address;
    private String mobileNumber;
    private String email;
    private String password;
    private long createdAt;

    public User() {
    }

    public User(String id, String fullName, String mobileNumber, String email) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public User(String id, String fullName, String mobileNumber, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
    }

    public User(String id, String fullName, String mobileNumber, String email, String password, long createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
