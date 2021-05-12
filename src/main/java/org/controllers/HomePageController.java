package org.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.model.Announcement;
import org.model.ImageStringTableRow;
import org.model.User;
import org.services.AnnouncementService;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class HomePageController extends Controller{
    private final ObservableList<ImageStringTableRow> announcements = FXCollections.observableArrayList();

    @FXML
    private TableView<ImageStringTableRow> announcementsTable;
    @FXML
    private TableColumn<ImageStringTableRow, ImageView> announcementImage;
    @FXML
    private TableColumn<ImageStringTableRow, String> announcementInfo;

    @FXML
    private ChoiceBox petType = new ChoiceBox();

    @FXML
    private ChoiceBox category = new ChoiceBox();

    public void initialize(){
        category.getItems().addAll("All","Lost", "Found", "Adoption");
        petType.getItems().addAll("All","Cat","Dog","Other");
        //category.getSelectionModel().selectedIndexProperty().addListener();
        if(announcementImage != null && announcementInfo != null){
            announcementImage.setPrefWidth(100);
            announcementImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            announcementInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
            announcementsTable.setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    try{
                        handleViewAnnouncementAction(AnnouncementService.getIdAnnouncement(announcementsTable.getSelectionModel().getSelectedItem().getID()), event);
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            });
        }
    }

    @FXML
    public void updateAnnouncementList() throws MalformedURLException {
        ArrayList <Announcement> cursor=AnnouncementService.getAnnouncements();

        if(category.getValue() != null  && (petType.getValue() ==null || petType.getValue().equals("All"))){
            announcementsTable.getItems().clear();
            cursor=AnnouncementService.getCategoryAnnouncements((String) category.getValue());
        }
        if((category.getValue() == null || category.getValue().equals("All")) && petType.getValue() !=null){
            announcementsTable.getItems().clear();
            cursor=AnnouncementService.getPetTypeAnnouncements((String) petType.getValue());
        }
        if(category.getValue() != null && petType.getValue() !=null){
            announcementsTable.getItems().clear();
            cursor=AnnouncementService.getCategoryPetTypeAnnouncements((String) category.getValue(),(String) petType.getValue());
        }

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
    public void redirectToHomePage(ActionEvent event){
        redirectToHome(event, user);
    }
}
