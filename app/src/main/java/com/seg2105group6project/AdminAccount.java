package com.seg2105group6project;

public class AdminAccount extends Account{
    private String _username;
    private String _password;
    private String _email;

    // Not currently in use
    // private int identification = 0;
    private String _firstname = "";
    private String _lastname = "";
    private String role = "admin";

    public AdminAccount() {
        // This is an empty constructor, just for FireBase to be able to store the class
    }

    public AdminAccount(String username, String password, String email, String firstName, String lastName) {
        this._username = username;
        this._email = email;
        this._password = password;
        this._firstname = firstName;
        this._lastname = lastName;
    }

    public String getUserName(){
        return this._username;
    }
    public String getPassword(){
        return this._password;
    }
    public String getEmail(){
        return this._email;
    }
    public String getFirstName()  { return this._firstname; }
    public String getLastName()  { return this._lastname; }
    public String getRole() {
        return role;
    }
}
