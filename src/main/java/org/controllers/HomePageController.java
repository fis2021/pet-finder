package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.exceptions.InvalidUserException;
import org.model.User;
import org.services.DatabaseService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


public class HomePageController {
    @FXML
    private User user;
    @FXML
    private Text AccountStatus;
    @FXML
    private ImageView imageView;

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

    public void setUser(User user) throws MalformedURLException {
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());

        File file = new File(user.getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.rotateProperty();
    }
}
