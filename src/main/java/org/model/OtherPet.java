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
import org.model.Pet;

class OtherPet extends Pet{
    public OtherPet(String type, String info, Image image) {
        super(type,info,image);
    }
    public OtherPet(){
        super();
    }
}