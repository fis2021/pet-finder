package org.model;

import org.dizitart.no2.objects.Id;

import java.util.Date;
import java.util.UUID;

public class Request {
    @Id
    private String ID;

    private User sender;
    private User receiver;

    private Announcement announcement;
    private String message;
    private String status;

    private Date date = new Date();

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

    public void setStatus(String status){ this.status = status; }

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

    public Date getDate() {
        return date;
    }
}
