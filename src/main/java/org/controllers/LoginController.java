package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.exceptions.InvalidUserException;
import org.model.User;
import org.services.DatabaseService;

import java.io.IOException;


public class LoginController {

    @FXML
    private Text loginMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private String role;

    @FXML
    public void handleLoginAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();

            User user = DatabaseService.login(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Logged-In succesfully");
            //wait(2000);

            String page = "";

            if(user.getRole().equals("Individual")){
                page = "homePage.fxml";
            }

            if(user.getRole().equals("Shelter")){
                page = "shelterHomePage.fxml";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
            Parent root = loader.load();
            currentStage.setTitle("Home");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
            HomePageController hpc = loader.getController();
            hpc.setUser(user);

        } catch (InvalidUserException | IOException e) {
            loginMessage.setText(e.getMessage());
        }
    }

    @FXML
    public void redirectToRegister(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("registerScene.fxml"));
        currentStage.setTitle("Register");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
    }
}
