package org.model;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.UUID;

public class Pet{
    private String ID;
    private String type;
    private String name;
    private String info;
    private String imagePath;

    public Pet(String name, String type){
        this.ID = UUID.randomUUID().toString();
        this.type = type;
        this.name = name;
        this.info = "";
        this.imagePath = "";
    }

    public Pet(){

    }

    public String getID(){ return this.ID; }
    public String getName(){ return this.name; }
    public String getType(){ return this.type; }
    public String getInfo(){ return this.info; }
    public String getImagePath(){ return this.imagePath; }
    public void setName(String name){ this.name = name; }
    public void setType(String type){ this.type=type; }
    public void setInfo(String info){ this.info=info; }
    public void setImagePath(String imagePath){ this.imagePath=imagePath; }


    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }

        if(o instanceof Pet){
            Pet comp=(Pet) o;
            return this.ID.equals(comp.getID()) && this.name.equals(comp.getName()) && this.type.equals(comp.getType()) && this.info.equals(comp.getInfo()) && this.imagePath.equals(comp.getImagePath());
        }

        return false;
    }

    @Override
    public String toString(){
        return ("Name: " + this.name + "\n\nPet type: " + this.type + "\n\nPet info: " + this.info);
    }
}
