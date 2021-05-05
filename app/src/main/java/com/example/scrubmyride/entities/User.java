package com.example.scrubmyride.entities;

public class User {
    private int UserID;
    private String FirstName;
    private String LastName;
    private String Email;
    private String PhoneNumber;
    private String Address;
    private String Postcode;
    private boolean IsCleaner;

    public User(int UserID, String firstName, String lastName, String email, String phoneNumber, String address, String postcode, boolean isCleaner) {
        this.UserID = UserID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Address = address;
        this.Postcode = postcode;
        this.IsCleaner = isCleaner;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        this.Postcode = postcode;
    }

    public boolean isCleaner() {
        return IsCleaner;
    }

    public void setCleaner(boolean cleaner) {
        IsCleaner = cleaner;
    }

    public int getUserID() {
        return UserID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }
}
