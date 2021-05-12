package org.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
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

public class ShelterManagerController {

    private User user;

    @FXML
    private TextField petName;

    @FXML
    private TextField petInfo;

    @FXML
    private ChoiceBox type = new ChoiceBox();

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
    private ImageView profilePicture;

    @FXML
    private TableView<ImageStringTableRow> petsTable;
    @FXML
    private TableColumn<ImageStringTableRow, ImageView> petImage;
    @FXML
    private TableColumn<ImageStringTableRow, String> petInformation;

    @FXML
    public void initialize() throws MalformedURLException {
        type.getItems().addAll("Dog", "Cat", "Other");

        if(petImage != null && petInformation != null && petsTable != null){
            petImage.setPrefWidth(100);
            petImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            petInformation.setCellValueFactory(new PropertyValueFactory<>("info"));
        }
    }

    @FXML
    public void updateList() throws MalformedURLException {
        ObservableList<ImageStringTableRow> pets = FXCollections.observableArrayList();
        ArrayList<Pet> crtPetList = user.getPetList();

        for(Pet pet : crtPetList){
            //crtPets.add(pet.toString());
            File file = new File(pet.getImagePath());
            String localUrl = file.toURI().toURL().toExternalForm();
            Image profile = new Image(localUrl, false);
            ImageView crtImg = new ImageView();
            crtImg.setImage(profile);
            crtImg.setFitHeight(100);
            crtImg.setFitWidth(100);

            String crtInfo = pet.toString();

            ImageStringTableRow crtPet = new ImageStringTableRow(crtImg, crtInfo, pet.getID());
            pets.add(crtPet);
        }

        petsTable.setItems(pets);
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
        imagePath = "src/main/resources/img/pet.jpg";
        File file = new File(imagePath);
        String localUrl = file.toURI().toURL().toExternalForm();
        Image profile = new Image(localUrl, false);
        imageView.setImage(profile);
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
        imageView.rotateProperty();
    }

    @FXML
    public void handleAddPetAction(ActionEvent event) throws IOException {
        if(petName.getText() == "" || type.getValue() == null){
            AddStatus.setText("Name and type are required!");
            return;
        }
        Pet crt = new Pet(petName.getText(), (String) type.getValue());

        crt.setInfo(petInfo.getText());
        crt.setImagePath(imagePath);
        user.addPet(crt);
        UserService.updateUser(user);
        Announcement crtAd = new Announcement(crt,user,"Adoption");
        crtAd.setInfo("");
        AnnouncementService.addAnnouncement(crtAd);

        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shelterManagerScene.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Manage pets");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();

        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
        smc.updateList();
    }

    @FXML
    public void handleRemovePetAction(ActionEvent event) throws MalformedURLException {
        ImageStringTableRow crt = (ImageStringTableRow) petsTable.getSelectionModel().getSelectedItem();

        String crtPetID = crt.getID();
        ArrayList<Pet> userPets;
        userPets = user.getPetList();

        Announcement ad = null;

        Pet crtPet = null;

        for(Pet pet : userPets){
            if(pet.getID().equals(crtPetID)){
                crtPet=pet;
            }
        }

        if(crtPet!=null){
            userPets.remove(crtPet);
            this.user.removePet(crtPet);
            ad = AnnouncementService.getPetAnnouncement(crtPet);
        }

        if(ad!=null) { AnnouncementService.removeAnnouncement(ad); }
        UserService.updateUser(user);

        this.updateList();
    }

    @FXML
    public void cancelAddPet(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shelterManagerScene.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("Manage pets");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();

        ShelterManagerController smc = loader.getController();
        smc.setUser(user);
        smc.updateList();
    }

    @FXML
    public void openAddPetPage(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addPetPage.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("AddPet");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();

        ShelterManagerController smc = loader.getController();
        smc.setUser(user);

    }

    @FXML
    public void redirectToShelterHomePage(ActionEvent event){
        try {
            Node node = (Node) event.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shelterHomePage.fxml"));
            Parent root = loader.load();
            currentStage.setTitle("Home");
            currentStage.setScene(new Scene(root, 500, 500));
            currentStage.show();

            HomePageController hpc = loader.getController();
            hpc.setUser(user);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void redirectToHomePage(ActionEvent event) throws Exception{
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("manageRequestsScene.fxml"));
        Parent root = loader.load();
        currentStage.setTitle("My requests");
        currentStage.setScene(new Scene(root, 800, 600));
        currentStage.show();

        RequestController rc = loader.getController();
        rc.setUser(user);
        rc.updateRequestList("Inbox");
    }


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
}
