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

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnnouncementInfo() {
        return announcementInfo;
    }

    public void setAnnouncementInfo(String announcementInfo) {
        this.announcementInfo = announcementInfo;
    }
}
