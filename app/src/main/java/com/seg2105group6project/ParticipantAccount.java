package com.seg2105group6project;

import java.io.Serializable;

public class ParticipantAccount extends Account implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String firstName = "";
    private String lastName = "";
    private final String role = "Participant";
    public ParticipantAccount() {
        // This is an empty constructor, just for FireBase to be able to store the class
    }

    public ParticipantAccount(String userName, String password, String email, String firstName, String lastName) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getRole(){
        return this.role;
    }
}
