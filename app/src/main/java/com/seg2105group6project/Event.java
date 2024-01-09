package com.seg2105group6project;

import java.util.ArrayList;

public class Event {

    // Event Hard Info
    private String region;
    // distance represented as double, and is in KM!!
    private double routeDistance;
    // average pace in KM/hr, 1 = 1km/hr
    private int averagePace;
    private int maxParticipants;

    // Management Related DataFields
    private ArrayList<ParticipantAccount> registeredParticipants;
    private OrganizerAccount mainOrganizer;
    private EventType eventType;
    private String eventName;
    private EventRegistration eventRegistration;

    public void setRegisteredParticipants(ArrayList<ParticipantAccount> registeredParticipants) {
        this.registeredParticipants = registeredParticipants;
    }
    // Constructor for event
    // Allows those to create different event types.
    // Since we only need to handle 5 different events, it is possible that we should
    public Event(){
        this.registeredParticipants = new ArrayList<>();
    }

    // Main Constructor that just initializes empty registrations list
    public Event(String region, double distance, int averagePace, int maxParticipants, String eventName, OrganizerAccount organizer, EventType eventType, EventRegistration eventRegistration){
        this.region = region;
        this.routeDistance = distance;
        this.mainOrganizer = organizer;
        this.maxParticipants = maxParticipants;
        this.registeredParticipants = new ArrayList<>(maxParticipants);
        this.eventType = eventType;
        this.averagePace = averagePace;
        this.eventName = eventName;
        this.eventRegistration = eventRegistration;
        this.registeredParticipants = new ArrayList<>(maxParticipants);
    }

    // Tertiary Constructor that allows you to input a previous list of registrations for whatever reason
    // USUALLY WE DO NOT USE!!!
    // Will maybe be used for edit stuff. idk
    // removed this constructor since it serves no purpose

    public String getRegion(){
        return this.region;
    }
    public void setRegion(String newRegion){
        this.region = newRegion;
    }
    public double getRouteDistance(){
        return this.routeDistance;
    }
    public void setRouteDistance(double newRouteDistance){
        this.routeDistance = newRouteDistance;
    }
    public ArrayList<ParticipantAccount> getRegisteredParticipants(){
        return this.registeredParticipants;
    }

    /**
     * This
     * @return Returns true if you can add a participant otherwise false
     *
     * */
    public boolean addParticipant(ParticipantAccount participant){
        if(this.registeredParticipants.size() < maxParticipants){
            this.registeredParticipants.add(participant);
            return true;
        }
        else{
            return false;
        }


    }
    public OrganizerAccount getMainOrganizer(){
        return this.mainOrganizer;
    }
    public EventType getEventType(){
        return this.eventType;
    }
    public void setEventType(EventType newType){
        this.eventType = newType;
    }

    public int getAveragePace(){
        return this.averagePace;
    }
    public void setAveragePace(int averagePace){
        this.averagePace = averagePace;
    }

    public int getMaxParticipants(){
        return this.maxParticipants;
    }
    public void setMaxParticipants(int newMaxAmount){
        this.maxParticipants = newMaxAmount;
    }



    public String getEventName(){
        return this.eventName;
    }
    public void setEventName(String name){
        this.eventName = name;
    }

    public EventRegistration getEventRegistration(){
        return this.eventRegistration;
    }

}
