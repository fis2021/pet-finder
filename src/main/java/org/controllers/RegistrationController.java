package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.exceptions.UsernameAlreadyExistsException;
import org.services.DatabaseService;

public class RegistrationController {

    @FXML
    private Text registrationMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;

    @FXML
    public void initialize() {
        role.getItems().addAll("Individual", "Shelter");
    }

    @FXML
    public void handleRegisterAction(ActionEvent event) throws IOException, InterruptedException {
        boolean succes = false;
        try {
            if(usernameField.getText() == "" || passwordField.getText() == "" || role.getValue() == ""){
                throw new NullPointerException("Username and password required");
            }

            DatabaseService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue());
            registrationMessage.setText("Account created successfully!\nRedirecting to login...");
            succes = true;

        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch(Exception e){
            registrationMessage.setText(e.getMessage());
        }
        if(succes) {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Register");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        }
    }

    @FXML
    public void redirectToLogin(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        currentStage.setTitle("Register");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();
    }
}
