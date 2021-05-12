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
import org.services.UserService;

import java.io.IOException;


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
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();

            User user = UserService.login(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Logged-In succesfully");
            String page = "";

            if(user.getRole().equals("Individual")){
                page = "homePageScene.fxml";
            }

            if(user.getRole().equals("Shelter")){
                page = "manageRequestsScene.fxml";
            }
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
            Parent root = loader.load();
            currentStage.setTitle("Home");
            currentStage.setScene(new Scene(root, 800, 600));
            currentStage.show();
            if(user.getRole().equals("Individual")){
                HomePageController hpc = loader.getController();
                hpc.setUser(user);
                hpc.updateAnnouncementList();
            }else{
                RequestController hpc = loader.getController();
                hpc.setUser(user);
                hpc.updateRequestList("Inbox");
            }


        } catch (InvalidUserException | IOException e) {
            loginMessage.setText(e.getMessage());
        }
    }
}
