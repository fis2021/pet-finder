package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.model.Pet;
import org.model.User;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShelterManagerController {
    @FXML
    private User user;

    @FXML
    private Text AccountStatus;

    @FXML
    private Text ListStatus;

    @FXML
    private ListView Pets;

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Manage Pets");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listPets(User user){
        if(user.getPetList().isEmpty()) { ListStatus.setText("No pets yet!"); }
        else{
            for(Pet pet:user.getPetList()){
               ;
            }
        }
    }


    @FXML
    public void redirectToShelterHomePage(ActionEvent event){
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("shelterHomePage.fxml"));
            currentStage.setTitle("Shelter Home Page");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
