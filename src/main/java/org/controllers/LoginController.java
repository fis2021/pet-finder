package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import org.exceptions.InvalidUserException;
import org.model.User;
import org.services.UserService;


public class LoginController extends Controller{

    @FXML
    private Text loginMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    @FXML
    public void handleLoginAction(ActionEvent event) {
        try {
            User user = UserService.login(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Logged-In successfully");
            redirectToHome(event, user);
        } catch (InvalidUserException e) {
            loginMessage.setText(e.getMessage());
        }
    }
}
