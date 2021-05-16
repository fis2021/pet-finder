package org.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.model.Announcement;
import org.model.User;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Controller {
    @FXML
    protected User user;
    @FXML
    protected Text AccountStatus;
    @FXML
    protected ImageView profilePicture;

    @FXML
    protected MenuButton menu;

    protected FXMLLoader redirect(Event event, String page, String title) throws IOException {
        System.out.println("\nRedirecting to " + page.toUpperCase() + " scene");
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle(title);
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        return loader;
    }

    @FXML
    public void redirectToMyProfile() throws IOException {
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "viewProfile.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My profile");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();

        UserController uc = loader.getController();
        uc.setUser(user);
    }
    @FXML
    public void redirectToMyRequests() throws IOException {
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "manageRequestsScene.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        RequestController rc = loader.getController();
        rc.setUser(user);
        rc.updateRequestList("Inbox");
    }

    @FXML
    public void redirectToAddAnnouncement(Event event) throws Exception{
        FXMLLoader loader = redirect(event, "addAnnouncementPage.fxml", "Add announcement");
        AnnouncementsController ac = loader.getController();
        ac.setUser(user);
    }

    @FXML
    public void handleViewAnnouncementAction(Announcement announcement, Event event) throws Exception{
        try {
            FXMLLoader loader = redirect(event, "announcementDetails.fxml", "Details");
            RequestController rc = loader.getController();
            rc.setUser(user);
            rc.setAnnouncementInfo(announcement);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleViewMyAnnouncements(ActionEvent event) throws Exception{
        Stage currentStage = (Stage) menu.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("viewMyAnnouncementsScene.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        AnnouncementsController ac = loader.getController();
        ac.setUser(user);
        ac.updateMyAnnouncementList();
    }

    @FXML
    public void redirectToRegister(ActionEvent event) throws Exception{
        FXMLLoader loader = redirect(event, "registerScene.fxml", "Register");
    }

    @FXML
    public void setUser(User user) throws MalformedURLException {
        this.user = user;

        if(AccountStatus != null){
            AccountStatus.setText("Logged-in as " + user.getUsername());
        }

        if(profilePicture != null){
            File file = new File(user.getImagePath());
            String localUrl = file.toURI().toURL().toExternalForm();
            Image profile = new Image(localUrl, false);
            profilePicture.setImage(profile);
        }
    }

    @FXML
    public void redirectToHome(ActionEvent event, User user){
        try {
            String page = "";

            if (user.getRole().equals("Individual")) {
                page = "homePageScene.fxml";
            }

            if (user.getRole().equals("Shelter")) {
                page = "manageRequestsScene.fxml";
            }
            FXMLLoader loader = redirect(event, page, "Home");

            if (user.getRole().equals("Individual")) {
                HomePageController hpc = loader.getController();
                hpc.setUser(user);
                hpc.updateAnnouncementList();
            } else {
                RequestController rc = loader.getController();
                rc.setUser(user);
                rc.updateRequestList("Inbox");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            System.out.println("\nSigning-out...");
            redirect(event,"login.fxml","Login");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
