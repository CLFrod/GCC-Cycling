package com.seg2105group6project;

import java.io.Serializable;
import java.util.ArrayList;

public class OrganizerAccount extends Account implements Serializable {
    private String userName;
    private String password;
    private String email;
    private String firstName = "";
    private String lastName = "";
    private String phoneNumber;
    private String instagramLink;
    private ArrayList<Rating> ratings;
    private final String role = "Organizer";
    public OrganizerAccount() {
        // This is an empty constructor, just for FireBase to be able to store the class
    }
    public OrganizerAccount(String userName, String password, String email, String firstName, String lastName, String phoneNumber, String instagramLink) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.instagramLink = instagramLink;
        this.ratings = new ArrayList<>();
    }
    public String getRole() {
        return role;
    }

    // Which is why the ROLE was appearing, and now the rest of the info is displayed.
    // ADDING THE GET functions will fill the database with the required info!!!

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
    public String getPhoneNumber() {return this.phoneNumber;}
    public String getInstagramLink() {return this.instagramLink;}
    public ArrayList<Rating> getRatings() {return this.ratings;}
    public void setRatings(ArrayList<Rating> ratings) {this.ratings = ratings;}
    public void addRating(Rating rating) {this.ratings.add(rating);}
}
