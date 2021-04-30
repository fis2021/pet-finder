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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.exceptions.UsernameAlreadyExistsException;
import org.model.User;
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
    private TextField phoneNo;
    @FXML
    private TextField country;
    @FXML
    private TextField region;
    @FXML
    private TextField town;
    @FXML
    private TextField street;
    @FXML
    private File image;
    @FXML
    private String imagePath;
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        role.getItems().addAll("Individual", "Shelter");
    }

    @FXML
    void handleAddPhotoAction() throws MalformedURLException {
        Stage stage = new Stage();
        stage.setTitle("Add Photo");
        FileChooser filechooser = new FileChooser();
        filechooser.setInitialDirectory(new File("C:\\"));
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jpg files","*.jpg"));
        image = filechooser.showOpenDialog(stage);
        imagePath = image.getAbsolutePath();
        filechooser.setInitialDirectory(image.getParentFile());
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.rotateProperty();
    }


    @FXML
    public void clearImageAction(ActionEvent event) throws MalformedURLException {
        imagePath = "";
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);
        imageView.rotateProperty();
    }

    @FXML
    public void handleRegisterAction(ActionEvent event) throws IOException, InterruptedException {
        boolean success = false;
        try {
            if(usernameField.getText() == "" || passwordField.getText() == "" || role.getValue() == ""){
                throw new NullPointerException("Username and password required");
            }

            DatabaseService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue());
            if(imagePath != ""){
                User crt = DatabaseService.findUserByUsername(usernameField.getText());
                crt.setImagePath(imagePath);
                DatabaseService.updateUser(crt);
            }
            registrationMessage.setText("Account created successfully!\nRedirecting to login...");
            success = true;

        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch(Exception e){
            registrationMessage.setText(e.getMessage());
        }
        if(success) {
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
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
    }
}
