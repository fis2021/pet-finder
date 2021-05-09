package org.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class ImageStringTableRow {
    private ImageView imageView;
    //private final SimpleStringProperty info;
    private final String info;

    public ImageStringTableRow(ImageView imageView, String info){
        this.imageView = imageView;
        //this.info = new SimpleStringProperty(info);
        this.info = info;
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
