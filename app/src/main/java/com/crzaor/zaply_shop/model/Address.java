package com.crzaor.zaply_shop.model;

public class Address {

    private String name;
    private String surname;
    private String address;
    private String postcode;
    private String province;
    private String city;
    private String phone;

    public Address(String name, String surname, String address,
             String postcode, String province, String city, String phone) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.postcode = postcode;
        this.province = province;
        this.city = city;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
