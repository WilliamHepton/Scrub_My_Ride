package com.example.scrubmyride.entities;

public class InvoiceCustomer {
    private int InvoiceID;
    private String ServiceTimeStart;
    private float Price;
    private float ServiceFee;
    private String Description;
    private String FirstName;
    private String LastName;
    private String Email;
    private String PhoneNumber;

    public InvoiceCustomer(int invoiceID, String serviceTimeStart, float price, float serviceFee, String description, String firstName, String lastName, String email, String phoneNumber) {
        InvoiceID = invoiceID;
        ServiceTimeStart = serviceTimeStart;
        Price = price;
        ServiceFee = serviceFee;
        Description = description;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        PhoneNumber = phoneNumber;
    }

    public int getInvoiceID() {
        return InvoiceID;
    }

    public String getServiceTimeStart() {
        return ServiceTimeStart;
    }

    public float getPrice() {
        return Price;
    }

    public float getServiceFee() {
        return ServiceFee;
    }

    public String getDescription() {
        return Description;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }
}
