package com.example.scrubmyride.entities;

public class Cleaner extends User{
    private String startDate;
    private String endDate;
    private Double price;

    public Cleaner(int UserID, String firstName, String lastName, String email, String phoneNumber, String address, String postcode, boolean isCleaner, String startDate, String endDate, Double price) {
        super(UserID, firstName, lastName, email, phoneNumber, address, postcode, isCleaner);
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Double getPrice() { return price; }


    public String toString() {
        return String.format("userID:%d,firstName:%s,lastName:%s,startDate:%s,endDate:%s,price:%d", startDate, endDate, price);
    }
}
