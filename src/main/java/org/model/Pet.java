package org.model;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.dizitart.no2.objects.Id;

public abstract class Pet{
    private String type;
    private Image image;
    private String info;

    public Pet(String type, String info, Image image) {
        this.type = type;
        this.info=info;
        this.image=image;
    }

    public Pet() {
    }

    public String getType(){ return this.type; }
    public String getInfo(){ return this.info; }
    public Image getImage(){ return this.image; }
    public void setType(String type){ this.type=type; }
    public void setInfo(String info){ this.info=info; }
    public void setImage(Image image){ this.image=image; }

    public boolean equals(Object o){
        if(o instanceof Pet){
            Pet comp=(Pet)o;
            if(this.type.equals(comp.getType()) && this.info.equals(comp.getInfo()) && this.image.equals(comp.getImage()))
                return true;
        }
        return false;
    }

}
