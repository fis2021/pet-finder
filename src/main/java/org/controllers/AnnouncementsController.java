package org.controllers;

import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.model.Announcement;
import org.model.Pet;
import org.model.User;
import org.services.AnnouncementService;
import org.services.DatabaseService;
import org.services.UserService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class AnnouncementsController {

    private User user;

    @FXML
    private TextField petName;

    @FXML
    private TextField petInfo;

    @FXML
    private ChoiceBox petType = new ChoiceBox();

    @FXML
    private TextField adInfo;

    @FXML
    private ChoiceBox category = new ChoiceBox();

    @FXML
    private Text AccountStatus;

    @FXML
    private Text AddStatus;

    @FXML
    private File image;
    @FXML
    private String imagePath="";
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        category.getItems().addAll("Lost", "Found", "Adoption");
        petType.getItems().addAll("Cat","Dog","Other");
    }

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Login");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
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
    public void handleAddAnnouncementAction(ActionEvent event) throws IOException {
        if(petName.getText() == "" || petType.getValue() == null || category.getValue() == null){
            AddStatus.setText("Pet name, pet type and announcement category are required!");
            return;
        }
        Pet crtPet = new Pet(petName.getText(), (String) petType.getValue());

        crtPet.setInfo(petInfo.getText());
        crtPet.setImagePath(imagePath);
        Announcement crtAd = new Announcement(crtPet, user, (String) category.getValue());
        crtAd.setInfo(adInfo.getText());
        AnnouncementService.addAnnouncement(crtAd);

        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homePage.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Individual Homepage");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();

        HomePageController hc = loader.getController();
        hc.setUser(user);
    }


    @FXML
    public void cancelAddPet(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homePage.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Individual Homepage");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();

        HomePageController hc = loader.getController();
        hc.setUser(user);

    }

    public void setUser(User user){
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());
    }
}
