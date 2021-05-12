package org.model;

import javafx.scene.image.ImageView;

public class ImageStringTableRow {
    private ImageView imageView;
    private final String info;
    private final String ID;

    public ImageStringTableRow(ImageView imageView, String info, String ID){
        this.imageView = imageView;
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
}
