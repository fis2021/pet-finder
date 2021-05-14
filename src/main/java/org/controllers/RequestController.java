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
import org.model.*;
import org.services.AnnouncementService;
import org.services.RequestService;
import org.services.UserService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Optional;

public class RequestController extends Controller{
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
    private Button acceptRequestButton;
    @FXML
    private Button declineRequestButton;
    @FXML
    private Button endRequestButton;

    @FXML
    private Label reqUserLabel;
    @FXML
    private Request request;
    @FXML
    private Text reqUserInfo;
    @FXML
    private Text reqAnnouncementInfo;
    @FXML
    private Text reqStatus;


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

    @FXML
    private Button managePetsButton;
    @FXML
    private Label outboxText;

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

    @FXML
    private Button viewSentButton;

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
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    public void updateRequestList(String listType){
        ArrayList<Request> databaseRequests;

        if(listType.equals("Inbox") || listType.equals("InboxClosed")){
            databaseRequests = RequestService.getUserReceivedRequests(user.getUsername());
        }else if(listType.equals("Outbox") || listType.equals("OutboxClosed")){
            databaseRequests = RequestService.getUserSentRequests(user.getUsername());
        }else{
            return;
        }

        for (Request crtRequest : databaseRequests) {
            Announcement crtAnnouncement = crtRequest.getAnnouncement();

            String crtID = crtRequest.getID();
            String crtStatus = crtRequest.getStatus();
            String crtCategory = crtRequest.getAnnouncement().getCategory();
            String crtSender;
            if(listType.equals("Inbox") || listType.equals("InboxClosed")){
                crtSender = crtRequest.getSender().getUsername();
            }else{
                crtSender = crtRequest.getReceiver().getUsername();
            }
            String crtDate = crtRequest.getDate().toString();
            String crtAnnouncementInfo = crtAnnouncement.getPet().getName() + " - " + crtAnnouncement.getPet().getType();

            RequestTableRow crtRequestRow = new RequestTableRow(crtID, crtStatus, crtCategory, crtSender, crtDate, crtAnnouncementInfo);

            if(listType.equals("Inbox") || listType.equals("Outbox")){
                if(crtRequestRow.getStatus().equals("Pending")){
                    requests.add(crtRequestRow);
                }
            }else{
                if(!crtRequestRow.getStatus().equals("Pending")){
                    requests.add(crtRequestRow);
                }
            }
        }

        requestsTable.setItems(requests);
    }

    @FXML
    public void redirectToViewAnnouncement(ActionEvent event) throws Exception {
        handleViewAnnouncementAction(this.request.getAnnouncement(),event);
    }

