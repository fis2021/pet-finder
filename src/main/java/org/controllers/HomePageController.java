package org.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.dizitart.no2.objects.Cursor;
import org.exceptions.InvalidUserException;
import org.model.Announcement;
import org.model.User;
import org.services.AnnouncementService;
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
    private ListView ads = new ListView<>();

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

    @FXML
    public void handleViewMyAnnouncements(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        String page = "viewMyAnnouncements.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My announcements");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();
        AnnouncementsController ac = loader.getController();
        ac.setUser(user);
        ac.updateMyAnnouncementList();
    }

    @FXML
    public void handleAddAnnouncementAction(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        String page = "addAnnouncementPage.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("Add Announcement");
        currentStage.setScene(new Scene(root, 500, 500));
        currentStage.show();
        AnnouncementsController ac = loader.getController();
        ac.setUser(user);
        //smc.updateList();
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

    @FXML
    public void updateAnnouncementList() {
        ObservableList<String> allAds = FXCollections.observableArrayList();

        Cursor<Announcement> cursor = AnnouncementService.getAnnouncementRepository().find();
        for (Announcement announcement : cursor) {
            allAds.add(announcement.toString());
        }

        if(allAds.isEmpty()){
            allAds.add("Currently there are no announcements");
        }

        ads.setItems(allAds);
    }
}
