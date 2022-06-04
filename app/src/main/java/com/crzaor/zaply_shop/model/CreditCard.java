package com.crzaor.zaply_shop.model;

public class CreditCard {

    private String number;
    private String incumbent;
    private int month;
    private int year;
    private String cvv;

    public CreditCard(String number, String incumbent,
               int month, int year, String cvv) {
        this.number = number;
        this.incumbent = incumbent;
        this.month = month;
        this.year = year;
        this.cvv = cvv;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIncumbent() {
        return incumbent;
    }

    public void setIncumbent(String incumbent) {
        this.incumbent = incumbent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
