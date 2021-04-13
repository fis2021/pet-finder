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

import org.exceptions.UserDoesNotExistException;
import org.model.User;
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
    public void handleLoginAction() {
        try {
            User user = UserService.login(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Logged-In succesfully");
        } catch (UserDoesNotExistException e) {
            loginMessage.setText(e.getMessage());
        }
    }

    @FXML
    public void redirectToRegister(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
        currentStage.setTitle("Register");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();
    }
}
