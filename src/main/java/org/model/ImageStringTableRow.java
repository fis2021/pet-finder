package org.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class ImageStringTableRow {
    private ImageView imageView;
    //private final SimpleStringProperty info;
    private final String info;
    private final String ID;

    public ImageStringTableRow(ImageView imageView, String info, String ID){
        this.imageView = imageView;
        //this.info = new SimpleStringProperty(info);
        this.info = info;
        this.ID = ID;
    }

    public final String getID(){
        return ID;
    }

    public final String getInfo(){
        return info;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }

    // SimpleStringProperty getInfo(){
    //    return this.info;
    //}

    //public void setInfo(String info){
    //    this.info.set(info);
    //}
}
