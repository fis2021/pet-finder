package org.model;

import org.dizitart.no2.objects.Id;

import java.util.UUID;

public class Request {
    @Id
    String ID;

    User sender;
    User receiver;

    Announcement announcement;
    String message;
    String status;

    public Request(User sender, User receiver, Announcement announcement){
        UUID uuid = UUID.randomUUID();
        this.ID = uuid.toString();

        this.sender = sender;
        this.receiver = receiver;
        this.announcement = announcement;
        this.message = "";
        this.status = "Pending";
    }

    public Request(){

    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getStatus(){ return status; }

    public String getID(){ return ID; }

    public String getMessage() {
        return message;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

}
