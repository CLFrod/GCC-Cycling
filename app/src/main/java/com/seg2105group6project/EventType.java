package com.seg2105group6project;

public class EventType {
    String eventName;

    String eventDescription;
    String _id;

    public EventType(){
    }
    public EventType(String eventName, String eventDescription){
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }

    public String getEventName(){
        return this.eventName;
    }
    public void setEventName(String newName){
        this.eventName = newName;
    }

    public String getEventDescription(){
        return this.eventDescription;
    }
    public void setEventDescription(String newDescription){
        this.eventDescription = newDescription;
    }


}
