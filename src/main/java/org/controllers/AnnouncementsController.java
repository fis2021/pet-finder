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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.model.Announcement;
import org.model.ImageStringTableRow;
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
    private ImageView profilePicture;

    private final ObservableList<ImageStringTableRow> announcements = FXCollections.observableArrayList();

    @FXML
    private TableView<ImageStringTableRow> announcementsTable;
    @FXML
    private TableColumn<ImageStringTableRow, ImageView> announcementImage;
    @FXML
    private TableColumn<ImageStringTableRow, String> announcementInfo;


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
    public void initialize() throws MalformedURLException {
        category.getItems().addAll("Lost", "Found", "Adoption");
        petType.getItems().addAll("Cat","Dog","Other");
        if(announcementImage != null && announcementInfo != null){
            announcementImage.setPrefWidth(100);
            announcementImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            announcementInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
            announcementsTable.setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    try{
                        //handleViewMyAnnouncementAction(announcementsTable.getSelectionModel().getSelectedItem(), event);
                        ;
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
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void updateMyAnnouncementList() throws MalformedURLException{

        /*ObservableList<String> crtAds = FXCollections.observableArrayList();

        ArrayList<Announcement> userAds=AnnouncementService.getUserAnnouncements(user.getUsername());
        for(Announcement announcement : userAds){
            crtAds.add(announcement.toString());
        }
        if(crtAds.isEmpty()){
            crtAds.add("Currently you have no announcements");
        }
        ads.setItems(crtAds);*/

        //ObservableList<String> allAds = FXCollections.observableArrayList();

        ArrayList<Announcement> cursor=AnnouncementService.getUserAnnouncements(user.getUsername());
        //Cursor<Announcement> cursor = AnnouncementService.getAnnouncementRepository().find();

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
    public void handleAddAnnouncement(ActionEvent event) throws Exception{
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
    public void handleRemoveAnnouncementAction(ActionEvent event) throws MalformedURLException {

        //String crt = (String) ads.getSelectionModel().getSelectedItem();
        ImageStringTableRow crt = announcementsTable.getSelectionModel().getSelectedItem();
        ArrayList<Announcement> userAds=AnnouncementService.getUserAnnouncements(user.getUsername());
        Announcement ad = null;
        for(Announcement announcement : userAds){
            if(crt.getID().equals(announcement.getID())){
                ad = announcement;
            }
            /*if(announcement.toString().equals(crt)){
                ad = announcement;
            }*/
        }
        if(ad != null){
            userAds.remove(ads);
            AnnouncementService.removeAnnouncement(ad);
            //announcementsTable.refresh();
            announcementsTable.getItems().remove(crt);
        }
        //announcementsTable.refresh();
        //this.updateMyAnnouncementList();

    }



    @FXML
    public void handleEditAnnouncementAction(ActionEvent event) throws Exception{
        Announcement crt = (Announcement) ads.getSelectionModel().getSelectedItem();

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


    public void setUser(User user) throws MalformedURLException {
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());

        File file = new File(user.getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        profilePicture.setImage(profile);

        /*String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(100);
        imageView.setFitHeight(100);*/
    }
}
