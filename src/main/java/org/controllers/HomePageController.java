package org.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.dizitart.no2.objects.Cursor;
import org.model.Announcement;
import org.model.ImageStringTableRow;
import org.model.User;
import org.services.AnnouncementService;

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
    private ImageView profilePicture;

    private final ObservableList<ImageStringTableRow> announcements = FXCollections.observableArrayList();

    @FXML
    private TableView<ImageStringTableRow> announcementsTable;
    @FXML
    private TableColumn<ImageStringTableRow, ImageView> announcementImage;
    @FXML
    private TableColumn<ImageStringTableRow, String> announcementInfo;

    @FXML
    private ImageView photo;
    @FXML
    private Text title;
    @FXML
    private Text body;
    @FXML
    private Text userInfo;

    @FXML
    private MenuButton menu;

    public void initialize(){
        if(announcementImage != null && announcementInfo != null){
            announcementImage.setPrefWidth(100);
            announcementImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            announcementInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
            announcementsTable.setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    try{
                        handleViewAnnouncementAction(announcementsTable.getSelectionModel().getSelectedItem(), event);
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            });
        }



    }

    @FXML
    public void handleSignOutAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            currentStage.setTitle("Login");
            currentStage.setScene(new Scene(root, 800, 600));
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
            currentStage.setScene(new Scene(root, 800, 600));
            currentStage.show();
            ShelterManagerController smc = loader.getController();
            smc.setUser(user);
            smc.updateList();
    }

    @FXML
    public void handleViewMyAnnouncements(ActionEvent event) throws Exception{
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "viewMyAnnouncementsScene.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My announcements");
        currentStage.setScene(new Scene(root, 800, 600));
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
        currentStage.setScene(new Scene(root, 800, 600));
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
        profilePicture.setImage(profile);
        //imageView.setImage(profile);
        //imageView.setFitHeight(100);
        //imageView.setFitWidth(150);
        //imageView.rotateProperty();
    }

    @FXML
    public void updateAnnouncementList() throws MalformedURLException {
        //ObservableList<String> allAds = FXCollections.observableArrayList();

        Cursor<Announcement> cursor = AnnouncementService.getAnnouncementRepository().find();

        for (Announcement announcement : cursor) {
            //allAds.add(announcement.toString());

            File file = new File((announcement.getPet()).getImagePath());
            String localUrl = file.toURI().toURL().toExternalForm();
            Image profile = new Image(localUrl, false);
            ImageView crtImg = new ImageView();
            crtImg.setImage(profile);
            crtImg.setFitHeight(100);
            crtImg.setFitWidth(100);

            User crtUser = announcement.getUser();
            String info = announcement.getCategory() + " " + announcement.getPet().getType() + "\n\nName: " + announcement.getPet().getName() + "\n\nPosted on " + announcement.getDatePosted().toString() + " by " + crtUser.getUsername();
            ImageStringTableRow crtAnnouncement = new ImageStringTableRow(crtImg, info, announcement.getID());

            announcements.add(crtAnnouncement);
        }

        //if(allAds.isEmpty()){
        //    allAds.add("Currently there are no announcements");
        //}

        //ads.setItems(allAds);
        announcementsTable.setItems(announcements);
    }

    @FXML
    public void handleViewAnnouncementAction(ImageStringTableRow selected, MouseEvent event) throws Exception{
        String ID = selected.getID();
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            String page = "announcementDetails.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
            Parent root = loader.load();
            currentStage.setTitle("Announcement Details");
            currentStage.setScene(new Scene(root, 800, 600));
            currentStage.show();

            HomePageController hpc = loader.getController();
            hpc.setUser(user);
            Announcement ad = AnnouncementService.getIdAnnouncement(ID);
            if(ad != null){
                hpc.setAnnouncementInfo(ad);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }



        //Alert alert = new Alert(Alert.AlertType.INFORMATION, "Viewing announcement " + ID);
        //alert.showAndWait();
    }

    @FXML
    public void setAnnouncementInfo(Announcement announcement) throws MalformedURLException {
        this.userInfo.setText(announcement.getUser().toString()+"\n"+announcement.getStringDate());
        this.title.setText(announcement.getID());
        this.body.setText(announcement.getPet().toString()+"\n"+announcement.getInfo());

        File file = new File(announcement.getPet().getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        this.photo.setImage(profile);
    }


    @FXML
    public void redirectToHomePage(ActionEvent event){
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homePageScene.fxml"));
            Parent root = loader.load();
            currentStage.setTitle("Home");
            currentStage.setScene(new Scene(root, 800, 600));
            currentStage.show();

            HomePageController hpc = loader.getController();
            hpc.setUser(user);
            hpc.updateAnnouncementList();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
