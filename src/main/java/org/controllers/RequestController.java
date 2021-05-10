package org.controllers;

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
import org.exceptions.RequestAlreadyExistsException;
import org.model.*;
import org.services.AnnouncementService;
import org.services.RequestService;

import java.io.File;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class RequestController {
    @FXML
    private User user;
    @FXML
    private Text AccountStatus;
    @FXML
    private ImageView profilePicture;
    @FXML
    private MenuButton menu;

    @FXML
    private Announcement announcement;
    @FXML
    private ImageView photo;
    @FXML
    private Text title;
    @FXML
    private Text body;
    @FXML
    private Text userInfo;

    @FXML
    private Request request;

    @FXML
    private Button requestButton;
    @FXML
    private Label exceptionMessage;
    @FXML
    private Label requestMessageLabel;
    @FXML
    private TextField requestMessage;
    @FXML
    private Button sendButton;

    private final ObservableList<RequestTableRow> requests = FXCollections.observableArrayList();

    @FXML
    private TableView<RequestTableRow> requestsTable;
    @FXML
    private TableColumn<RequestTableRow, String> requestStatus;
    @FXML
    private TableColumn<RequestTableRow, String> requestCategory;
    @FXML
    private TableColumn<RequestTableRow, String> requestSender;
    @FXML
    private TableColumn<RequestTableRow, String> requestDate;
    @FXML
    private TableColumn<RequestTableRow, String> requestAnnouncement;

    public void initialize(){
        if(requestStatus != null && requestCategory != null && requestSender != null && requestDate != null && requestAnnouncement != null && requestsTable != null){
            requestStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            requestCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            requestSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
            requestDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            requestAnnouncement.setCellValueFactory(new PropertyValueFactory<>("announcementInfo"));

            requestsTable.setOnMouseClicked((MouseEvent event) -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    try{
                        handleViewRequestAction(requestsTable.getSelectionModel().getSelectedItem(), event);
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            });
        }
    }

    @FXML
    public void updateRequestList(String listType) throws MalformedURLException {

        ArrayList<Request> databaseRequests;

        if(listType.equals("Inbox")){
            databaseRequests = RequestService.getUserReceivedRequests(user.getUsername());
        }else if(listType.equals("Outbox")){
            databaseRequests = RequestService.getUserSentRequests(user.getUsername());
        }else{
            return;
        }

        for (Request crtRequest : databaseRequests) {
            Announcement crtAnnouncement = crtRequest.getAnnouncement();

            String crtID = crtRequest.getID();
            String crtStatus = crtRequest.getStatus();
            String crtCategory = crtRequest.getAnnouncement().getCategory();
            String crtSender = crtRequest.getSender().getUsername();
            String crtDate = crtRequest.getDate().toString();
            String crtAnnouncementInfo = crtAnnouncement.getPet().getName() + " - " + crtAnnouncement.getPet().getType();

            RequestTableRow crtRequestRow = new RequestTableRow(crtID, crtStatus, crtCategory, crtSender, crtDate, crtAnnouncementInfo);

            requests.add(crtRequestRow);
        }

        requestsTable.setItems(requests);
    }

    @FXML
    public void handleViewRequestAction(RequestTableRow selected, MouseEvent event){
        String ID = selected.getID();

        Request crt = RequestService.findRequestByID(ID);

        try{
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Viewing request ID:\n" + crt.getID());
            a.showAndWait();
            if(false){
                Node node = (Node) event.getSource();
                Stage currentStage = (Stage) node.getScene().getWindow();
                String page = "requestDetails.fxml";

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
                Parent root = loader.load();
                currentStage.setTitle("View Request");
                currentStage.setScene(new Scene(root, 800, 600));
                currentStage.show();

                RequestController rc = loader.getController();
                rc.setUser(user);

                if(crt != null){
                    rc.setRequestInfo(crt);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void toggleViewTextFields() throws IOException {
        try {
            if (RequestService.requestExists(this.user, announcement.getUser(), this.announcement)) {
                throw new RequestAlreadyExistsException();
            }

            boolean opposite = !(requestMessage.isVisible());
            if (opposite == true) {
                requestButton.setText("Cancel");
            } else {
                if (announcement.getCategory().equals("Adoption")) {
                    requestButton.setText("Send adoption request");
                } else if (announcement.getCategory().equals("Found")) {
                    requestButton.setText("Contact finder");
                } else if (announcement.getCategory().equals("Lost")) {
                    requestButton.setText("Contact owner");
                } else {
                    requestButton.setVisible(false);
                }
            }

            requestMessageLabel.setVisible(opposite);
            requestMessage.setVisible(opposite);
            sendButton.setVisible(opposite);
            exceptionMessage.setVisible(opposite);

        }catch(RequestAlreadyExistsException e){
            Alert a = new Alert(Alert.AlertType.INFORMATION, "You already have a request on this announcement pending reviewal");
            a.showAndWait();
            redirectToMyRequests();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRequestAction(){
        try{
            if(requestMessage.getText().equals("")){
                throw new IOException("Please enter a message");
            }
            Request crt = new Request(this.user, announcement.getUser(), this.announcement);
            crt.setMessage(requestMessage.getText());

            RequestService.sendRequest(crt);
            redirectToMyRequests();
        }catch(Exception e){
            exceptionMessage.setText(e.getMessage());
        }
    }

    public void setUser(User user) throws MalformedURLException {
        this.user = user;
        AccountStatus.setText("Logged-in as " + user.getUsername());

        File file = new File(user.getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        profilePicture.setImage(profile);
    }

    @FXML
    public void setAnnouncementInfo(Announcement announcement) throws MalformedURLException {
        this.announcement = announcement;
        this.userInfo.setText(announcement.getUser().toString()+"\n"+announcement.getStringDate());
        this.title.setText(announcement.getID());
        this.body.setText(announcement.getPet().toString()+"\n"+announcement.getInfo());

        File file = new File(announcement.getPet().getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        this.photo.setImage(profile);
        if(announcement.getCategory().equals("Adoption")){
            requestButton.setText("Send adoption request");
        }else if(announcement.getCategory().equals("Found")){
            requestButton.setText("Contact finder");
        }else if(announcement.getCategory().equals("Lost")){
            requestButton.setText("Contact owner");
        }else{
            requestButton.setVisible(false);
        }

        if(announcement.getUser().equals(user)){
            requestButton.setDisable(true);
        }
    }

    @FXML
    public void setRequestInfo(Request request) throws MalformedURLException {
        this.request = request;
        this.userInfo.setText(announcement.getUser().toString()+"\n"+announcement.getStringDate());
        this.title.setText(announcement.getID());
        this.body.setText(announcement.getPet().toString()+"\n"+announcement.getInfo());
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
}
