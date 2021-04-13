package org.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.exceptions.UsernameAlreadyExistsException;
import org.services.UserService;

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
        role.getItems().addAll("User", "Shelter");
    }

    @FXML
    public void handleRegisterAction() {
        try {
            if(usernameField.getText() == "" || passwordField.getText() == "" || role.getValue() == ""){
                throw new NullPointerException("Username and password required");
            }
            
            UserService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue());
            registrationMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch(Exception e){
            registrationMessage.setText(e.getMessage());
        }
    }
}
