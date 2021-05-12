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
import org.services.RequestService;
import org.services.UserService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class AnnouncementsController extends Controller{
    @FXML
    private TextField petName;
    @FXML
    private TextField petInfo;
    @FXML
    private ChoiceBox<String> petType = new ChoiceBox<>();

    @FXML
    private ListView ads = new ListView<>();

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
    private ChoiceBox<String> category = new ChoiceBox<>();

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
        if(announcementImage != null && announcementInfo != null){
            announcementImage.setPrefWidth(100);
            announcementImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            announcementInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        }
    }

    @FXML
    public void updateMyAnnouncementList() throws MalformedURLException{
        ArrayList<Announcement> cursor=AnnouncementService.getUserAnnouncements(user.getUsername());

        for (Announcement announcement : cursor) {
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
        Image petImg = new Image(localUrl, false);
        imageView.setImage(petImg);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.rotateProperty();
    }

    @FXML
    public void handleAddAnnouncementAction(ActionEvent event) {
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

        redirectToHome(event, user);
    }

    @FXML
    public void cancelAddAnnouncement(ActionEvent event) throws IOException {
        redirectToHome(event, user);
    }

    @FXML
    public void handleRemoveAnnouncementAction(ActionEvent event) throws MalformedURLException {
        ImageStringTableRow crt = announcementsTable.getSelectionModel().getSelectedItem();
        ArrayList<Announcement> userAds=AnnouncementService.getUserAnnouncements(user.getUsername());
        Announcement ad = null;
        for(Announcement announcement : userAds){
            if(crt.getID().equals(announcement.getID())){
                ad = announcement;
            }
        }
        if(ad != null){
            RequestService.closeAllRequestsRelatedToAnnouncement(ad);
            AnnouncementService.removeAnnouncement(ad);
            announcementsTable.getItems().remove(crt);
        }
    }

    @FXML
    public void redirectToHomePage(ActionEvent event){
        redirectToHome(event,user);
    }
}
