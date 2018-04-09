package com.matt.bookapp;
public class UserInformation {

    public String name;
    public String address;
    public String country;
    public String city;
    public String phone;

    public UserInformation(){

    }

    public UserInformation(String name, String address, String country, String city, String phone) {
        this.name = name;
        this.address = address;
        this.country = country;
        this.city = city;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
