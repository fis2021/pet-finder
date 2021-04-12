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
import org.model.*;
import java.util.*;

import org.dizitart.no2.objects.Id;

class PetList{
    private ArrayList<Pet> petList=new ArrayList<Pet>();

    public PetList(){
    }

    public void addPet(Pet pet){
        petList.add(pet);
    }

    public void removePet(Pet pet){
        petList.remove(pet);
    }
}
