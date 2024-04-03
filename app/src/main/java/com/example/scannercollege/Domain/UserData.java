package com.example.scannercollege.Domain;

public class UserData {
    private static UserData instance;
    private String name;
    private String phoneNumber;
    private String email;

    private UserData() {}

    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    // Getters and setters for user data
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
