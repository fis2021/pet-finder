package org.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Announcement {
    private Pet pet;
    private User user;
    private Date datePosted=new Date();
    private String info="";
    private String category="";

    public Announcement(Pet pet, User user, String category){
        this.pet=pet;
        this.user=user;
        this.category=category;
    }

    public Announcement(){}

    public Pet getPet(){ return this.pet; }
    public User getUser() { return this.user; }
    public Date getDatePosted() { return this.datePosted; }
    public String getInfo() { return this.info; }
    public String getCategory() { return this.category; }
    public void setPet(Pet pet){ this.pet=pet; }
    public void setUser(User user){ this.user=user; }
    public void setInfo(String info){ this.info=info; }
    public void setDatePosted(Date datePosted){ this.datePosted=datePosted; }
    public void setCategory(String category){ this.category=category; }
    public String getStringDate(){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String strDate = df.format(this.datePosted);
        return strDate;
    }

    @Override
    public String toString(){
        return this.user + " " + this.category + "\nPet:" + this.pet + "\n" + this.getStringDate() +"\n" + this.info;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if(o instanceof Announcement){
            Announcement comp=(Announcement) o;
            return this.user.equals(comp.getUser()) && this.pet.equals(comp.getPet()) && this.category.equals(comp.getCategory()) && this.info.equals(comp.getInfo()) && this.getStringDate().equals(comp.getStringDate());
        }
        return false;
    }


}