package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.exceptions.InvalidUserException;
import org.model.User;
import org.services.DatabaseService;

import java.io.IOException;


public class HomePageController {
    @FXML
    private User user;

    @FXML
    private Text AccountStatus;

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Login");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    public void handleManagePetsAction(ActionEvent event) throws Exception{
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            String page = "shelterManagerPage.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
            Parent root = loader.load();
            currentStage.setTitle("Manage Pets");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
            ShelterManagerController smc = loader.getController();
            smc.setUser(user);
            smc.updateList();
    }

    public void setUser(User user){
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());
    }
}
