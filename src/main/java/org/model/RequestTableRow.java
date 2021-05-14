package org.model;

public class RequestTableRow {
    private String ID;

    private String status;
    private String category;
    private String sender;
    private String date;
    private String announcementInfo;

    public RequestTableRow(String ID, String status, String category, String sender, String date, String announcementInfo){
        this.ID = ID;
        this.status = status;
        this.category = category;
        this.sender = sender;
        this.date = date;
        this.announcementInfo = announcementInfo;
    }
    public String getID() {
        return ID;
    }
    public String getStatus() {
        return status;
    }
    public String getCategory() {
        return category;
    }
    public String getSender() {
        return sender;
    }
    public String getDate() {
        return date;
    }
    public String getAnnouncementInfo() {
        return announcementInfo;
    }
}
