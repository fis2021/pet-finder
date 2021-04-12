package org.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import org.exceptions.UserDoesNotExistException;
import org.services.UserService;

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
    public void handleRegisterAction() {
        try {
            UserService.login(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Logged-In succesfully");
        } catch (UserDoesNotExistException e) {
            loginMessage.setText(e.getMessage());
        }
    }
}
