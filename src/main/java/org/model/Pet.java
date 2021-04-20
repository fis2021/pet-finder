package org.model;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pet{
    private String type;
    private String info;
    private String imagePath;

    public Pet(String type, String info){
        this.type = type;
        this.info=info;
        this.imagePath="";
    }

    public Pet() {
    }

    public String getType(){ return this.type; }
    public String getInfo(){ return this.info; }
    public String getImagePath(){ return this.imagePath; }
    public void setType(String type){ this.type=type; }
    public void setInfo(String info){ this.info=info; }
    public void setImagePath(String imagePath){ this.imagePath=imagePath; }
    public void setImage(String imagePath){
        Image image=new Image(imagePath);
        ImageView imageView = new ImageView(image);
    }
    public Image getImage(){
        Image image=new Image(imagePath);
        ImageView imageView = new ImageView(image);
        return image;
    }


    public boolean equals(Object o){
        if(o instanceof Pet){
            Pet comp=(Pet)o;
            if(this.type.equals(comp.getType()) && this.info.equals(comp.getInfo()) && this.imagePath.equals(comp.getImagePath()))
                return true;
        }
        return false;
    }

}
