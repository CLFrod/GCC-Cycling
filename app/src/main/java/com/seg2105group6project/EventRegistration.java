package com.seg2105group6project;

import java.util.ArrayList;

public class EventRegistration {
    private int minAge;
    private String eventDate;

    // this can be an int out 4.
    // Beginner, Intermediate, Advanced, Professional. kind of like skiing.
    // Ex Usage: if the level 1, then anyone of any skill level can join.
    // if level 2, then only advanced and above can join etc.
    private int levelRequired;

    // just gonna be a dollar figure
    private int registrationFee;
    private int participantTime;
    private ParticipantAccount participant;

    // Empty Constructor for firebase reasons? Who knows at this point.
    public EventRegistration(){
    }

    // Normal constructor.
    public EventRegistration(int minimumAge, int level, int registrationFee, String eventDate){
        this.eventDate = eventDate;
        this.minAge = minimumAge;
        this.levelRequired = level;
        this.registrationFee = registrationFee;
    }


    // Getters and setters for this class
    public int getMinAge(){
        return this.minAge;
    }
    public void setMinAge(int minAge){
        this.minAge = minAge;
    }
    public int getLevel(){
        return this.levelRequired;
    }

    public void setLevel(int level){
        this.levelRequired = level;
    }
    public int getRegistrationFee(){
        return this.registrationFee;
    }

    public void setRegistrationFee(int registrationFee){
        this.registrationFee = registrationFee;
    }

    public String getEventDate(){
        return this.eventDate;
    }
    public void setEventDate(String newEventDate){
        this.eventDate = newEventDate;
    }

}
