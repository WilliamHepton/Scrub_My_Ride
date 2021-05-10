package com.example.scrubmyride.entities;

public class Customer extends User{

    private String carRegnumber;

    public Customer(int UserID, String firstName, String lastName, String email, String phoneNumber, String address, String postcode, boolean isCleaner, String carRegnumber) {
        super(UserID, firstName, lastName, email, phoneNumber, address, postcode, isCleaner);

        this.carRegnumber = carRegnumber;
    }
}
