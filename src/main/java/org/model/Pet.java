package org.model;


import java.awt.image.BufferedImage;

public class Pet{
    private String type;
    private BufferedImage image;
    private String info;

    public Pet(String type, String info, BufferedImage image) {
        this.type = type;
        this.info=info;
        this.image=image;
    }

    public Pet() {
    }

    public String getType(){ return this.type; }
    public String getInfo(){ return this.info; }
    public BufferedImage getImage(){ return this.image; }
    public void setType(String type){ this.type=type; }
    public void setInfo(String info){ this.info=info; }
    public void setImage(BufferedImage image){ this.image=image; }

    public boolean equals(Object o){
        if(o instanceof Pet){
            Pet comp=(Pet)o;
            if(this.type.equals(comp.getType()) && this.info.equals(comp.getInfo()) && this.image.equals(comp.getImage()))
                return true;
        }
        return false;
    }

}