    @FXML
    public void handleViewRequestAction(RequestTableRow selected, MouseEvent event){
        String ID = selected.getID();

        Request crt = RequestService.findRequestByID(ID);
        try{
            FXMLLoader loader = redirect(event,"viewRequestDetails.fxml", "Details");
            RequestController rc = loader.getController();
            rc.setUser(user);
            if(crt!=null){
                rc.setRequestInfo(crt);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void reloadViewRequest(ActionEvent event){
        Request crt = this.request;
        try{
            FXMLLoader loader = redirect(event,"viewRequestDetails.fxml", "Details");
            RequestController rc = loader.getController();
            rc.setUser(user);

            if(crt != null){
                rc.setRequestInfo(crt);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void toggleViewTextFields(){
        boolean opposite = !(requestMessage.isVisible());
        if (opposite) {
            requestButton.setText("Cancel");
        } else {
            switch (announcement.getCategory()) {
                case "Adoption":
                    requestButton.setText("Send adoption request");
                    break;
                case "Found":
                    requestButton.setText("Contact finder");
                    break;
                case "Lost":
                    requestButton.setText("Contact owner");
                    break;
                default:
                    requestButton.setVisible(false);
                    break;
            }
        }

        requestMessageLabel.setVisible(opposite);
        requestMessage.setVisible(opposite);
        sendButton.setVisible(opposite);
        exceptionMessage.setVisible(opposite);

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
        super.setUser(user);
        if(managePetsButton != null){
            if(user.getRole().equals("Individual")){
                managePetsButton.setVisible(false);
            }
        }
        if(viewSentButton != null){
            if(user.getRole().equals("Shelter")){
                viewSentButton.setVisible(false);
            }
        }

        if(outboxText != null){
            if(user.getRole().equals("Shelter")){
                outboxText.setVisible(false);
            }
        }
    }

    @FXML
    public void setAnnouncementInfo(Announcement announcement) throws MalformedURLException {
        this.announcement = announcement;
        this.userInfo.setText(announcement.getUser().toString()+"\n"+announcement.getStringDate());
        this.title.setText(announcement.getCategory() + " pet announcement");
        this.body.setText(announcement.getPet().toString()+"\n"+announcement.getInfo());

        File file = new File(announcement.getPet().getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        this.photo.setImage(profile);
        switch (announcement.getCategory()) {
            case "Adoption":
                requestButton.setText("Send adoption request");
                break;
            case "Found":
                requestButton.setText("Contact finder");
                break;
            case "Lost":
                requestButton.setText("Contact owner");
                break;
            default:
                requestButton.setVisible(false);
                break;
        }

        if(announcement.getUser().equals(user)){
            requestButton.setDisable(true);
        }
    }

    @FXML
    public void setRequestInfo(Request request) throws MalformedURLException {
        this.request = request;

        this.announcement = request.getAnnouncement();
        User sender = request.getSender();
        User receiver = request.getReceiver();

        File file = new File(announcement.getPet().getImagePath());
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        this.photo.setImage(profile);

        if(request.getSender().equals(user)){
            reqUserLabel.setText("Request sent to:");
            reqUserInfo.setText(receiver.toString());
        }else{
            reqUserLabel.setText("Request from:");
            reqUserInfo.setText(sender.toString());
        }

        if((!request.getSender().equals(user)) && ((request.getAnnouncement()).getCategory()).equals("Adoption") && request.getStatus().equals("Pending")){
            acceptRequestButton.setVisible(true);
            declineRequestButton.setVisible(true);
        }

        endRequestButton.setDisable(!request.getStatus().equals("Pending"));

        reqStatus.setText(request.getStatus());
        reqAnnouncementInfo.setText(request.getAnnouncement().getInfo());
    }

    @FXML
    public void acceptAdoptionRequest(ActionEvent event){
        request.setStatus("Accepted");
        Announcement announcement = request.getAnnouncement();
        if(user.getRole().equals("Shelter")){
            user.removePet(request.getAnnouncement().getPet());
            UserService.updateUser(user);
        }
        AnnouncementService.removeAnnouncement(announcement);
        RequestService.updateRequest(request);
        reloadViewRequest(event);
    }

    @FXML
    public void declineAdoptionRequest(ActionEvent event){
        request.setStatus("Declined");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Decline message");
        dialog.setHeaderText("Please enter a decline message:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            request.setStatus(request.getStatus() + "\nReason: " + result.get());
        }
        RequestService.updateRequest(request);
        reloadViewRequest(event);
    }

    @FXML
    public void closeRequest(ActionEvent event){
        request.setStatus("Closed");
        RequestService.updateRequest(request);
        reloadViewRequest(event);
    }

    @FXML
    public void redirectToMyClosedRequests() throws IOException {
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "manageClosedRequestsScene.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        RequestController rc = loader.getController();
        rc.setUser(user);
        rc.updateRequestList("InboxClosed");
    }

    @FXML
    public void redirectToMySentRequests() throws IOException {
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "manageSentRequestsScene.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        RequestController rc = loader.getController();
        rc.setUser(user);
        rc.updateRequestList("Outbox");
    }

    @FXML
    public void redirectToMySentClosedRequests() throws IOException {
        Stage currentStage = (Stage) menu.getScene().getWindow();
        String page = "manageClosedSentRequestsScene.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(page));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();
        RequestController rc = loader.getController();
        rc.setUser(user);
        rc.updateRequestList("OutboxClosed");
    }

    @FXML
    public void handleManagePetsAction(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        String page = "shelterManagerScene.fxml";
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
    public void redirectToHomePage(ActionEvent event){
        redirectToHome(event, user);
    }
}
