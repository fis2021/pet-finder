package org.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.filters.ObjectFilters;
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
    private ListView ads = new ListView<>();

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
    private String imagePath = "src/main/resources/img/pet.png";
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
    public void updateMyAnnouncementList() {
        ObservableList<String> crtAds = FXCollections.observableArrayList();

        ArrayList<Announcement> userAds=AnnouncementService.getUserAnnouncements(user.getUsername());
        for(Announcement announcement : userAds){
            crtAds.add(announcement.toString());
        }
        if(crtAds.isEmpty()){
            crtAds.add("Currently you have no announcements");
        }
        ads.setItems(crtAds);
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
        imageView.setFitWidth(100);
        imageView.rotateProperty();
    }

    @FXML
    public void clearImageAction(ActionEvent event) throws MalformedURLException {
        imagePath = "src/main/resources/img/pet.png";
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
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
        hc.updateAnnouncementList();
    }


    @FXML
    public void cancelAddAnnouncement(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homePageScene.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Individual Homepage");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();

        HomePageController hc = loader.getController();
        hc.setUser(user);
        hc.updateAnnouncementList();

    }

    @FXML
    public void handleRemoveAnnouncementAction(ActionEvent event) {
        //String crt = (String) ads.getSelectionModel().getSelectedItem();
        Announcement crt = (Announcement) ads.getSelectionModel().getSelectedItem();
        //ArrayList<Announcement> userAds;

        //userPets = user.getPetList();

        //ObservableList<String> userAds = FXCollections.observableArrayList();

        //Cursor<Announcement> cursor = AnnouncementService.getAnnouncementRepository().find(ObjectFilters.eq("user",user));
        //Cursor<Announcement> cursor = AnnouncementService.getAnnouncementRepository().find();

        AnnouncementService.getAnnouncementRepository().remove(crt);

        //UserService.updateUser(user);
        //AnnouncementService.updateAnnouncement(ann);

        this.updateMyAnnouncementList();
    }

    @FXML
    public void redirectToHomePage(ActionEvent event){
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homePage.fxml"));
            Parent root = loader.load();
            currentStage.setTitle("Home");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();

            HomePageController hpc = loader.getController();
            hpc.setUser(user);
            hpc.updateAnnouncementList();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void setUser(User user) throws MalformedURLException {
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(100);
        imageView.setFitHeight(100);
    }
}
